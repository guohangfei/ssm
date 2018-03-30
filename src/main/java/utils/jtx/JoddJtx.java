package utils.jtx;

import java.lang.annotation.Annotation;
import jodd.jtx.meta.ReadOnlyTransaction;
import jodd.jtx.meta.ReadWriteTransaction;
import jodd.jtx.meta.Transaction;

public class JoddJtx {
	private static final JoddJtx instance = new JoddJtx();
	private Class<? extends Annotation>[] txAnnotations = new Class[]{Transaction.class, ReadWriteTransaction.class,
			ReadOnlyTransaction.class};

	public static JoddJtx defaults() {
		return instance;
	}

	public Class<? extends Annotation>[] getTxAnnotations() {
		return this.txAnnotations;
	}

	public void setTxAnnotations(Class... txAnnotations) {
		this.txAnnotations = txAnnotations;
	}
}