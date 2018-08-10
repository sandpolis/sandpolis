/******************************************************************************
 *                                                                            *
 *                    Copyright 2018 Subterranean Security                    *
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
package com.sandpolis.core.instance.storage;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An ephemeral {@link StoreProvider} that is backed by a {@link HashMap}.
 * 
 * @author cilki
 * @since 5.0.0
 */
public class MemoryMapStoreProvider<E> extends EphemeralStoreProvider<E> implements StoreProvider<E> {

	/**
	 * The backing storage for this {@code StoreProvider}.
	 */
	private final Map<Object, E> map = new HashMap<>();

	public MemoryMapStoreProvider(Class<E> cls) {
		super(cls);
	}

	@Override
	public void add(E e) {
		try {
			map.put(getId.invoke(e), e);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	@Override
	public E get(Object id) {
		return map.get(id);
	}

	@Override
	public E get(String field, Object id) {
		try {
			MethodHandle getField = fieldGetter(field);
			for (E e : map.values())
				if (id.equals(getField.invoke(e)))
					return e;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
		return null;
	}

	@Override
	public Iterator<E> iterator() {
		return map.values().iterator();
	}

	@Override
	public Stream<E> stream() {
		return map.values().stream();
	}

	@Override
	public void remove(E e) {
		map.remove(e);
	}

}