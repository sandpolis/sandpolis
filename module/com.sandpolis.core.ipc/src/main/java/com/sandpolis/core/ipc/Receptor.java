//============================================================================//
//                                                                            //
//                Copyright © 2015 - 2020 Subterranean Security               //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation at:                                //
//                                                                            //
//    https://mozilla.org/MPL/2.0                                             //
//                                                                            //
//=========================================================S A N D P O L I S==//
package com.sandpolis.core.ipc;

import static com.sandpolis.core.ipc.IPCStore.IPCStore;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;
import com.sandpolis.core.ipc.Message.MSG;

/**
 * A simple IPC receiver that processes messages serially from an underlying
 * {@link Socket}.
 *
 * @author cilki
 * @since 5.0.0
 */
public class Receptor implements Runnable, Closeable {

	public static final Logger log = LoggerFactory.getLogger(Receptor.class);

	/**
	 * A {@link RateLimiter} that limits message processing to prevent abuse.
	 * Connections that exceed the limit are closed.
	 */
	private final RateLimiter messageLimiter = RateLimiter.create(4);

	/**
	 * The connection's socket.
	 */
	private Socket socket;

	/**
	 * The receptor task's {@link Future}.
	 */
	private Future<?> future;

	/**
	 * Create a new {@link Receptor} around an established socket.
	 *
	 * @param socket A socket pre-established by an IPC Listener.
	 */
	public Receptor(Socket socket) {
		this.socket = Objects.requireNonNull(socket);
	}

	@Override
	public void run() {
		try (Socket s = socket; InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
			MSG message;
			while (!s.isClosed()) {
				message = MSG.parseDelimitedFrom(in);
				if (message == null)
					// Stream reached EOF
					return;
				if (!messageLimiter.tryAcquire())
					// Limit exceeded; close the connection
					return;

				// Delegate to handler
				if (IPCStore.getHandlers().containsKey(message.getPayloadCase()))
					IPCStore.getHandlers().get(message.getPayloadCase()).handle(message, out);
				else
					log.warn("Dropping unknown IPC message: {}", message.getPayloadCase());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// Remove this receptor
			IPCStore.getMutableReceptors().remove(this);
		}
	}

	/**
	 * Start the receptor on the given {@link ExecutorService}.
	 *
	 * @param service The executor service
	 */
	public void start(ExecutorService service) {
		if (future != null)
			throw new IllegalStateException("Receptor already running");
		if (socket.isClosed())
			throw new IllegalStateException("Socket closed");

		future = service.submit(this);
	}

	@Override
	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (future != null)
			future.cancel(true);
	}
}
