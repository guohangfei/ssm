package utils.madvoc.injector;

import java.io.IOException;
import java.util.function.BiConsumer;
import jodd.json.JsonParser;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.ScopeType;
import jodd.madvoc.injector.Injector;
import jodd.madvoc.injector.Targets;
import jodd.servlet.ServletUtil;
import jodd.util.StringUtil;

public class RequestBodyScopeInject implements Injector {
	private static final ScopeType SCOPE_TYPE;

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			String body;
			try {
				body = ServletUtil.readRequestBodyFromStream(actionRequest.getHttpServletRequest());
			} catch (IOException arg4) {
				return;
			}

			if (!StringUtil.isEmpty(body)) {
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					Object value = JsonParser.create().parse(body, in.type);
					target.writeValue(in.propertyName(), value, true);
				});
			}
		}
	}

	static {
		SCOPE_TYPE = ScopeType.BODY;
	}
}