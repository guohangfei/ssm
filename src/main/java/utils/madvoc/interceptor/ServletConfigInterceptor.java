package utils.madvoc.interceptor;

import javax.servlet.http.HttpServletRequest;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.MadvocConfig;
import jodd.madvoc.ScopeType;
import jodd.madvoc.component.InjectorsManager;
import jodd.madvoc.interceptor.ActionInterceptor;
import jodd.madvoc.meta.In;
import jodd.madvoc.meta.Scope;
import jodd.servlet.ServletUtil;
import jodd.servlet.upload.MultipartRequestWrapper;

public class ServletConfigInterceptor implements ActionInterceptor {
	@In
	@Scope(ScopeType.CONTEXT)
	protected MadvocConfig madvocConfig;
	@In
	@Scope(ScopeType.CONTEXT)
	protected InjectorsManager injectorsManager;

	public Object intercept(ActionRequest actionRequest) throws Exception {
		HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
		if (ServletUtil.isMultipartRequest(servletRequest)) {
			MultipartRequestWrapper servletRequest1 = new MultipartRequestWrapper(servletRequest,
					this.madvocConfig.getFileUploadFactory(), this.madvocConfig.getEncoding());
			actionRequest.bind(servletRequest1);
		}

		this.inject(actionRequest);
		Object result = actionRequest.invoke();
		this.outject(actionRequest);
		return result;
	}

	protected void inject(ActionRequest actionRequest) {
		this.injectorsManager.madvocContextScopeInjector().inject(actionRequest);
		this.injectorsManager.servletContextScopeInjector().inject(actionRequest);
		this.injectorsManager.applicationScopeInjector().inject(actionRequest);
		this.injectorsManager.sessionScopeInjector().inject(actionRequest);
		this.injectorsManager.requestScopeInjector().inject(actionRequest);
		this.injectorsManager.actionPathMacroInjector().inject(actionRequest);
		this.injectorsManager.cookieInjector().inject(actionRequest);
		this.injectorsManager.requestBodyScopeInject().inject(actionRequest);
	}

	protected void outject(ActionRequest actionRequest) {
		this.injectorsManager.cookieInjector().outject(actionRequest);
		this.injectorsManager.applicationScopeInjector().outject(actionRequest);
		this.injectorsManager.sessionScopeInjector().outject(actionRequest);
		this.injectorsManager.requestScopeInjector().outject(actionRequest);
	}
}