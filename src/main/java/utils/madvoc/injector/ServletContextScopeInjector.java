package utils.madvoc.injector;

import java.io.IOException;
import java.util.function.BiConsumer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.bean.BeanUtil;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.injector.ContextInjector;
import utils.madvoc.injector.Injector;
import utils.madvoc.injector.Targets;
import utils.servlet.CsrfShield;
import utils.servlet.ServletUtil;
import utils.servlet.map.HttpServletContextMap;
import utils.servlet.map.HttpServletRequestMap;
import utils.servlet.map.HttpServletRequestParamMap;
import utils.servlet.map.HttpSessionMap;
import utils.util.StringUtil;

public class ServletContextScopeInjector implements Injector, ContextInjector<ServletContext> {
	private static final ScopeType SCOPE_TYPE;
	public static final String REQUEST_NAME = "request";
	public static final String SESSION_NAME = "session";
	public static final String CONTEXT_NAME = "context";
	public static final String REQUEST_MAP = "requestMap";
	public static final String REQUEST_PARAM_MAP = "requestParamMap";
	public static final String REQUEST_BODY = "requestBody";
	public static final String SESSION_MAP = "sessionMap";
	public static final String CONTEXT_MAP = "contextMap";
	public static final String CSRF_NAME = "csrfTokenValid";

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			HttpServletResponse servletResponse = actionRequest.getHttpServletResponse();
			targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
				Class fieldType = in.type;
				Object value = null;
				if (fieldType.equals(HttpServletRequest.class)) {
					value = servletRequest;
				} else if (fieldType.equals(ServletRequest.class)) {
					value = servletRequest;
				} else if (fieldType.equals(HttpServletResponse.class)) {
					value = servletResponse;
				} else if (fieldType.equals(ServletResponse.class)) {
					value = servletResponse;
				} else if (fieldType.equals(HttpSession.class)) {
					value = servletRequest.getSession();
				} else if (fieldType.equals(ServletContext.class)) {
					value = servletRequest.getSession().getServletContext();
				} else if (in.name.equals("requestMap")) {
					value = new HttpServletRequestMap(servletRequest);
				} else if (in.name.equals("requestParamMap")) {
					value = new HttpServletRequestParamMap(servletRequest);
				} else if (in.name.equals("requestBody")) {
					try {
						value = ServletUtil.readRequestBodyFromStream(servletRequest);
					} catch (IOException arg6) {
						value = arg6.toString();
					}
				} else if (in.name.equals("requestBody")) {
					value = new HttpServletRequestParamMap(servletRequest);
				} else if (in.name.equals("sessionMap")) {
					value = new HttpSessionMap(servletRequest);
				} else if (in.name.equals("contextMap")) {
					value = new HttpServletContextMap(servletRequest);
				} else {
					String name;
					if (in.name.startsWith("request")) {
						name = StringUtil.uncapitalize(in.name.substring("request".length()));
						if (!name.isEmpty()) {
							value = BeanUtil.declared.getProperty(servletRequest, name);
						}
					} else if (in.name.startsWith("session")) {
						name = StringUtil.uncapitalize(in.name.substring("session".length()));
						if (!name.isEmpty()) {
							value = BeanUtil.declared.getProperty(servletRequest.getSession(), name);
						}
					} else if (in.name.startsWith("context")) {
						name = StringUtil.uncapitalize(in.name.substring("context".length()));
						if (!name.isEmpty()) {
							value = BeanUtil.declared.getProperty(servletRequest.getSession().getServletContext(),
									name);
						}
					} else if (in.name.equals("csrfTokenValid")) {
						value = Boolean.valueOf(CsrfShield.checkCsrfToken(servletRequest));
					}
				}

				if (value != null) {
					target.writeValue(in.propertyName(), value, true);
				}

			});
		}
	}

	public void injectContext(Targets targets, ServletContext servletContext) {
		targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
			Class fieldType = in.type;
			Object value = null;
			if (fieldType.equals(ServletContext.class)) {
				value = servletContext;
			} else if (in.name.equals("contextMap")) {
				value = new HttpServletContextMap(servletContext);
			} else if (in.name.startsWith("context")) {
				value = BeanUtil.declared.getProperty(servletContext,
						StringUtil.uncapitalize(in.name.substring("context".length())));
			}

			if (value != null) {
				target.writeValue(in.propertyName(), value, true);
			}

		});
	}

	static {
		SCOPE_TYPE = ScopeType.SERVLET;
	}
}