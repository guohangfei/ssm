package utils.mutable;

public final class MutableLong extends Number implements Comparable<MutableLong>, Cloneable {
	public long value;

	public static MutableLong of(long value) {
		return new MutableLong(value);
	}

	public MutableLong() {
	}

	public MutableLong(long value) {
		this.value = value;
	}

	public MutableLong(String value) {
		this.value = Long.parseLong(value);
	}

	public MutableLong(Number number) {
		this.value = number.longValue();
	}

	public long get() {
		return this.value;
	}

	public void set(long value) {
		this.value = value;
	}

	public void set(Number value) {
		this.value = value.longValue();
	}

	public String toString() {
		return Long.toString(this.value);
	}

	public int hashCode() {
		return (int) (this.value ^ this.value >>> 32);
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (Long.valueOf(this.value).getClass() == obj.getClass()) {
				return this.value == ((Long) obj).longValue();
			}

			if (this.getClass() == obj.getClass()) {
				return this.value == ((MutableLong) obj).value;
			}
		}

		return false;
	}

	public int intValue() {
		return (int) this.value;
	}

	public long longValue() {
		return this.value;
	}

	public float floatValue() {
		return (float) this.value;
	}

	public double doubleValue() {
		return (double) this.value;
	}

	public int compareTo(MutableLong other) {
		return this.value < other.value ? -1 : (this.value == other.value ? 0 : 1);
	}

	public MutableLong clone() {
		return new MutableLong(this.value);
	}
}