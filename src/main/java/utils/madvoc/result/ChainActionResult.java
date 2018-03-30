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

import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.component.ResultMapper;
import utils.madvoc.meta.In;
import utils.madvoc.meta.Scope;
import utils.madvoc.result.ActionResult;
import utils.madvoc.result.Chain;

/**
 * Process chain results. Chaining is very similar to forwarding, except it is done
 * by {@link utils.madvoc.MadvocServletFilter} and not by container. Chaining to next action request
 * happens after the complete execution of current one: after all interceptors and this result has been
 * finished.
 */
public class ChainActionResult implements ActionResult<utils.madvoc.result.Chain> {

	@In @Scope(ScopeType.CONTEXT)
	protected ResultMapper resultMapper;

	/**
	 * Sets the {@link ActionRequest#setNextActionPath(String) next action request} for the chain.
	 */
	@Override
	public void render(final ActionRequest actionRequest, final Chain chainResult) {
		String resultBasePath = actionRequest.getActionRuntime().getResultBasePath();

		String resultPath = resultMapper.resolveResultPathString(resultBasePath, chainResult.path());

		actionRequest.setNextActionPath(resultPath);
	}

}