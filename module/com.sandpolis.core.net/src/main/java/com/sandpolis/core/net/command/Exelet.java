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
package com.sandpolis.core.net.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sandpolis.core.net.sock.Sock;

/**
 * An {@link Exelet} handles incoming messages from a {@link Sock}.
 *
 * @author cilki
 * @since 5.0.0
 */
public abstract class Exelet {

	/**
	 * Defines the message type that the target {@link Exelet} method handles.
	 *
	 * @author cilki
	 * @since 5.1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Handler {

		/**
		 * The message tag of the message that is handled.
		 *
		 * @return The message tag
		 */
		public int tag();
	}

	/**
	 * When applied to an {@link Exelet} method, the method will be executable on
	 * authenticated connections only.
	 *
	 * @author cilki
	 * @since 5.0.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Auth {
	}

	/**
	 * When applied to an {@link Exelet} method, the method will be executable on
	 * unauthenticated connections only.
	 *
	 * @author cilki
	 * @since 5.0.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Unauth {
	}

	/**
	 * When applied to an {@link Exelet} method, the method will be executable on
	 * connections which have the necessary permission.
	 *
	 * @author cilki
	 * @since 5.0.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Permission {
		short permission();
	}
}
