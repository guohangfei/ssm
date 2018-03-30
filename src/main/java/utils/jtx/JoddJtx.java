package utils.jtx;

import java.lang.annotation.Annotation;
import utils.jtx.meta.ReadOnlyTransaction;
import utils.jtx.meta.ReadWriteTransaction;
import utils.jtx.meta.Transaction;

public class utilsJtx {
	private static final utilsJtx instance = new utilsJtx();
	private Class<? extends Annotation>[] txAnnotations = new Class[]{Transaction.class, ReadWriteTransaction.class,
			ReadOnlyTransaction.class};

	public static utilsJtx defaults() {
		return instance;
	}

	public Class<? extends Annotation>[] getTxAnnotations() {
		return this.txAnnotations;
	}

	public void setTxAnnotations(Class... txAnnotations) {
		this.txAnnotations = txAnnotations;
	}
}