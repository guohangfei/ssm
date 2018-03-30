package utils.jtx;

import utils.jtx.JtxException;
import utils.jtx.JtxIsolationLevel;
import utils.jtx.JtxPropagationBehavior;
import utils.util.HashCode;

public class JtxTransactionMode {
	protected JtxPropagationBehavior propagationBehavior;
	private JtxIsolationLevel isolationLevel;
	public static final boolean READ_ONLY = true;
	public static final boolean READ_WRITE = false;
	private boolean readOnlyMode;
	public static final int DEFAULT_TIMEOUT = -1;
	private int timeout;

	public JtxTransactionMode() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_SUPPORTS;
		this.isolationLevel = JtxIsolationLevel.ISOLATION_DEFAULT;
		this.readOnlyMode = true;
		this.timeout = -1;
	}

	public JtxPropagationBehavior getPropagationBehavior() {
		return this.propagationBehavior;
	}

	public void setPropagationBehaviour(JtxPropagationBehavior propagation) {
		this.propagationBehavior = propagation;
	}

	public JtxTransactionMode propagationRequired() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_REQUIRED;
		return this;
	}

	public JtxTransactionMode propagationSupports() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_SUPPORTS;
		return this;
	}

	public JtxTransactionMode propagationMandatory() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_MANDATORY;
		return this;
	}

	public JtxTransactionMode propagationRequiresNew() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_REQUIRES_NEW;
		return this;
	}

	public JtxTransactionMode propagationNotSupported() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_NOT_SUPPORTED;
		return this;
	}

	public JtxTransactionMode propagationNever() {
		this.propagationBehavior = JtxPropagationBehavior.PROPAGATION_NEVER;
		return this;
	}

	public JtxIsolationLevel getIsolationLevel() {
		return this.isolationLevel;
	}

	public void setIsolationLevel(JtxIsolationLevel isolation) {
		this.isolationLevel = isolation;
	}

	public JtxTransactionMode isolationNone() {
		this.isolationLevel = JtxIsolationLevel.ISOLATION_NONE;
		return this;
	}

	public JtxTransactionMode isolationReadUncommitted() {
		this.isolationLevel = JtxIsolationLevel.ISOLATION_READ_UNCOMMITTED;
		return this;
	}

	public JtxTransactionMode isolationReadCommitted() {
		this.isolationLevel = JtxIsolationLevel.ISOLATION_READ_COMMITTED;
		return this;
	}

	public JtxTransactionMode isolationRepeatableRead() {
		this.isolationLevel = JtxIsolationLevel.ISOLATION_REPEATABLE_READ;
		return this;
	}

	public JtxTransactionMode isolationSerializable() {
		this.isolationLevel = JtxIsolationLevel.ISOLATION_SERIALIZABLE;
		return this;
	}

	public boolean isReadOnly() {
		return this.readOnlyMode;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnlyMode = readOnly;
	}

	public JtxTransactionMode readOnly(boolean readOnly) {
		this.readOnlyMode = readOnly;
		return this;
	}

	public int getTransactionTimeout() {
		return this.timeout;
	}

	public void setTransactionTimeout(int timeout) {
		if (timeout < -1) {
			throw new JtxException("Invalid TX timeout: " + timeout);
		} else {
			this.timeout = timeout;
		}
	}

	public JtxTransactionMode transactionTimeout(int timeout) {
		this.setTransactionTimeout(timeout);
		return this;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (this.getClass() != object.getClass()) {
			return false;
		} else {
			JtxTransactionMode mode = (JtxTransactionMode) object;
			return mode.getPropagationBehavior() == this.propagationBehavior
					&& mode.getIsolationLevel() == this.isolationLevel && mode.isReadOnly() == this.readOnlyMode
					&& mode.getTransactionTimeout() == this.timeout;
		}
	}

	public int hashCode() {
		return HashCode.create().hash(this.propagationBehavior).hash(this.readOnlyMode).hash(this.isolationLevel)
				.hash(this.timeout).get();
	}

	public String toString() {
		return "jtx{" + this.propagationBehavior + ',' + (this.readOnlyMode ? "readonly" : "readwrite") + ','
				+ this.isolationLevel.toString() + ',' + this.timeout + '}';
	}
}