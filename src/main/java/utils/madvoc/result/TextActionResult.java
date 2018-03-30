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

import utils.io.StreamUtil;
import utils.madvoc.ActionRequest;
import utils.madvoc.MadvocConfig;
import utils.madvoc.ScopeType;
import utils.madvoc.meta.In;
import utils.madvoc.meta.Scope;
import utils.madvoc.result.ActionResult;
import utils.madvoc.result.TextResult;
import utils.util.StringPool;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * Text result returns a result value, i.e. a string.
 * Useful for JSON responses, when resulting string is built
 * in the action.
 */
public class TextActionResult implements ActionResult<utils.madvoc.result.TextResult> {

	@In @Scope(ScopeType.CONTEXT)
	protected MadvocConfig madvocConfig;

	@Override
	public void render(final ActionRequest actionRequest, final TextResult resultValue) throws Exception {
		final HttpServletResponse response = actionRequest.getHttpServletResponse();

		String encoding = response.getCharacterEncoding();

		if (encoding == null) {
			encoding = madvocConfig.getEncoding();
		}

		response.setContentType(resultValue.contentType());
		response.setCharacterEncoding(encoding);

		String text = resultValue.value();

		if (text == null) {
			text = StringPool.EMPTY;
		}

		final byte[] data = text.getBytes(encoding);
		response.setContentLength(data.length);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(data);
		} finally {
			StreamUtil.close(out);
		}
	}
}