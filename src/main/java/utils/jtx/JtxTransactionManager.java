package utils.jtx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import utils.jtx.JtxException;
import utils.jtx.JtxIsolationLevel;
import utils.jtx.JtxResourceManager;
import utils.jtx.JtxStatus;
import utils.jtx.JtxTransaction;
import utils.jtx.JtxTransactionMode;import utils.jtx.JtxTransactionManager.1;
import utils.log.Logger;
import utils.log.LoggerFactory;

public class JtxTransactionManager {
	private static final Logger log = LoggerFactory.getLogger(JtxTransactionManager.class);
	protected int maxResourcesPerTransaction = -1;
	protected boolean oneResourceManager;
	protected boolean validateExistingTransaction;
	protected boolean ignoreScope;
	protected Map<Class, JtxResourceManager> resourceManagers = new HashMap();
	protected final ThreadLocal<ArrayList<JtxTransaction>> txStack = new ThreadLocal();
	protected int totalTransactions;

	public int getMaxResourcesPerTransaction() {
		return this.maxResourcesPerTransaction;
	}

	public void setMaxResourcesPerTransaction(int maxResourcesPerTransaction) {
		this.maxResourcesPerTransaction = maxResourcesPerTransaction;
	}

	public boolean isValidateExistingTransaction() {
		return this.validateExistingTransaction;
	}

	public void setValidateExistingTransaction(boolean validateExistingTransaction) {
		this.validateExistingTransaction = validateExistingTransaction;
	}

	public boolean isSingleResourceManager() {
		return this.oneResourceManager;
	}

	public void setSingleResourceManager(boolean oneResourceManager) {
		this.oneResourceManager = oneResourceManager;
	}

	public boolean isIgnoreScope() {
		return this.ignoreScope;
	}

	public void setIgnoreScope(boolean ignoreScope) {
		this.ignoreScope = ignoreScope;
	}

	public int totalThreadTransactions() {
		ArrayList txList = (ArrayList) this.txStack.get();
		return txList == null ? 0 : txList.size();
	}

	public int totalThreadTransactionsWithStatus(JtxStatus status) {
		ArrayList txlist = (ArrayList) this.txStack.get();
		if (txlist == null) {
			return 0;
		} else {
			int count = 0;
			Iterator arg3 = txlist.iterator();

			while (arg3.hasNext()) {
				JtxTransaction tx = (JtxTransaction) arg3.next();
				if (tx.getStatus() == status) {
					++count;
				}
			}

			return count;
		}
	}

	public int totalActiveThreadTransactions() {
		return this.totalThreadTransactionsWithStatus(JtxStatus.STATUS_ACTIVE);
	}

	public boolean isAssociatedWithThread(JtxTransaction tx) {
		ArrayList txList = (ArrayList) this.txStack.get();
		return txList == null ? false : txList.contains(tx);
	}

	protected boolean removeTransaction(JtxTransaction tx) {
		ArrayList txList = (ArrayList) this.txStack.get();
		if (txList == null) {
			return false;
		} else {
			boolean removed = txList.remove(tx);
			if (removed) {
				--this.totalTransactions;
			}

			if (txList.isEmpty()) {
				this.txStack.remove();
			}

			return removed;
		}
	}

	public JtxTransaction getTransaction() {
		ArrayList txlist = (ArrayList) this.txStack.get();
		return txlist == null ? null : (txlist.isEmpty() ? null : (JtxTransaction) txlist.get(txlist.size() - 1));
	}

	protected void associateTransaction(JtxTransaction tx) {
		++this.totalTransactions;
		ArrayList txList = (ArrayList) this.txStack.get();
		if (txList == null) {
			txList = new ArrayList();
			this.txStack.set(txList);
		}

		txList.add(tx);
	}

	public int totalTransactions() {
		return this.totalTransactions;
	}

	protected JtxTransaction createNewTransaction(JtxTransactionMode tm, Object scope, boolean active) {
		return new JtxTransaction(this, tm, scope, active);
	}

	public JtxTransaction requestTransaction(JtxTransactionMode mode) {
		return this.requestTransaction(mode, (Object) null);
	}

