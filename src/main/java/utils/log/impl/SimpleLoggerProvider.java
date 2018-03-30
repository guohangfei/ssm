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

package utils.log.impl;

import utils.log.Logger;
import utils.log.LoggerProvider;
import utils.log.impl.SimpleLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Provider for {@link utils.log.impl.SimpleLogger} adapter.
 */
public class SimpleLoggerProvider implements LoggerProvider<utils.log.impl.SimpleLogger> {

	private final long startTime;
	private static Map<String, utils.log.impl.SimpleLogger> loggers = new HashMap<>();
	private Function<String, utils.log.impl.SimpleLogger> simpleLoggerFunction =
		n -> new utils.log.impl.SimpleLogger(this, n, Logger.Level.DEBUG);

	protected SimpleLoggerProvider() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Returns elapsed time in milliseconds.
	 */
	public long getElapsedTime() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimpleLogger createLogger(final String name) {
		return loggers.computeIfAbsent(name, simpleLoggerFunction);
	}

}