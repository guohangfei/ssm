package utils.mutable;

import jodd.mutable.Value;

class ValueImpl<T> implements Value<T> {
	private T value;

	ValueImpl(T v) {
		this.value = v;
	}

	public T get() {
		return this.value;
	}

	public void set(T value) {
		this.value = value;
	}

	public String toString() {
		return this.value == null ? "value: {null}" : "value: {" + this.value.toString() + '}';
	}
}