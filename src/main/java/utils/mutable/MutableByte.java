package utils.mutable;

public final class MutableByte extends Number implements Comparable<MutableByte>, Cloneable {
	public byte value;

	public static MutableByte of(byte value) {
		return new MutableByte(value);
	}

	public MutableByte() {
	}

	public MutableByte(byte value) {
		this.value = value;
	}

	public MutableByte(String value) {
		this.value = Byte.parseByte(value);
	}

	public MutableByte(Number number) {
		this.value = number.byteValue();
	}

	public byte get() {
		return this.value;
	}

	public void set(byte value) {
		this.value = value;
	}

	public void set(Number value) {
		this.value = value.byteValue();
	}

	public String toString() {
		return Integer.toString(this.value);
	}

	public int hashCode() {
		return this.value;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (Byte.valueOf(this.value).getClass() == obj.getClass()) {
				return this.value == ((Byte) obj).byteValue();
			}

			if (this.getClass() == obj.getClass()) {
				return this.value == ((MutableByte) obj).value;
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

	public int compareTo(MutableByte other) {
		return this.value < other.value ? -1 : (this.value == other.value ? 0 : 1);
	}

	public MutableByte clone() {
		return new MutableByte(this.value);
	}
}