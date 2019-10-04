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
package com.sandpolis.server.vanilla.exe;

import static com.sandpolis.core.proto.util.Result.ErrorCode.ACCESS_DENIED;
import static com.sandpolis.core.util.ProtoUtil.begin;
import static com.sandpolis.core.util.ProtoUtil.complete;
import static com.sandpolis.core.util.ProtoUtil.failure;
import static com.sandpolis.core.util.ProtoUtil.success;
import static com.sandpolis.server.vanilla.store.listener.ListenerStore.ListenerStore;

import com.google.protobuf.MessageOrBuilder;
import com.sandpolis.core.net.command.Exelet;
import com.sandpolis.core.net.handler.exelet.ExeletContext;
import com.sandpolis.core.proto.net.MCListener.RQ_AddListener;
import com.sandpolis.core.proto.net.MCListener.RQ_ChangeListener;
import com.sandpolis.core.proto.net.MCListener.RQ_ListenerDelta;
import com.sandpolis.core.proto.net.MCListener.RQ_RemoveListener;
import com.sandpolis.core.proto.net.MSG;
import com.sandpolis.server.vanilla.store.listener.Listener;

/**
 * Listener message handlers.
 *
 * @author cilki
 * @since 5.0.0
 */
public final class ListenerExe extends Exelet {

	@Auth
	@Permission(permission = 0/* server.listener.create */)
	@Handler(tag = MSG.Message.RQ_ADD_LISTENER_FIELD_NUMBER)
	public static MessageOrBuilder rq_add_listener(RQ_AddListener rq) {
		var outcome = begin();

		ListenerStore.add(rq.getConfig());
		return success(outcome);
	}

	@Auth
	@Handler(tag = MSG.Message.RQ_REMOVE_LISTENER_FIELD_NUMBER)
	public static MessageOrBuilder rq_remove_listener(ExeletContext context, RQ_RemoveListener rq) {
		if (!checkOwnership(context, rq.getId()))
			return failure(ACCESS_DENIED);
		var outcome = begin();

		ListenerStore.remove(rq.getId());
		return success(outcome);
	}

	@Auth
	@Handler(tag = MSG.Message.RQ_LISTENER_DELTA_FIELD_NUMBER)
	public static MessageOrBuilder rq_listener_delta(ExeletContext context, RQ_ListenerDelta rq) {
		if (!checkOwnership(context, rq.getId()))
			return failure(ACCESS_DENIED);
		var outcome = begin();

		return complete(outcome, ListenerStore.delta(rq.getId(), rq.getDelta()));
	}

	@Auth
	@Handler(tag = MSG.Message.RQ_CHANGE_LISTENER_FIELD_NUMBER)
	public static MessageOrBuilder rq_change_listener(ExeletContext context, RQ_ChangeListener rq) {
		if (!checkOwnership(context, rq.getId()))
			return failure(ACCESS_DENIED);
		var outcome = begin();

		return complete(outcome, ListenerStore.change(rq.getId(), rq.getState()));
	}

	private static boolean checkOwnership(ExeletContext context, long listenerId) {
		Listener listener = ListenerStore.get(listenerId).orElse(null);
		if (listener == null)
			return false;

		return listener.getOwner().getCvid() == context.connector.getRemoteCvid();
	}

	private ListenerExe() {
	}
}
