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

import utils.json.JsonSerializer;
import utils.madvoc.meta.RenderWith;
import utils.madvoc.result.JsonActionResult;
import utils.util.StringPool;
import utils.util.net.HttpStatus;

/**
 * General JSON result.
 */
@RenderWith(JsonActionResult.class)
public class JsonResult {

	private final String body;
	private int status = 200;
	private String message = "OK";

	/**
	 * Creates new JSON result of provided JSON string.
	 */
	public static JsonResult of(final String json) {
		return new JsonResult(json);
	}

	/**
	 * Creates JSON result from given object. The object will be serialized to JSON.
	 */
	public static JsonResult of(final Object object) {
		String json = JsonSerializer.create().deep(true).serialize(object);
		return new JsonResult(json);
	}

	/**
	 * Creates a JSON result with empty body and given status.
	 */
	public static JsonResult of(final HttpStatus httpStatus) {
		return empty().status(httpStatus);
	}

	/**
	 * Creates a JSON result with empty body.
	 */
	public static JsonResult empty() {
		return new JsonResult(StringPool.EMPTY);
	}

	public JsonResult(final String body) {
		this.body = body;
	}

	public JsonResult status(final int status) {
		this.status = status;
		this.message = message;
		return this;
	}

	public JsonResult status(final HttpStatus httpStatus) {
		this.status = httpStatus.status();
		this.message = httpStatus.message();
		return this;
	}

	public JsonResult status(final int status, final String message) {
		this.status = status;
		this.message = message;
		return this;
	}

	/**
	 * Returns JSON body.
	 */
	public String value() {
		return body;
	}

	/**
	 * Returns response status.
	 */
	public int status() {
		return status;
	}

	/**
	 * Returns response message.
	 */
	public String message() {
		return message;
	}
}