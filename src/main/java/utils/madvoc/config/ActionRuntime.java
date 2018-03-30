package utils.madvoc.config;

import java.lang.reflect.Method;
import jodd.madvoc.ActionConfig;
import jodd.madvoc.ActionHandler;
import jodd.madvoc.config.ActionDefinition;
import jodd.madvoc.config.MethodParam;
import jodd.madvoc.config.RouteChunk;
import jodd.madvoc.config.ScopeData;
import jodd.madvoc.filter.ActionFilter;
import jodd.madvoc.interceptor.ActionInterceptor;
import jodd.madvoc.result.ActionResult;

public class ActionRuntime {
	private final ActionHandler actionHandler;
	private final Class actionClass;
	private final Method actionClassMethod;
	private final Class<? extends ActionResult> actionResult;
	private final String actionPath;
	private final String actionMethod;
	private final String resultBasePath;
	private final boolean async;
	private final ScopeData[][] scopeData;
	private final MethodParam[] methodParams;
	private final boolean hasArguments;
	private RouteChunk routeChunk;
	private final ActionFilter[] filters;
	private final ActionInterceptor[] interceptors;
	private final ActionConfig actionConfig;

	public ActionRuntime(ActionHandler actionHandler, Class actionClass, Method actionClassMethod,
			ActionFilter[] filters, ActionInterceptor[] interceptors, ActionDefinition actionDefinition,
			Class<? extends ActionResult> actionResult, boolean async, ScopeData[][] scopeData,
			MethodParam[] methodParams, ActionConfig actionConfig) {
		this.actionHandler = actionHandler;
		this.actionClass = actionClass;
		this.actionClassMethod = actionClassMethod;
		this.actionPath = actionDefinition.actionPath();
		this.actionMethod = actionDefinition.actionMethod() == null
				? null
				: actionDefinition.actionMethod().toUpperCase();
		this.resultBasePath = actionDefinition.resultBasePath();
		this.hasArguments = actionClassMethod != null && actionClassMethod.getParameterTypes().length != 0;
		this.actionResult = actionResult;
		this.async = async;
		this.scopeData = scopeData;
		this.filters = filters;
		this.interceptors = interceptors;
		this.methodParams = methodParams;
		this.actionConfig = actionConfig;
	}

	public boolean isActionHandlerDefined() {
		return this.actionHandler != null;
	}

	public ActionHandler getActionHandler() {
		return this.actionHandler;
	}

	public Class getActionClass() {
		return this.actionClass;
	}

	public Method getActionClassMethod() {
		return this.actionClassMethod;
	}

	public String getActionPath() {
		return this.actionPath;
	}

	public String getActionMethod() {
		return this.actionMethod;
	}

	public String getResultBasePath() {
		return this.resultBasePath;
	}

	public ActionInterceptor[] getInterceptors() {
		return this.interceptors;
	}

	public ActionFilter[] getFilters() {
		return this.filters;
	}

	public boolean isAsync() {
		return this.async;
	}

	public MethodParam[] getMethodParams() {
		return this.methodParams;
	}

	public Class<? extends ActionResult> getActionResult() {
		return this.actionResult;
	}

	public boolean hasArguments() {
		return this.hasArguments;
	}

	public ScopeData[][] getScopeData() {
		return this.scopeData;
	}

	public ActionConfig getActionConfig() {
		return this.actionConfig;
	}

	public void bind(RouteChunk routeChunk) {
		this.routeChunk = routeChunk;
	}

	public RouteChunk getRouteChunk() {
		return this.routeChunk;
	}

	public String createActionString() {
		if (this.actionHandler != null) {
			return this.actionHandler.getClass().getName();
		} else {
			String className = this.actionClass.getName();
			int ndx = className.indexOf("$$");
			if (ndx != -1) {
				className = className.substring(0, ndx);
			}

			return className + '#' + this.actionClassMethod.getName();
		}
	}

	public String toString() {
		return "action: " + this.actionPath + (this.actionMethod == null ? "" : '#' + this.actionMethod) + "  -->  "
				+ this.createActionString();
	}
}