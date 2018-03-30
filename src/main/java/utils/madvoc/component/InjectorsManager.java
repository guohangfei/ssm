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

package utils.madvoc.component;

import utils.madvoc.MadvocConfig;
import utils.madvoc.injector.ActionPathMacroInjector;
import utils.madvoc.injector.ApplicationScopeInjector;
import utils.madvoc.injector.CookieScopeInjector;
import utils.madvoc.injector.MadvocContextScopeInjector;
import utils.madvoc.injector.MadvocParamsInjector;
import utils.madvoc.injector.RequestBodyScopeInject;
import utils.madvoc.injector.RequestScopeInjector;
import utils.madvoc.injector.ServletContextScopeInjector;
import utils.madvoc.injector.SessionScopeInjector;
import utils.petite.PetiteContainer;
import utils.petite.meta.PetiteInitMethod;
import utils.petite.meta.PetiteInject;

import static utils.petite.meta.InitMethodInvocationStrategy.POST_DEFINE;

/**
 * Injectors manager creates and holds instances of all injectors.
 */
public class InjectorsManager {

	@PetiteInject
	protected PetiteContainer madpc;

	@PetiteInject
	protected MadvocConfig madvocConfig;

	protected RequestScopeInjector requestScopeInjector;
	protected SessionScopeInjector sessionScopeInjector;
	protected ActionPathMacroInjector actionPathMacroInjector;
	protected MadvocContextScopeInjector madvocContextScopeInjector;
	protected MadvocParamsInjector madvocParamsInjector;
	protected ApplicationScopeInjector applicationScopeInjector;
	protected ServletContextScopeInjector servletContextScopeInjector;
	protected CookieScopeInjector cookieInjector;
	protected RequestBodyScopeInject requestBodyScopeInject;

	@PetiteInitMethod(order = 1, invoke = POST_DEFINE)
	void createInjectors() {
		requestScopeInjector = new RequestScopeInjector(madvocConfig);
		sessionScopeInjector = new SessionScopeInjector();
		actionPathMacroInjector = new ActionPathMacroInjector();
		madvocContextScopeInjector = new MadvocContextScopeInjector(madpc);
		madvocParamsInjector = new MadvocParamsInjector(madvocConfig);
		applicationScopeInjector = new ApplicationScopeInjector();
		servletContextScopeInjector = new ServletContextScopeInjector();
		cookieInjector = new CookieScopeInjector();
		requestBodyScopeInject = new RequestBodyScopeInject();
	}

	// ---------------------------------------------------------------- getter

	public RequestScopeInjector requestScopeInjector() {
		return requestScopeInjector;
	}

	public SessionScopeInjector sessionScopeInjector() {
		return sessionScopeInjector;
	}

	public ActionPathMacroInjector actionPathMacroInjector() {
		return actionPathMacroInjector;
	}

	public MadvocContextScopeInjector madvocContextScopeInjector() {
		return madvocContextScopeInjector;
	}

	public MadvocParamsInjector madvocParamsInjector() {
		return madvocParamsInjector;
	}

	public ApplicationScopeInjector applicationScopeInjector() {
		return applicationScopeInjector;
	}

	public ServletContextScopeInjector servletContextScopeInjector() {
		return servletContextScopeInjector;
	}

	public CookieScopeInjector cookieInjector() {
		return cookieInjector;
	}

	public RequestBodyScopeInject requestBodyScopeInject() {
		return requestBodyScopeInject;
	}
}