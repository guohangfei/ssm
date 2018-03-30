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

package utils.madvoc.result;

import jodd.madvoc.meta.RenderWith;
import jodd.madvoc.result.PathResult;
import jodd.madvoc.result.ServletRedirectActionResult;

import java.util.function.Consumer;

/**
 * Redirect result.
 */
@RenderWith(ServletRedirectActionResult.class)
public class Redirect extends PathResult {

	public static Redirect to(final String target) {
		return new Redirect(target);
	}

	public static <T> Redirect to(final Class<T> target, final Consumer<T> consumer) {
		return new Redirect(target, consumer);
	}
	@SuppressWarnings("unchecked")
	public static <T> Redirect to(final T target, final Consumer<T> consumer) {
		return new Redirect((Class<T>) target.getClass(), consumer);
	}

	public static Redirect of(final Redirect result, final String append) {
		return new Redirect("/<" + result.path() + ">" + append);
	}

	// ---------------------------------------------------------------- ctor

	public <T> Redirect(final Class<T> target, final Consumer<T> consumer) {
		super(target, consumer);
	}

	public Redirect(final String path) {
		super(path);
	}
}
