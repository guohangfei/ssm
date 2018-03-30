package utils.jtx;

import jodd.jtx.JtxResourceManager;
import jodd.jtx.JtxTransaction;
import jodd.util.ClassUtil;

final class JtxResource<E> {
	final JtxTransaction transaction;
	final JtxResourceManager<E> resourceManager;
	private final E resource;

	JtxResource(JtxTransaction transaction, JtxResourceManager<E> resourceManager, E resource) {
		this.transaction = transaction;
		this.resourceManager = resourceManager;
		this.resource = resource;
	}

	public boolean isSameTypeAsResource(Class type) {
		return ClassUtil.isTypeOf(type, this.resource.getClass());
	}

	void commitTransaction() {
		this.resourceManager.commitTransaction(this.resource);
	}

	void rollbackTransaction() {
		this.resourceManager.rollbackTransaction(this.resource);
	}

	public E getResource() {
		return this.resource;
	}
}