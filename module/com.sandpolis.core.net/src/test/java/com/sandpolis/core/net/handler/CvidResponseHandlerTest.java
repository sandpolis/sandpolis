/*******************************************************************************
 *                                                                             *
 *                Copyright © 2015 - 2019 Subterranean Security                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *                                                                             *
 ******************************************************************************/
package com.sandpolis.core.net.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sandpolis.core.net.init.ChannelConstant;
import com.sandpolis.core.proto.net.MCCvid.RQ_Cvid;
import com.sandpolis.core.proto.net.MCCvid.RS_Cvid;
import com.sandpolis.core.proto.net.MSG.Message;
import com.sandpolis.core.proto.util.Platform.Instance;
import com.sandpolis.core.util.IDUtil;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.concurrent.DefaultPromise;

public class CvidResponseHandlerTest {

	private static final CvidResponseHandler serverHandler = new CvidResponseHandler();
	private EmbeddedChannel server;

	@BeforeEach
	public void setup() {
		server = new EmbeddedChannel();
		server.pipeline().addLast("cvid", serverHandler);
		server.attr(ChannelConstant.FUTURE_CVID).set(new DefaultPromise<>(new DefaultEventLoop()));
		server.attr(ChannelConstant.HANDLER_EXELET).set(new ExeletHandler(null));
	}

	@Test
	public void testReceiveIncorrect() {
		assertNotNull(server.pipeline().get("cvid"));
		assertFalse(server.attr(ChannelConstant.FUTURE_CVID).get().isDone());
		server.writeInbound(Message.newBuilder()
				.setRqCvid(RQ_Cvid.newBuilder().setInstance(Instance.SERVER).setUuid("testuuid2")).build());
		assertTrue(server.attr(ChannelConstant.FUTURE_CVID).get().isDone());
		assertFalse(server.attr(ChannelConstant.FUTURE_CVID).get().isSuccess());
		assertNull(server.pipeline().get("cvid"));
	}

	@Test
	public void testReceiveCorrect() {
		assertNotNull(server.pipeline().get("cvid"));
		assertFalse(server.attr(ChannelConstant.FUTURE_CVID).get().isDone());
		server.writeInbound(Message.newBuilder()
				.setRqCvid(RQ_Cvid.newBuilder().setInstance(Instance.CLIENT).setUuid("testuuid2")).build());
		assertTrue(server.attr(ChannelConstant.FUTURE_CVID).get().isDone());
		assertTrue(server.attr(ChannelConstant.FUTURE_CVID).get().isSuccess());

		assertEquals(Instance.CLIENT, IDUtil.CVID.extractInstance(server.attr(ChannelConstant.CVID).get()));
		assertEquals("testuuid2", server.attr(ChannelConstant.UUID).get());
		assertNull(server.pipeline().get("cvid"));

		Message msg = server.readOutbound();
		RS_Cvid rs = msg.getRsCvid();

		assertEquals(Instance.CLIENT, IDUtil.CVID.extractInstance(rs.getCvid()));
		assertFalse(rs.getServerUuid().isEmpty());

	}

}
