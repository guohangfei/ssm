package utils.madvoc.interceptor;

import jodd.madvoc.ActionRequest;
import jodd.madvoc.MadvocException;
import jodd.madvoc.interceptor.ActionInterceptor;

public final class DefaultWebAppInterceptors implements ActionInterceptor {
	public String intercept(ActionRequest actionRequest) {
		throw new MadvocException(this.getClass().getSimpleName() + " is just a marker");
	}
}