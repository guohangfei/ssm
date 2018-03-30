package utils.mutable;

public final class MutableBoolean implements Comparable<MutableBoolean>, Cloneable {
	public boolean value;

	public static MutableBoolean of(boolean value) {
		return new MutableBoolean(value);
	}

	public MutableBoolean() {
	}

	public MutableBoolean(boolean value) {
		this.value = value;
	}

	public MutableBoolean(String value) {
		this.value = Boolean.valueOf(value).booleanValue();
	}

	public MutableBoolean(Boolean value) {
		this.value = value.booleanValue();
	}

	public MutableBoolean(Number number) {
		this.value = number.intValue() != 0;
	}

	public boolean get() {
		return this.value;
	}

	public void set(boolean value) {
		this.value = value;
	}

	public void set(Boolean value) {
		this.value = value.booleanValue();
	}

	public String toString() {
		return Boolean.toString(this.value);
	}

	public int hashCode() {
		return this.value ? 1231 : 1237;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (Boolean.valueOf(this.value).getClass() == obj.getClass()) {
				return this.value == ((Boolean) obj).booleanValue();
			}

			if (this.getClass() == obj.getClass()) {
				return this.value == ((MutableBoolean) obj).value;
			}
		}

		return false;
	}

	public int compareTo(MutableBoolean o) {
		return this.value == o.value ? 0 : (!this.value ? -1 : 1);
	}

	public MutableBoolean clone() {
		return new MutableBoolean(this.value);
	}
}