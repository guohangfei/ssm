package utils.madvoc.injector;

import java.util.Enumeration;
import java.util.function.BiConsumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.injector.Injector;
import utils.madvoc.injector.Outjector;
import utils.madvoc.injector.Targets;

public class SessionScopeInjector implements Injector, Outjector {
	private static final ScopeType SCOPE_TYPE;

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			HttpSession session = servletRequest.getSession();
			Enumeration attributeNames = session.getAttributeNames();

			while (attributeNames.hasMoreElements()) {
				String attrName = (String) attributeNames.nextElement();
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					String name = in.matchedPropertyName(attrName);
					if (name != null) {
						Object attrValue = session.getAttribute(attrName);
						target.writeValue(name, attrValue, true);
					}

				});
			}

		}
	}

	public void outject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			HttpSession session = servletRequest.getSession();
			targets.forEachTargetAndOutScopes(SCOPE_TYPE, (target, out) -> {
				Object value = target.readTargetProperty(out);
				session.setAttribute(out.name, value);
			});
		}
	}

	static {
		SCOPE_TYPE = ScopeType.SESSION;
	}
}