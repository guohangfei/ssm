// Copyright (c) 2003-present, utils Team (http://utils.org)
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

package utils.madvoc.result;

import utils.madvoc.meta.RenderWith;
import utils.madvoc.result.PathResult;
import utils.madvoc.result.ServletDispatcherActionResult;

import java.util.function.Consumer;

/**
 * Servlet dispatched result.
 */
@RenderWith(ServletDispatcherActionResult.class)
public class Forward extends PathResult {

	public static Forward to(final String target) {
		return new Forward(target);
	}

	public static <T> Forward to(final Class<T> target, final Consumer<T> consumer) {
		return new Forward(target, consumer);
	}
	@SuppressWarnings("unchecked")
	public static <T> Forward to(final T target, final Consumer<T> consumer) {
		return new Forward((Class<T>) target.getClass(), consumer);
	}

	public static Forward of(final Forward result, final String append) {
		return new Forward("/<" + result.path() + ">.." + append);
	}

	// ---------------------------------------------------------------- ctor

	public <T> Forward(final Class<T> target, final Consumer<T> consumer) {
		super(target, consumer);
	}

	public Forward(final String path) {
		super(path);
	}
}
