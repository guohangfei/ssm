package utils.madvoc.interceptor;

import utils.madvoc.ActionRequest;
import utils.madvoc.MadvocException;
import utils.madvoc.interceptor.ActionInterceptor;

public final class DefaultWebAppInterceptors implements ActionInterceptor {
	public String intercept(ActionRequest actionRequest) {
		throw new MadvocException(this.getClass().getSimpleName() + " is just a marker");
	}
}