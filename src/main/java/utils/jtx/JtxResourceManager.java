package utils.jtx;

import utils.jtx.JtxTransactionMode;

public interface JtxResourceManager<E> {
	Class<E> getResourceType();

	E beginTransaction(JtxTransactionMode arg0, boolean arg1);

	void commitTransaction(E arg0);

	void rollbackTransaction(E arg0);

	void close();
}