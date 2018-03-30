package utils.madvoc.interceptor;

import jodd.madvoc.ActionRequest;
import jodd.madvoc.ActionWrapper;

public interface ActionInterceptor extends ActionWrapper {
	default Object apply(ActionRequest actionRequest) throws Exception {
		return this.intercept(actionRequest);
	}

	Object intercept(ActionRequest arg0) throws Exception;
}