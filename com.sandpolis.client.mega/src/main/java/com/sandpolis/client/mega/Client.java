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
package com.sandpolis.client.mega;

import static com.sandpolis.core.instance.Environment.EnvPath.LIB;
import static com.sandpolis.core.instance.Environment.EnvPath.LOG;
import static com.sandpolis.core.instance.Environment.EnvPath.TMP;
import static com.sandpolis.core.instance.MainDispatch.register;
import static com.sandpolis.core.instance.store.plugin.PluginStore.PluginStore;
import static com.sandpolis.core.instance.store.thread.ThreadStore.ThreadStore;
import static com.sandpolis.core.net.store.connection.ConnectionStore.ConnectionStore;
import static com.sandpolis.core.net.store.network.NetworkStore.NetworkStore;
import static com.sandpolis.core.util.ArtifactUtil.ParsedCoordinate.fromCoordinate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.sandpolis.client.mega.cmd.AuthCmd;
import com.sandpolis.client.mega.cmd.PluginCmd;
import com.sandpolis.core.instance.BasicTasks;
import com.sandpolis.core.instance.Config;
import com.sandpolis.core.instance.ConfigConstant.plugin;
import com.sandpolis.core.instance.Core;
import com.sandpolis.core.instance.Environment;
import com.sandpolis.core.instance.MainDispatch;
import com.sandpolis.core.instance.MainDispatch.InitializationTask;
import com.sandpolis.core.instance.MainDispatch.Task;
import com.sandpolis.core.instance.PoolConstant.net;
import com.sandpolis.core.ipc.task.IPCTask;
import com.sandpolis.core.net.future.ResponseFuture;
import com.sandpolis.core.net.store.network.NetworkStoreEvents.ServerEstablishedEvent;
import com.sandpolis.core.net.store.network.NetworkStoreEvents.ServerLostEvent;
import com.sandpolis.core.proto.util.Auth.KeyContainer;
import com.sandpolis.core.proto.util.Generator.MegaConfig;
import com.sandpolis.core.proto.util.Result.Outcome;
import com.sandpolis.core.soi.Dependency.SO_DependencyMatrix.Artifact;
import com.sandpolis.core.util.AsciiUtil;
import com.sandpolis.core.util.CryptoUtil.SAND5.ReciprocalKeyPair;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

/**
 * The entry point for Client instances. This class is responsible for
 * initializing the new instance.
 *
 * @author cilki
 * @since 1.0.0
 */
public final class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	/**
	 * The configuration included in the instance jar.
	 */
	public static final MegaConfig SO_CONFIG;

	static {
		try {
			SO_CONFIG = MegaConfig.parseFrom(Client.class.getResourceAsStream("/soi/client.bin"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read SO_CONFIG!", e);
		}
	}

	public static void main(String[] args) {
		log.info("Launching {} ({})", AsciiUtil.toRainbow("Sandpolis Client"), Core.SO_BUILD.getVersion());
		log.debug("Built on {} with {} (Build: {})", new Date(Core.SO_BUILD.getTime()), Core.SO_BUILD.getPlatform(),
				Core.SO_BUILD.getNumber());

		register(BasicTasks.loadConfiguration);
		register(IPCTask.load);
		register(IPCTask.checkLock);
		register(IPCTask.setLock);
//		register(Client.install);
		register(Client.loadEnvironment);
		register(Client.loadStores);
		register(Client.loadPlugins);
		register(Client.beginConnectionRoutine);
	}

	/**
	 * Install the client if necessary.
	 */
	@InitializationTask(name = "Install client", fatal = true)
	public static final Task install = new Task((task) -> {

		if (Environment.JAR == null)
			return task.skipped();

		// Installation mode
		Path base = Paths.get(SO_CONFIG.getExecution().getInstallPathOrDefault(0, "."));// TODO 0
		Path lib = base.resolve("lib");

		Files.createDirectories(base);
		Files.createDirectories(lib);

		Files.copy(Client.class.getResourceAsStream("/main/main.jar"), base.resolve("client.jar"));

		for (Artifact artifact : Core.SO_MATRIX.getArtifactList()) {
			String name = fromCoordinate(artifact.getCoordinates()).filename;

			Files.copy(Client.class.getResourceAsStream("/lib/" + name), lib.resolve(name));
		}

		System.exit(0);
		return task.success();
	});

	/**
	 * Load the runtime environment.
	 */
	@InitializationTask(name = "Load runtime environment", fatal = true)
	public static final Task loadEnvironment = new Task((task) -> {

		if (!Environment.load(TMP, LOG, LIB))
			Environment.setup();

		return task.success();
	});

	/**
	 * Load static stores.
	 */
	@InitializationTask(name = "Load static stores", fatal = true)
	public static final Task loadStores = new Task((task) -> {

		ThreadStore.init(config -> {
			config.ephemeral();
			config.defaults.put(net.exelet, new NioEventLoopGroup(2).next());
			config.defaults.put(net.connection.outgoing, new NioEventLoopGroup(2).next());
			config.defaults.put("temploop", new NioEventLoopGroup(2).next());
			config.defaults.put(net.message.incoming, new UnorderedThreadPoolEventExecutor(2));
			config.defaults.put("store.event_bus", Executors.newSingleThreadExecutor());
		});

		ConnectionStore.init(config -> {
			config.ephemeral();
		});

		NetworkStore.init(config -> {
			config.ephemeral();
		});

		PluginStore.init(config -> {
			config.ephemeral();
		});

		// Register the subscribers in this class by building a temporary instance
		var client = new Client();
		NetworkStore.register(client);

		return task.success();
	});

	/**
	 * Load plugins.
	 */
	@InitializationTask(name = "Load client plugins", condition = plugin.enabled)
	public static final Task loadPlugins = new Task((task) -> {
		PluginStore.scanPluginDirectory();
		PluginStore.loadPlugins();

		return task.success();
	});

	/**
	 * Begin the connection routine.
	 */
	@InitializationTask(name = "Begin the connection routine", fatal = true)
	public static final Task beginConnectionRoutine = new Task((task) -> {
		ConnectionStore.connect(SO_CONFIG.getNetwork().getLoopConfig());

		return task.success();
	});

	@Subscribe
	private void onSrvLost(ServerLostEvent event) {
		ConnectionStore.connect(SO_CONFIG.getNetwork().getLoopConfig());
	}

	@Subscribe
	private void onSrvEstablished(ServerEstablishedEvent event) {
		ResponseFuture<Outcome> future;
		var auth = SO_CONFIG.getAuthentication();

		switch (auth.getAuthOneofCase()) {
		case KEY:
			KeyContainer mech = auth.getKey();
			ReciprocalKeyPair key = new ReciprocalKeyPair(mech.getClient().getVerifier().toByteArray(),
					mech.getClient().getSigner().toByteArray());
			future = AuthCmd.async().key(auth.getGroupName(), mech.getId(), key);
			break;
		case PASSWORD:
			future = AuthCmd.async().password(auth.getPassword().getPassword());
			break;
		default:
			future = AuthCmd.async().none();
			break;
		}

		if (Config.getBoolean(plugin.enabled)) {
			future.addHandler((Outcome rs) -> {
				// Synchronize plugins
				PluginCmd.async().sync().sync();
				PluginStore.loadPlugins();
			});
		}
	}

	private Client() {
	}

	static {
		MainDispatch.register(Client.class);
	}
}
