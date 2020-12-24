//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.instance.state.vst;

import com.sandpolis.core.foundation.Result.ErrorCode;

/**
 * A {@link VirtObject} is a member of the virtual state tree (VST).
 *
 * <p>
 * The VST mirrors the real state tree (ST) and provides a more useful
 * domain-specific API. Objects in the VST are created and destroyed
 * frivolously, and are always backed by a corresponding object in the real ST.
 *
 * <p>
 * Objects in the VST can be thought of as a convenient "view" for objects in
 * the real ST.
 *
 * @since 7.0.0
 */
public interface VirtObject {

	/**
	 * An {@link IncompleteObjectException} is thrown when a {@link VirtObject} is
	 * not {@link #complete} when expected to be.
	 */
	public static class IncompleteObjectException extends RuntimeException {
		private static final long serialVersionUID = -6332437282463564387L;
	}

	/**
	 * A {@link VirtObject} is complete if all required fields are present.
	 *
	 * @param config The candidate configuration
	 * @return An error code or {@link ErrorCode#OK}
	 */
	public default ErrorCode complete() {
		return ErrorCode.OK;
	}

	/**
	 * A {@link VirtObject} is valid if all present attributes pass value
	 * restrictions.
	 *
	 * @return An error code or {@link ErrorCode#OK}
	 */
	public default ErrorCode valid() {
		return ErrorCode.OK;
	}
}
