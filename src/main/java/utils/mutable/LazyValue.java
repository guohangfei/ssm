package utils.mutable;

import java.util.Optional;
import java.util.function.Supplier;

public class LazyValue<T> implements Supplier<T> {
	private Supplier<T> supplier;
	private volatile boolean initialized;
	private T value;

	public static <T> LazyValue<T> of(Supplier<T> supplier) {
		return new LazyValue(supplier);
	}

	private LazyValue(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get() {
		if (!this.initialized) {
			synchronized (this) {
				if (!this.initialized) {
					Object t = this.supplier.get();
					this.value = t;
					this.initialized = true;
					this.supplier = null;
					return t;
				}
			}
		}

		return this.value;
	}

	public Optional<T> optional() {
		return Optional.ofNullable(this.get());
	}
}