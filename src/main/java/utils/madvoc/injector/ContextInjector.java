package utils.madvoc.injector;

import utils.madvoc.injector.Targets;

public interface ContextInjector<C> {
	void injectContext(Targets arg0, C arg1);
}