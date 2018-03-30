package utils.madvoc.injector;

import java.util.function.BiConsumer;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.ScopeType;
import jodd.madvoc.injector.ContextInjector;
import jodd.madvoc.injector.Injector;
import jodd.madvoc.injector.Targets;
import jodd.petite.PetiteContainer;

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