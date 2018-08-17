/******************************************************************************
 *                                                                            *
 *                    Copyright 2017 Subterranean Security                    *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package com.sandpolis.server.exe;

import static com.sandpolis.core.util.ProtoUtil.rq;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sandpolis.core.net.ExeletTest;
import com.sandpolis.core.net.Sock;
import com.sandpolis.core.proto.net.MCPing.RQ_Ping;
import com.sandpolis.core.proto.net.MCServer.RQ_ServerBanner;
import com.sandpolis.core.proto.net.MSG.Message;
import com.sandpolis.core.proto.net.MSG.Message.MsgOneofCase;

class ServerExeTest extends ExeletTest {

	private ServerExe exe;

	@BeforeEach
	void setup() {
		initChannel();
		exe = new ServerExe(new Sock(channel));
	}

	@Test
	void testDeclaration() {
		testDeclaration(ServerExe.class);
	}

	@Test
	void testRqServerBanner() {
		exe.rq_server_banner(rq(RQ_ServerBanner.newBuilder()).build());

		assertEquals(MsgOneofCase.RS_SERVER_BANNER, ((Message) channel.readOutbound()).getMsgOneofCase());
	}

	@Test
	void testRqPing() {
		exe.rq_ping(rq(RQ_Ping.newBuilder()).build());

		assertEquals(MsgOneofCase.RS_PING, ((Message) channel.readOutbound()).getMsgOneofCase());
	}

}