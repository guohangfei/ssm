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

package utils.madvoc.action;

import utils.madvoc.MadvocConfig;
import utils.madvoc.component.ActionsManager;
import utils.madvoc.component.FiltersManager;
import utils.madvoc.component.InterceptorsManager;
import utils.madvoc.component.ResultsManager;
import utils.madvoc.config.ActionRuntime;
import utils.madvoc.filter.ActionFilter;
import utils.madvoc.interceptor.ActionInterceptor;
import utils.madvoc.meta.In;
import utils.madvoc.meta.Out;
import utils.madvoc.meta.Scope;
import utils.madvoc.result.ActionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static utils.madvoc.ScopeType.CONTEXT;

/**
 * Helper action that returns sorted list of all registered action runtime configurations,
 * action results and interceptors. It can be extended as an Madvoc action or used independently.
 */
public class ListMadvocConfig {

	@In @Scope(CONTEXT)
	protected MadvocConfig madvocConfig;

	@In @Scope(CONTEXT)
	protected ActionsManager actionsManager;

	@In @Scope(CONTEXT)
	protected FiltersManager filtersManager;

	@In @Scope(CONTEXT)
	protected InterceptorsManager interceptorsManager;

	@In @Scope(CONTEXT)
	protected ResultsManager resultsManager;

	@Out
	protected List<ActionRuntime> actions;

	@Out
	protected List<ActionResult> results;

	@Out
	protected List<ActionInterceptor> interceptors;

	@Out
	protected List<ActionFilter> filters;

	/**
	 * Collects all interceptors.
	 */
	protected void collectActionInterceptors() {
		final Collection<? extends ActionInterceptor> interceptorValues = interceptorsManager.getAllInterceptors();
		interceptors = new ArrayList<>();
		interceptors.addAll(interceptorValues);
		interceptors.sort(Comparator.comparing(a -> a.getClass().getSimpleName()));
	}

	/**
	 * Collects all filters.
	 */
	protected void collectActionFilters() {
		final Collection<? extends ActionFilter> filterValues = filtersManager.getAllFilters();
		filters = new ArrayList<>();
		filters.addAll(filterValues);
		filters.sort(Comparator.comparing(a -> a.getClass().getSimpleName()));
	}

	/**
	 * Collects all action results.
	 */
	protected void collectActionResults() {
		final Collection<ActionResult> resultsValues = resultsManager.getAllActionResults();
		results = new ArrayList<>();
		results.addAll(resultsValues);
		results.sort(Comparator.comparing(a -> a.getClass().getSimpleName()));
	}

	/**
	 * Collects all action runtime configurations.
	 */
	protected void collectActionRuntimes() {
		actions = actionsManager.getAllActionRuntimes();
		actions.sort(Comparator.comparing(ActionRuntime::getActionPath));
	}

}