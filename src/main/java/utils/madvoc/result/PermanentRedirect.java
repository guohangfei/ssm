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
import jodd.madvoc.result.ServletPermanentRedirectActionResult;

import java.util.function.Consumer;

/**
 * Permanent redirection.
 */
@RenderWith(ServletPermanentRedirectActionResult.class)
public class PermanentRedirect extends PathResult {

	public static PermanentRedirect to(final String target) {
		return new PermanentRedirect(target);
	}

	public static <T> PermanentRedirect to(final Class<T> target, final Consumer<T> consumer) {
		return new PermanentRedirect(target, consumer);
	}

	// ---------------------------------------------------------------- ctor

	public <T> PermanentRedirect(final Class<T> target, final Consumer<T> consumer) {
		super(target, consumer);
	}

	public PermanentRedirect(final String path) {
		super(path);
	}
}
