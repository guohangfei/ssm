package utils.jtx;

public enum JtxIsolationLevel {
	ISOLATION_DEFAULT(0), ISOLATION_NONE(1), ISOLATION_READ_UNCOMMITTED(2), ISOLATION_READ_COMMITTED(
			3), ISOLATION_REPEATABLE_READ(4), ISOLATION_SERIALIZABLE(5);

	private int value;

	private JtxIsolationLevel(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public String toString() {
		switch (this.value) {
			case 0 :
				return "Default";
			case 1 :
				return "None";
			case 2 :
				return "Read Uncommitted";
			case 3 :
				return "Read Committed";
			case 4 :
				return "Repeatable Read";
			case 5 :
				return "Serializable";
			default :
				return "Undefined";
		}
	}
}