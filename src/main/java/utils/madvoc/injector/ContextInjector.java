package utils.madvoc.injector;

import jodd.madvoc.injector.Targets;

public interface ContextInjector<C> {
	void injectContext(Targets arg0, C arg1);
}