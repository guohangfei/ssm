package utils.madvoc.injector;

import java.util.Enumeration;
import java.util.function.BiConsumer;
import javax.servlet.ServletContext;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.ScopeType;
import jodd.madvoc.injector.ContextInjector;
import jodd.madvoc.injector.Injector;
import jodd.madvoc.injector.Outjector;
import jodd.madvoc.injector.Targets;

public class ApplicationScopeInjector implements Injector, Outjector, ContextInjector<ServletContext> {
	private static final ScopeType SCOPE_TYPE;

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			ServletContext servletContext = actionRequest.getHttpServletRequest().getSession().getServletContext();
			Enumeration attributeNames = servletContext.getAttributeNames();

			while (attributeNames.hasMoreElements()) {
				String attrName = (String) attributeNames.nextElement();
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					String name = in.matchedPropertyName(attrName);
					if (name != null) {
						Object attrValue = servletContext.getAttribute(attrName);
						target.writeValue(name, attrValue, true);
					}

				});
			}

		}
	}

	public void injectContext(Targets targets, ServletContext servletContext) {
		if (targets.usesScope(SCOPE_TYPE)) {
			Enumeration attributeNames = servletContext.getAttributeNames();

			while (attributeNames.hasMoreElements()) {
				String attrName = (String) attributeNames.nextElement();
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					String name = in.matchedPropertyName(attrName);
					if (name != null) {
						Object attrValue = servletContext.getAttribute(attrName);
						target.writeValue(name, attrValue, true);
					}

				});
			}

		}
	}

	public void outject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			ServletContext context = actionRequest.getHttpServletRequest().getSession().getServletContext();
			targets.forEachTargetAndOutScopes(SCOPE_TYPE, (target, out) -> {
				Object value = target.readTargetProperty(out);
				context.setAttribute(out.name, value);
			});
		}
	}

	static {
		SCOPE_TYPE = ScopeType.APPLICATION;
	}
}