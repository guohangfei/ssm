package utils.jtx;

public enum JtxStatus {
	STATUS_ACTIVE(0), STATUS_MARKED_ROLLBACK(1), STATUS_COMMITTED(3), STATUS_ROLLEDBACK(4), STATUS_UNKNOWN(
			5), STATUS_NO_TRANSACTION(6), STATUS_COMMITTING(8), STATUS_ROLLING_BACK(9);

	private int value;

	private JtxStatus(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public String toString() {
		switch (this.value) {
			case 0 :
				return "Active";
			case 1 :
				return "Marked for rollback";
			case 2 :
			case 7 :
			default :
				return "Undefined";
			case 3 :
				return "Committed";
			case 4 :
				return "Rolled back";
			case 5 :
				return "Unknown";
			case 6 :
				return "No transaction";
			case 8 :
				return "Committing";
			case 9 :
				return "Rolling back";
		}
	}
}