	public JtxTransaction requestTransaction(JtxTransactionMode mode, Object scope) {
      if(log.isDebugEnabled()) {
         log.debug("Requesting TX " + mode.toString());
      }

      JtxTransaction currentTx = this.getTransaction();
      if(!this.isNewTxScope(currentTx, scope)) {
         return currentTx;
      } else {
         switch(1.$SwitchMap$utils$jtx$JtxPropagationBehavior[mode.getPropagationBehavior().ordinal()]) {
         case 1:
            return this.propRequired(currentTx, mode, scope);
         case 2:
            return this.propSupports(currentTx, mode, scope);
         case 3:
            return this.propMandatory(currentTx, mode, scope);
         case 4:
            return this.propRequiresNew(currentTx, mode, scope);
         case 5:
            return this.propNotSupported(currentTx, mode, scope);
         case 6:
            return this.propNever(currentTx, mode, scope);
         default:
            throw new JtxException("Invalid TX propagation value: " + mode.getPropagationBehavior().value());
         }
      }
   }

	protected boolean isNewTxScope(JtxTransaction currentTx, Object destScope) {
		return this.ignoreScope
				? true
				: (currentTx == null
						? true
						: (destScope == null
								? true
								: (currentTx.getScope() == null ? true : !destScope.equals(currentTx.getScope()))));
	}

	protected void continueTx(JtxTransaction sourceTx, JtxTransactionMode destMode) {
		if (this.validateExistingTransaction) {
			JtxTransactionMode sourceMode = sourceTx.getTransactionMode();
			JtxIsolationLevel destIsolationLevel = destMode.getIsolationLevel();
			if (destIsolationLevel != JtxIsolationLevel.ISOLATION_DEFAULT) {
				JtxIsolationLevel currentIsolationLevel = sourceMode.getIsolationLevel();
				if (currentIsolationLevel != destIsolationLevel) {
					throw new JtxException("Participating TX specifies isolation level: " + destIsolationLevel
							+ " which is incompatible with existing TX: " + currentIsolationLevel);
				}
			}

			if (!destMode.isReadOnly() && sourceMode.isReadOnly()) {
				throw new JtxException("Participating TX is not marked as read-only, but existing TX is");
			}
		}
	}

	protected JtxTransaction propRequired(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		if (currentTx != null && !currentTx.isNoTransaction()) {
			this.continueTx(currentTx, mode);
		} else {
			currentTx = this.createNewTransaction(mode, scope, true);
		}

		return currentTx;
	}

	protected JtxTransaction propRequiresNew(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		return this.createNewTransaction(mode, scope, true);
	}

	protected JtxTransaction propSupports(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		if (currentTx != null && !currentTx.isNoTransaction()) {
			this.continueTx(currentTx, mode);
		}

		if (currentTx == null) {
			currentTx = this.createNewTransaction(mode, scope, false);
		}

		return currentTx;
	}

	protected JtxTransaction propMandatory(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		if (currentTx != null && !currentTx.isNoTransaction()) {
			this.continueTx(currentTx, mode);
			return currentTx;
		} else {
			throw new JtxException("No existing TX found for TX marked with propagation \'mandatory\'");
		}
	}

	protected JtxTransaction propNotSupported(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		return currentTx == null
				? this.createNewTransaction(mode, scope, false)
				: (currentTx.isNoTransaction() ? currentTx : this.createNewTransaction(mode, scope, false));
	}

	protected JtxTransaction propNever(JtxTransaction currentTx, JtxTransactionMode mode, Object scope) {
		if (currentTx != null && !currentTx.isNoTransaction()) {
			throw new JtxException("Existing TX found for TX marked with propagation \'never\'");
		} else {
			if (currentTx == null) {
				currentTx = this.createNewTransaction(mode, scope, false);
			}

			return currentTx;
		}
	}

	public void registerResourceManager(JtxResourceManager resourceManager) {
		if (this.oneResourceManager && !this.resourceManagers.isEmpty()) {
			throw new JtxException("TX manager allows only one resource manager");
		} else {
			this.resourceManagers.put(resourceManager.getResourceType(), resourceManager);
		}
	}

	protected <E> JtxResourceManager<E> lookupResourceManager(Class<E> resourceType) {
		JtxResourceManager resourceManager = (JtxResourceManager) this.resourceManagers.get(resourceType);
		if (resourceManager == null) {
			throw new JtxException("No registered resource manager for resource type: " + resourceType.getSimpleName());
		} else {
			return resourceManager;
		}
	}

	public void close() {
		Iterator arg0 = this.resourceManagers.values().iterator();

		while (arg0.hasNext()) {
			JtxResourceManager resourceManager = (JtxResourceManager) arg0.next();

			try {
				resourceManager.close();
			} catch (Exception arg3) {
				;
			}
		}

		this.resourceManagers.clear();
	}
}