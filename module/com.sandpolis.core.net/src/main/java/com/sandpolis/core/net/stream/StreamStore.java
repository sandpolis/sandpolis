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
package com.sandpolis.core.net.stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.sandpolis.core.net.store.connection.ConnectionStore.ConnectionStore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.sandpolis.core.instance.store.StoreBase;
import com.sandpolis.core.instance.store.StoreConfig;
import com.sandpolis.core.net.store.connection.ConnectionStoreEvents.SockLostEvent;
import com.sandpolis.core.net.stream.StreamStore.StreamStoreConfig;

/**
 * The {@link StreamStore} contains four "banks" of stream endpoints that each
 * serve a specific purpose.
 * <ul>
 * <li><b>INBOUND:</b> Receives and unwraps events from the network and routes
 * them into the OUTBOUND or SINK banks.</li>
 * <li><b>OUTBOUND:</b> Receives events from the INBOUND or SOURCE banks and
 * routes them into the network.</li>
 * <li><b>SOURCE:</b> Streams in this bank produce events and routes them into
 * the OUTBOUND or SINK banks.</li>
 * <li><b>SINK:</b> Streams in this bank consume events from the INBOUND or
 * SOURCE banks.</li>
 * </ul>
 *
 * <p>
 * This architecture elegantly supports multicast streams. For example, an
 * endpoint in the INBOUND bank can easily and efficiently route incoming
 * messages from one connection to multiple outgoing connections and vice-versa.
 *
 * <p>
 * The graphic below illustrates a multicast stream A and a unicast stream B.
 *
 * <pre>
 * IN     OUT    SRC    SINK
 * [A]    [B]    [B]    [A]
 * [ ]    [A]    [ ]    [ ]
 * [ ]    [A]    [ ]    [ ]
 * [ ]    [ ]    [ ]    [ ]
 * [ ]    [ ]    [ ]    [ ]
 * </pre>
 *
 * @author cilki
 * @since 5.0.2
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class StreamStore extends StoreBase<StreamStoreConfig> {

	private static final Logger log = LoggerFactory.getLogger(StreamStore.class);

	public StreamStore() {
		super(log);
	}

	/**
	 * The SOURCE bank.
	 */
	private List<StreamSource> source;

	/**
	 * The SINK bank.
	 */
	private List<StreamSink> sink;

	/**
	 * The INBOUND bank.
	 */
	private List<InboundStreamAdapter> inbound;

	/**
	 * The OUTBOUND bank.
	 */
	private List<OutboundStreamAdapter> outbound;

	public synchronized <E extends MessageOrBuilder> void add(InboundStreamAdapter<E> in,
			OutboundStreamAdapter<E> out) {
		checkArgument(!in.isSubscribed(out));
		in.subscribe(out);

		this.inbound.add(in);
		this.outbound.add(out);
	}

	public synchronized <E extends MessageOrBuilder> void add(InboundStreamAdapter<E> in, StreamSink<E> sink) {
		checkArgument(!in.isSubscribed(sink));
		in.subscribe(sink);

		this.inbound.add(in);
		this.sink.add(sink);
	}

	public synchronized <E extends MessageOrBuilder> void add(StreamSource<E> source, OutboundStreamAdapter<E> out) {
		checkArgument(!source.isSubscribed(out));
		source.subscribe(out);

		this.source.add(source);
		this.outbound.add(out);
	}

	public synchronized <E extends MessageOrBuilder> void add(StreamSource<E> source, StreamSink<E> sink) {
		checkArgument(!source.isSubscribed(sink));
		source.subscribe(sink);

		this.source.add(source);
		this.sink.add(sink);
	}

	public synchronized void streamData(int id, Message data) {
		inbound.stream().filter(adapter -> adapter.getStreamID() == id).findFirst().ifPresent(adapter -> {
			adapter.submit(data);
		});
	}

	/**
	 * Immediately halt a stream by its ID. Other streams may also be stopped as a
	 * result of stopping the target stream.
	 *
	 * @param id The stream ID
	 */
	public synchronized void stop(int id) {
		inbound.removeIf(adapter -> {
			if (adapter.getStreamID() == id) {
				adapter.close();
				return true;
			}
			return false;
		});
		outbound.removeIf(adapter -> {
			if (adapter.getStreamID() == id) {
//				adapter.close();
				return true;
			}
			return false;
		});
		source.removeIf(source -> {
			if (source.getStreamID() == id) {
				source.close();
				return true;
			}
			return false;
		});
		sink.removeIf(sink -> {
			if (sink.getStreamID() == id) {
				sink.close();
				return true;
			}
			return false;
		});

		// TODO find dependent streams to also close
	}

	@Subscribe
	private synchronized void onSockLost(SockLostEvent event) {
		inbound.removeIf(adapter -> {
			if (adapter.getSock().equals(event.get())) {
				adapter.close();
				return true;
			}
			return false;
		});
		outbound.removeIf(adapter -> {
			if (adapter.getSock().equals(event.get())) {
//				adapter.close();
				return true;
			}
			return false;
		});

		// TODO find dependent streams to also close
	}

	@Override
	public StreamStore init(Consumer<StreamStoreConfig> configurator) {
		var config = new StreamStoreConfig();
		configurator.accept(config);

		ConnectionStore.register(this);

		return (StreamStore) super.init(null);
	}

	public final class StreamStoreConfig extends StoreConfig {

		@Override
		public void ephemeral() {
			source = new ArrayList<>();
			sink = new ArrayList<>();
			inbound = new ArrayList<>();
			outbound = new ArrayList<>();
		}
	}

	public static final StreamStore StreamStore = new StreamStore();
}
