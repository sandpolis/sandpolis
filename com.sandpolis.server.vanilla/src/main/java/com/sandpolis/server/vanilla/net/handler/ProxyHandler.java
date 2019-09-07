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
package com.sandpolis.server.vanilla.net.handler;

import static com.sandpolis.core.net.store.connection.ConnectionStore.ConnectionStore;

import com.sandpolis.core.net.exception.InvalidMessageException;
import com.sandpolis.core.net.init.ChannelConstant;
import com.sandpolis.core.net.init.PipelineInitializer.ProtobufShortcutFrameEncoder;
import com.sandpolis.core.proto.net.MCNetwork.EV_EndpointClosed;
import com.sandpolis.core.proto.net.MSG.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * This handler reads the first two fields of an incoming {@link Message} to
 * determine its destination. If the destination is another instance, the
 * {@link ByteBuf} will be efficiently forwarded. Otherwise the {@link ByteBuf}
 * will be decoded and executed for this instance.<br>
 * <br>
 *
 * This handler MUST be placed after a {@link ProtobufVarint32FrameDecoder}!
 * Otherwise a malicious instance could send messages to unauthorized instances
 * by sending two messages in rapid succession. Since this handler only verifies
 * the first few bytes and then routes the entire buffer accordingly, sending
 * one small valid message followed by an invalid message would lead to the
 * invalid message being delivered to the receiver.
 *
 * @author cilki
 * @since 5.0.0
 */
@Sharable
public class ProxyHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * The server's CVID which is used in determining when to route messages.
	 */
	private final int cvid;

	public ProxyHandler(int cvid) {
		this.cvid = cvid;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		msg.markReaderIndex();

		// Read field: "to"
		if (msg.isReadable() && msg.readByte() == 0x08) {
			int to = readCvid(msg);

			// Perform redirection if necessary
			if (to != cvid) {

				// Read field: "from"
				if (msg.isReadable() && msg.readByte() == 0x10) {
					int from = readCvid(msg);

					// Verify the from field to prevent spoofing
					if (ctx.channel().attr(ChannelConstant.CVID).get() != from) {
						throw new InvalidMessageException("Message 'from' does not match channel's CVID");
					}
				} else {
					throw new InvalidMessageException("Message specifies 'to' but not 'from'");
				}

				// Route the message
				var sock = ConnectionStore.get(to);
				if (sock.isPresent()) {
					msg.resetReaderIndex();
					msg.retain();

					// Skip to the middle of the pipeline
					((ProtobufShortcutFrameEncoder) sock.get().channel().pipeline().get("protobuf.frame_encoder"))
							.shortcut(msg);
				} else {
					ctx.channel().writeAndFlush(Message.newBuilder()
							.setEvEndpointClosed(EV_EndpointClosed.newBuilder().setCvid(to)).build());
				}

				return;
			}
		}

		msg.resetReaderIndex();
		ReferenceCountUtil.retain(msg);
		ctx.fireChannelRead(msg);
	}

	/**
	 * Read a CVID varint from the given {@link ByteBuf}.
	 *
	 * @param buffer The buffer to read (read pointer will be modified)
	 * @return The decoded CVID
	 */
	private static int readCvid(ByteBuf buffer) {
		byte tmp = buffer.readByte();
		if (tmp >= 0) {
			return tmp;
		} else {
			int result = tmp & 127;
			if ((tmp = buffer.readByte()) >= 0) {
				result |= tmp << 7;
			} else {
				result |= (tmp & 127) << 7;
				if ((tmp = buffer.readByte()) >= 0) {
					result |= tmp << 14;
				} else {
					result |= (tmp & 127) << 14;
					if ((tmp = buffer.readByte()) >= 0) {
						result |= tmp << 21;
					} else {
						result |= (tmp & 127) << 21;
						result |= (tmp = buffer.readByte()) << 28;
						if (tmp < 0) {
							// The CVID is negative (negative varints are always 10 bytes)
							throw new CorruptedFrameException("Invalid CVID");
						}
					}
				}
			}
			if (result < 0)
				throw new CorruptedFrameException("Invalid CVID");

			return result;
		}
	}
}
