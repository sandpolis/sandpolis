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
package com.sandpolis.plugin.sysinfo;

import com.sandpolis.core.attribute.AttributeGroupKey;
import com.sandpolis.core.attribute.AttributeKey;

public final class AK_WINDOWS {
  /**
   * TODO.
   */
  public static final AttributeGroupKey WINDOWS = new AttributeGroupKey(com.sandpolis.core.profile.store.DomainStore.get("com.sandpolis.plugin.sysinfo"), 9, 0);

  /**
   * TODO.
   */
  public static final AttributeKey<String> IE_VERSION = AttributeKey.newBuilder(WINDOWS, 1).setDotPath("windows.ie_version").build();

  /**
   * TODO.
   */
  public static final AttributeKey<Long> INSTALL_TIMESTAMP = AttributeKey.newBuilder(WINDOWS, 2).setDotPath("windows.install_timestamp").build();

  /**
   * TODO.
   */
  public static final AttributeKey<String> POWERSHELL_VERSION = AttributeKey.newBuilder(WINDOWS, 3).setDotPath("windows.powershell_version").build();

  /**
   * TODO.
   */
  public static final AttributeKey<String> SERIAL = AttributeKey.newBuilder(WINDOWS, 4).setDotPath("windows.serial").build();
}
