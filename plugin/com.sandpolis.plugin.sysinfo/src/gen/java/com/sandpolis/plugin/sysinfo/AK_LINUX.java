// This file is automatically generated. Do not edit!
package com.sandpolis.plugin.sysinfo;

import com.sandpolis.core.attribute.AttributeGroupKey;
import com.sandpolis.core.attribute.AttributeKey;

public final class AK_LINUX {
  /**
   * TODO.
   */
  public static final AttributeGroupKey LINUX = new AttributeGroupKey(com.sandpolis.core.profile.store.DomainStore.get("com.sandpolis.plugin.sysinfo"), 8, 0);

  /**
   * TODO.
   */
  public static final AttributeKey<String> DISTRIBUTION = AttributeKey.newBuilder(LINUX, 1).build();

  /**
   * TODO.
   */
  public static final AttributeKey<String> SHELL = AttributeKey.newBuilder(LINUX, 2).build();

  /**
   * TODO.
   */
  public static final AttributeKey<String> WINDOW_MANAGER = AttributeKey.newBuilder(LINUX, 3).build();
}
