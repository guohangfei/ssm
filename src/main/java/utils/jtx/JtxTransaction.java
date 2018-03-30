package utils.jtx;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import utils.jtx.JtxException;
import utils.jtx.JtxResource;
import utils.jtx.JtxResourceManager;
import utils.jtx.JtxStatus;
import utils.jtx.JtxTransactionManager;
import utils.jtx.JtxTransactionMode;
import utils.log.Logger;
import utils.log.LoggerFactory;

public class JtxTransaction {
	private static final Logger log = LoggerFactory.getLogger(JtxTransaction.class);
	protected final JtxTransactionManager txManager;
	protected final JtxTransactionMode mode;
	protected final Set<utils.jtx.JtxResource> resources;
	protected final Object scope;
	protected final long deadline;
	protected final boolean startAsActive;
	protected Throwable rollbackCause;
	protected JtxStatus status;

	public JtxTransaction(JtxTransactionManager txManager, JtxTransactionMode mode, Object scope, boolean active) {
		this.txManager = txManager;
		this.mode = mode;
		this.scope = scope;
		this.resources = new HashSet();
		this.deadline = mode.getTransactionTimeout() == -1
				? -1L
				: System.currentTimeMillis() + (long) mode.getTransactionTimeout() * 1000L;
		this.status = active ? JtxStatus.STATUS_ACTIVE : JtxStatus.STATUS_NO_TRANSACTION;
		this.startAsActive = active;
		txManager.associateTransaction(this);
		if (log.isDebugEnabled()) {
			log.debug("New JTX {status:" + this.status + ", mode:" + this.mode + '}');
		}

	}

	public JtxTransactionMode getTransactionMode() {
		return this.mode;
	}

	public JtxTransactionManager getTransactionManager() {
		return this.txManager;
	}

	public Object getScope() {
		return this.scope;
	}

	public JtxStatus getStatus() {
		return this.status;
	}

	public boolean isStartAsActive() {
		return this.startAsActive;
	}

	public boolean isActive() {
		return this.status == JtxStatus.STATUS_ACTIVE;
	}

	public boolean isNoTransaction() {
		return this.status == JtxStatus.STATUS_NO_TRANSACTION;
	}

	public boolean isCommitted() {
		return this.status == JtxStatus.STATUS_COMMITTED;
	}

	public boolean isRolledback() {
		return this.status == JtxStatus.STATUS_ROLLEDBACK;
	}

	public boolean isCompleted() {
		return this.status == JtxStatus.STATUS_COMMITTED || this.status == JtxStatus.STATUS_ROLLEDBACK;
	}

	public void setRollbackOnly() {
		this.setRollbackOnly((Throwable) null);
	}

	public void setRollbackOnly(Throwable th) {
		if (!this.isNoTransaction() && this.status != JtxStatus.STATUS_MARKED_ROLLBACK
				&& this.status != JtxStatus.STATUS_ACTIVE) {
			throw new JtxException("TNo active TX that can be marked as rollback only");
		} else {
			this.rollbackCause = th;
			this.status = JtxStatus.STATUS_MARKED_ROLLBACK;
		}
	}

	public boolean isRollbackOnly() {
		return this.status == JtxStatus.STATUS_MARKED_ROLLBACK;
	}

	protected void checkTimeout() {
		if (this.deadline != -1L) {
			if (this.deadline - System.currentTimeMillis() < 0L) {
				this.setRollbackOnly();
				throw new JtxException("TX timed out, marked as rollback only");
			}
		}
	}

	public void commit() {
		this.checkTimeout();
		this.commitOrRollback(true);
	}

	public void rollback() {
		this.commitOrRollback(false);
	}

	protected void commitOrRollback(boolean doCommit) {
		if (log.isDebugEnabled()) {
			if (doCommit) {
				log.debug("Commit JTX");
			} else {
				log.debug("Rollback JTX");
			}
		}

		boolean forcedRollback = false;
		if (!this.isNoTransaction()) {
			if (this.isRollbackOnly()) {
				if (doCommit) {
					doCommit = false;
					forcedRollback = true;
				}
			} else if (!this.isActive()) {
				if (this.isCompleted()) {
					throw new JtxException("TX is already completed, commit or rollback should be called once per TX");
				}

				throw new JtxException("No active TX to " + (doCommit ? "commit" : "rollback"));
			}
		}

		if (doCommit) {
			this.commitAllResources();
		} else {
			this.rollbackAllResources(forcedRollback);
		}

	}

	protected void commitAllResources() throws JtxException {
		this.status = JtxStatus.STATUS_COMMITTING;
		Exception lastException = null;
		Iterator it = this.resources.iterator();

		while (it.hasNext()) {
			utils.jtx.JtxResource resource = (utils.jtx.JtxResource) it.next();

			try {
				resource.commitTransaction();
				it.remove();
			} catch (Exception arg4) {
				lastException = arg4;
			}
		}

		if (lastException != null) {
			this.setRollbackOnly(lastException);
			throw new JtxException("Commit failed: one or more TX resources couldn\'t commit a TX", lastException);
		} else {
			this.txManager.removeTransaction(this);
			this.status = JtxStatus.STATUS_COMMITTED;
		}
	}

	protected void rollbackAllResources(boolean wasForced) {
		this.status = JtxStatus.STATUS_ROLLING_BACK;
		Exception lastException = null;
		Iterator it = this.resources.iterator();

		while (it.hasNext()) {
			utils.jtx.JtxResource resource = (utils.jtx.JtxResource) it.next();

			try {
				resource.rollbackTransaction();
			} catch (Exception arg8) {
				lastException = arg8;
			} finally {
				it.remove();
			}
		}

		this.txManager.removeTransaction(this);
		this.status = JtxStatus.STATUS_ROLLEDBACK;
		if (lastException != null) {
			this.status = JtxStatus.STATUS_UNKNOWN;
			throw new JtxException("Rollback failed: one or more TX resources couldn\'t rollback a TX", lastException);
		} else if (wasForced) {
			throw new JtxException("TX rolled back because it has been marked as rollback-only", this.rollbackCause);
		}
	}

	public <E> E requestResource(Class<E> resourceType) {
		if (this.isCompleted()) {
			throw new JtxException("TX is already completed, resource are not available after commit or rollback");
		} else if (this.isRollbackOnly()) {
			throw new JtxException("TX is marked as rollback only, resource are not available", this.rollbackCause);
		} else if (!this.isNoTransaction() && !this.isActive()) {
			throw new JtxException("Resources are not available since TX is not active");
		} else {
			this.checkTimeout();
			Object resource = this.lookupResource(resourceType);
			if (resource == null) {
				int maxResources = this.txManager.getMaxResourcesPerTransaction();
				if (maxResources != -1 && this.resources.size() >= maxResources) {
					throw new JtxException("TX already has attached max. number of resources");
				}

				JtxResourceManager resourceManager = this.txManager.lookupResourceManager(resourceType);
				resource = resourceManager.beginTransaction(this.mode, this.isActive());
				this.resources.add(new utils.jtx.JtxResource(this, resourceManager, resource));
			}

			return resource;
		}
	}

	protected <E> E lookupResource(Class<E> resourceType) {
		Iterator arg1 = this.resources.iterator();

		utils.jtx.JtxResource jtxResource;
		do {
			if (!arg1.hasNext()) {
				return null;
			}

			jtxResource = (utils.jtx.JtxResource) arg1.next();
		} while (!jtxResource.isSameTypeAsResource(resourceType));

		return jtxResource.getResource();
	}
}