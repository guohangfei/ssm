// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package utils.madvoc;

import jodd.madvoc.ActionRequest;
import jodd.madvoc.ActionWrapper;
import jodd.madvoc.MadvocException;

/**
 * Base action wrapper stack.
 */
public abstract class BaseActionWrapperStack<T extends jodd.madvoc.ActionWrapper> implements ActionWrapper {

	protected Class<? extends T>[] wrappers;

	/**
	 * Constructs an empty wrapper stack that will be configured later,
	 * using setter.
	 */
	protected BaseActionWrapperStack() {
	}

	/**
	 * Constructs an wrapper stack with the given wrappers.
	 */
	protected BaseActionWrapperStack(final Class<? extends T>... wrapperClasses) {
		this.wrappers = wrapperClasses;
	}

	/**
	 * Returns an array of wrappers.
	 */
	public Class<? extends T>[] getWrappers() {
		return wrappers;
	}

	/**
	 * Throws an exception, as stack can not be invoked.
	 */
	@Override
	public Object apply(final ActionRequest actionRequest) {
		throw new MadvocException("Wrapper stack can not be invoked");
	}

}