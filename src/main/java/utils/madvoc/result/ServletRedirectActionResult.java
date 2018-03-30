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

import utils.bean.BeanTemplateParser;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.component.ResultMapper;
import utils.madvoc.meta.In;
import utils.madvoc.meta.Scope;
import utils.madvoc.result.ActionResult;
import utils.madvoc.result.Redirect;
import utils.madvoc.result.ServletDispatcherActionResult;
import utils.madvoc.result.ServletPermanentRedirectActionResult;
import utils.servlet.DispatcherUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simply redirects to a page using <code>RequestDispatcher</code>.
 * 
 * @see ServletDispatcherActionResult
 * @see ServletPermanentRedirectActionResult
 */
public class ServletRedirectActionResult implements ActionResult<utils.madvoc.result.Redirect> {

	protected final BeanTemplateParser beanTemplateParser = new BeanTemplateParser();

	public ServletRedirectActionResult() {
		beanTemplateParser.setMacroPrefix(null);
		beanTemplateParser.setMacroStart("{");
		beanTemplateParser.setMacroEnd("}");
	}

	@In @Scope(ScopeType.CONTEXT)
	protected ResultMapper resultMapper;

	/**
	 * Redirects to the given location. Provided path is parsed, action is used as a value context.
	 */
	@Override
	public void render(final ActionRequest actionRequest, final Redirect redirectResult) throws Exception {
		String resultBasePath = actionRequest.getActionRuntime().getResultBasePath();

		String resultPath;
		final String resultValue = redirectResult.path();

		if (resultValue.startsWith("http://") || resultValue.startsWith("https://")) {
			resultPath = resultValue;
		}
		else {
			resultPath = resultMapper.resolveResultPathString(resultBasePath, resultValue);
		}

		HttpServletRequest request = actionRequest.getHttpServletRequest();
		HttpServletResponse response = actionRequest.getHttpServletResponse();

		String path = resultPath;
		path = beanTemplateParser.parseWithBean(path, actionRequest.getAction());

		redirect(request, response, path);
	}

	protected void redirect(final HttpServletRequest request, final HttpServletResponse response, final String path) throws IOException {
		DispatcherUtil.redirect(request, response, path);
	}

}