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
package com.sandpolis.core.profile.cmd;

import com.sandpolis.core.net.command.Cmdlet;
import com.sandpolis.core.net.future.ResponseFuture;
import com.sandpolis.core.net.stream.InboundStreamAdapter;
import com.sandpolis.core.net.stream.StreamSink;
import com.sandpolis.core.net.stream.StreamStore;
import com.sandpolis.core.proto.net.MsgStream.EV_ProfileStream;
import com.sandpolis.core.proto.net.MsgStream.RQ_ProfileStream;
import com.sandpolis.core.proto.util.Result.Outcome;
import com.sandpolis.core.util.IDUtil;

/**
 * Contains profile commands.
 *
 * @author cilki
 * @since 6.1.0
 */
public final class ProfileCmd extends Cmdlet<ProfileCmd> {

	public ResponseFuture<Outcome> openProfileStream() {
		int id = IDUtil.stream();

		// StreamStore.StreamStore.add(new InboundStreamAdapter<>(id, sock, msg ->
		// msg.getEvProfileStream()), null);
		return request(RQ_ProfileStream.newBuilder().setId(id));
	}

	/**
	 * Prepare for an asynchronous command.
	 *
	 * @return A configurable object from which all asynchronous (nonstatic)
	 *         commands in {@link ProfileCmd} can be invoked
	 */
	public static ProfileCmd async() {
		return new ProfileCmd();
	}

	private ProfileCmd() {
	}
}