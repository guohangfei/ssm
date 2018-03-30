package utils.mutable;

public final class MutableShort extends Number implements Comparable<MutableShort>, Cloneable {
	public short value;

	public static MutableShort of(short value) {
		return new MutableShort(value);
	}

	public MutableShort() {
	}

	public MutableShort(short value) {
		this.value = value;
	}

	public MutableShort(String value) {
		this.value = Short.parseShort(value);
	}

	public MutableShort(Number number) {
		this.value = number.shortValue();
	}

	public short get() {
		return this.value;
	}

	public void set(short value) {
		this.value = value;
	}

	public void set(Number value) {
		this.value = value.shortValue();
	}

	public String toString() {
		return Integer.toString(this.value);
	}

	public int hashCode() {
		return this.value;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (Short.valueOf(this.value).getClass() == obj.getClass()) {
				return this.value == ((Short) obj).shortValue();
			}

			if (this.getClass() == obj.getClass()) {
				return this.value == ((MutableShort) obj).value;
			}
		}

		return false;
	}

	public int intValue() {
		return this.value;
	}

	public long longValue() {
		return (long) this.value;
	}

	public float floatValue() {
		return (float) this.value;
	}

	public double doubleValue() {
		return (double) this.value;
	}

	public int compareTo(MutableShort other) {
		return this.value < other.value ? -1 : (this.value == other.value ? 0 : 1);
	}

	public MutableShort clone() {
		return new MutableShort(this.value);
	}
}