package utils.madvoc.injector;

import java.util.function.BiConsumer;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.ScopeType;
import jodd.madvoc.injector.Injector;
import jodd.madvoc.injector.Outjector;
import jodd.madvoc.injector.Targets;
import jodd.servlet.ServletUtil;
import jodd.util.StringUtil;

public class CookieScopeInjector implements Injector, Outjector {
	private static final ScopeType SCOPE_TYPE;

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
				Object value = null;
				if (in.type == Cookie.class) {
					String cookieName = StringUtil.uncapitalize(in.name);
					value = ServletUtil.getCookie(servletRequest, cookieName);
				} else if (in.type.isArray() && in.type.getComponentType().equals(Cookie.class)) {
					if (StringUtil.isEmpty(in.name)) {
						value = servletRequest.getCookies();
					} else {
						value = ServletUtil.getAllCookies(servletRequest, in.name);
					}
				}

				if (value != null) {
					target.writeValue(in.propertyName(), value, true);
				}

			});
		}
	}

	public void outject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletResponse servletResponse = actionRequest.getHttpServletResponse();
			targets.forEachTargetAndOutScopes(SCOPE_TYPE, (target, out) -> {
				Cookie cookie = (Cookie) target.readTargetProperty(out);
				if (cookie != null) {
					servletResponse.addCookie(cookie);
				}

			});
		}
	}

	static {
		SCOPE_TYPE = ScopeType.COOKIE;
	}
}