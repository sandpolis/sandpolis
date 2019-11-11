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
package com.sandpolis.server.vanilla.gen.mega;

import com.google.common.io.BaseEncoding;
import com.sandpolis.core.proto.util.Generator.GenConfig;
import com.sandpolis.server.vanilla.gen.MegaGen;

/**
 * This generator produces a URL with an embedded configuration.
 *
 * @author cilki
 * @since 5.0.0
 */
public class UrlPackager extends MegaGen {
	public UrlPackager(GenConfig config) {
		super(config);
	}

	@Override
	protected byte[] generate() throws Exception {
		String url = "https://sandpolis.com/config?c=";

		switch (config.getPayload()) {
		case OUTPUT_CONFIG:
			switch (config.getPayloadConfigCase()) {
			case MEGA:
				url += BaseEncoding.base64Url().encode(config.getMega().toByteArray());
				break;
			case MICRO:
				url += BaseEncoding.base64Url().encode(config.getMicro().toByteArray());
				break;
			default:
				throw new RuntimeException();
			}
			break;
		}

		return url.getBytes();
	}
}