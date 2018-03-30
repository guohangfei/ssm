package utils.madvoc.injector;

import java.util.function.BiConsumer;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.config.ActionRuntime;
import utils.madvoc.config.RouteChunk;
import utils.madvoc.injector.Injector;
import utils.madvoc.injector.Targets;
import utils.madvoc.macro.PathMacros;
import utils.util.StringUtil;

public class ActionPathMacroInjector implements Injector {
	private static final ScopeType SCOPE_TYPE;

	public void inject(ActionRequest actionRequest) {
		ActionRuntime actionRuntime = actionRequest.getActionRuntime();
		RouteChunk routeChunk = actionRuntime.getRouteChunk();
		if (routeChunk.hasMacrosOnPath()) {
			Targets targets = actionRequest.getTargets();
			if (targets.usesScope(SCOPE_TYPE)) {
				String[] actionPath = actionRequest.getActionPathChunks();
				int ndx = actionPath.length - 1;

				for (RouteChunk chunk = routeChunk; chunk.parent() != null; chunk = chunk.parent()) {
					PathMacros pathMacros = chunk.pathMacros();
					if (pathMacros != null) {
						this.injectMacros(actionPath[ndx], pathMacros, targets);
					}

					--ndx;
				}

			}
		}
	}

	private void injectMacros(String actionPath, PathMacros pathMacros, Targets targets) {
		String[] names = pathMacros.names();
		String[] values = pathMacros.extract(actionPath);

		for (int ndx = 0; ndx < values.length; ++ndx) {
			String value = values[ndx];
			if (!StringUtil.isEmpty(value)) {
				String macroName = names[ndx];
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					String name = in.matchedPropertyName(macroName);
					if (name != null) {
						target.writeValue(name, value, true);
					}

				});
			}
		}

	}

	static {
		SCOPE_TYPE = ScopeType.REQUEST;
	}
}