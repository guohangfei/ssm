package utils.madvoc.injector;

import java.io.IOException;
import java.util.function.BiConsumer;
import utils.json.JsonParser;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.injector.Injector;
import utils.madvoc.injector.Targets;
import utils.servlet.ServletUtil;
import utils.util.StringUtil;

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