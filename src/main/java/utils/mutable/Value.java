package utils.mutable;

import java.util.Optional;
import java.util.function.Supplier;
import utils.mutable.ValueImpl;

public interface Value<T> extends Supplier<T> {
	static default <R> Value<R> of(R value) {
		return new ValueImpl(value);
	}

	default Optional<T> optional() {
		return Optional.ofNullable(this.get());
	}

	void set(T arg0);
}