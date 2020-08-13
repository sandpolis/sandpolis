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
package com.sandpolis.core.instance.store;

import com.sandpolis.core.instance.store.provider.StoreProvider;
import com.sandpolis.core.instance.store.provider.StoreProviderFactory;

/**
 * {@link StoreConfig} is a base type for store configurations.
 */
public abstract class StoreConfig {

	/**
	 * Indicate that the store's data should not survive the closing of the store.
	 */
	public void ephemeral() {
		throw new UnsupportedOperationException(getClass().getName() + " does not support ephemeral providers");
	}

	/**
	 * Indicate that the store's data should be persisted with a provider obtained
	 * from the given factory.
	 *
	 * @param factory The provider factory that will produce a {@link StoreProvider}
	 */
	public void persistent(StoreProviderFactory factory) {
		throw new UnsupportedOperationException(getClass().getName() + " does not support persistent providers");
	}
}
