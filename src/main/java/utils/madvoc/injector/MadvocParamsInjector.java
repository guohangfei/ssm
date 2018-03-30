package utils.madvoc.injector;

import java.util.function.Consumer;
import jodd.madvoc.MadvocConfig;
import jodd.madvoc.injector.ContextInjector;
import jodd.madvoc.injector.Targets;
import jodd.petite.ParamManager;
import jodd.petite.PetiteContainer;

public class MadvocParamsInjector implements ContextInjector<PetiteContainer> {
	protected final MadvocConfig madvocConfig;

	public MadvocParamsInjector(MadvocConfig madvocConfig) {
		this.madvocConfig = madvocConfig;
	}

	public void injectContext(Targets targets, PetiteContainer madpc) {
		targets.forEachTarget((target) -> {
			Class targetType = target.resolveType();
			String baseName = targetType.getName();
			ParamManager madvocPetiteParamManager = madpc.paramManager();
			String[] params = madvocPetiteParamManager.filterParametersForBeanName(baseName, true);
			String[] arg5 = params;
			int arg6 = params.length;

			for (int arg7 = 0; arg7 < arg6; ++arg7) {
				String param = arg5[arg7];
				Object value = madvocPetiteParamManager.get(param);
				String propertyName = param.substring(baseName.length() + 1);
				target.writeValue(propertyName, value, false);
			}

		});
	}
}