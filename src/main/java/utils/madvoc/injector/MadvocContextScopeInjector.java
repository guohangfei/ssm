package utils.madvoc.injector;

import java.util.function.BiConsumer;
import utils.madvoc.ActionRequest;
import utils.madvoc.ScopeType;
import utils.madvoc.injector.ContextInjector;
import utils.madvoc.injector.Injector;
import utils.madvoc.injector.Targets;
import utils.petite.PetiteContainer;

public class MadvocContextScopeInjector implements Injector, ContextInjector<PetiteContainer> {
	private static final ScopeType SCOPE_TYPE;
	protected final PetiteContainer madpc;

	public MadvocContextScopeInjector(PetiteContainer madpc) {
		this.madpc = madpc;
	}

	public void injectContext(Targets targets, PetiteContainer madpc) {
		targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
			Object value = madpc.getBean(in.name);
			if (value != null) {
				target.writeValue(in.propertyName(), value, false);
			}

		});
	}

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
			Object value = this.madpc.getBean(in.name);
			if (value != null) {
				target.writeValue(in.propertyName(), value, false);
			}

		});
	}

	static {
		SCOPE_TYPE = ScopeType.CONTEXT;
	}
}