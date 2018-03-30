package utils.mutable;

public final class MutableFloat extends Number implements Comparable<MutableFloat>, Cloneable {
	public float value;

	public static MutableFloat of(float value) {
		return new MutableFloat(value);
	}

	public MutableFloat() {
	}

	public MutableFloat(float value) {
		this.value = value;
	}

	public MutableFloat(String value) {
		this.value = Float.parseFloat(value);
	}

	public MutableFloat(Number number) {
		this.value = number.floatValue();
	}

	public float get() {
		return this.value;
	}

	public void set(float value) {
		this.value = value;
	}

	public void set(Number value) {
		this.value = value.floatValue();
	}

	public String toString() {
		return Float.toString(this.value);
	}

	public int hashCode() {
		return Float.floatToIntBits(this.value);
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (Float.valueOf(this.value).getClass() == obj.getClass()) {
				return Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float) obj).floatValue());
			}

			if (this.getClass() == obj.getClass()) {
				return Float.floatToIntBits(this.value) == Float.floatToIntBits(((MutableFloat) obj).value);
			}
		}

		return false;
	}

	public int intValue() {
		return (int) this.value;
	}

	public long longValue() {
		return (long) this.value;
	}

	public float floatValue() {
		return this.value;
	}

	public double doubleValue() {
		return (double) this.value;
	}

	public boolean isNaN() {
		return Float.isNaN(this.value);
	}

	public boolean isInfinite() {
		return Float.isInfinite(this.value);
	}

	public int compareTo(MutableFloat other) {
		return Float.compare(this.value, other.value);
	}

	public MutableFloat clone() {
		return new MutableFloat(this.value);
	}
}