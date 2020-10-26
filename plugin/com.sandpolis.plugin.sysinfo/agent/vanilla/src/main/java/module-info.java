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
module com.sandpolis.plugin.sysinfo.agent.vanilla {
	exports com.sandpolis.plugin.sysinfo.agent.vanilla;

	requires com.sandpolis.core.instance;
	requires com.sandpolis.core.net;
	requires com.sandpolis.plugin.sysinfo;

	provides com.sandpolis.core.instance.plugin.SandpolisPlugin with com.sandpolis.plugin.sysinfo.agent.vanilla.SysinfoPlugin;
}