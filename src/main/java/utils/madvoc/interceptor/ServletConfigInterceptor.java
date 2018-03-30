package utils.madvoc.interceptor;

import javax.servlet.http.HttpServletRequest;
import utils.madvoc.ActionRequest;
import utils.madvoc.MadvocConfig;
import utils.madvoc.ScopeType;
import utils.madvoc.component.InjectorsManager;
import utils.madvoc.interceptor.ActionInterceptor;
import utils.madvoc.meta.In;
import utils.madvoc.meta.Scope;
import utils.servlet.ServletUtil;
import utils.servlet.upload.MultipartRequestWrapper;

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