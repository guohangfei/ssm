package utils.madvoc.interceptor;

import utils.madvoc.ActionRequest;
import utils.madvoc.BaseActionWrapperStack;
import utils.madvoc.interceptor.ActionInterceptor;

public class ActionInterceptorStack extends BaseActionWrapperStack<ActionInterceptor> implements ActionInterceptor {
	public ActionInterceptorStack() {
	}

	public ActionInterceptorStack(Class... interceptorClasses) {
		super(interceptorClasses);
	}

	public void setInterceptors(Class... interceptors) {
		this.wrappers = interceptors;
	}

	public Class<? extends ActionInterceptor>[] getInterceptors() {
		return this.getWrappers();
	}

	public final Object intercept(ActionRequest actionRequest) throws Exception {
		return this.apply(actionRequest);
	}
}