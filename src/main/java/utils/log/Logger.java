package utils.log;

import java.util.function.Supplier;
import utils.log.Logger.Level;

public interface Logger {
	String getName();

	boolean isEnabled(Level arg0);

	void log(Level arg0, String arg1);

	default void log(Level level, Supplier<String> messageSupplier) {
		if (this.isEnabled(level)) {
			this.log(level, (String) messageSupplier.get());
		}

	}

	void log(Level arg0, String arg1, Throwable arg2);

	default void log(Level level, Supplier<String> messageSupplier, Throwable throwable) {
		if (this.isEnabled(level)) {
			this.log(level, (String) messageSupplier.get(), throwable);
		}

	}

	void setLevel(Level arg0);

	boolean isTraceEnabled();

	void trace(String arg0);

	default void trace(Supplier<String> messageSupplier) {
		if (this.isTraceEnabled()) {
			this.trace((String) messageSupplier.get());
		}

	}

	boolean isDebugEnabled();

	void debug(String arg0);

	default void debug(Supplier<String> messageSupplier) {
		if (this.isDebugEnabled()) {
			this.debug((String) messageSupplier.get());
		}

	}

	boolean isInfoEnabled();

	void info(String arg0);

	default void info(Supplier<String> messageSupplier) {
		if (this.isInfoEnabled()) {
			this.info((String) messageSupplier.get());
		}

	}

	boolean isWarnEnabled();

	void warn(String arg0);

	void warn(String arg0, Throwable arg1);

	default void warn(Supplier<String> messageSupplier) {
		if (this.isWarnEnabled()) {
			this.warn((String) messageSupplier.get());
		}

	}

	default void warn(Supplier<String> messageSupplier, Throwable throwable) {
		if (this.isWarnEnabled()) {
			this.warn((String) messageSupplier.get(), throwable);
		}

	}

	boolean isErrorEnabled();

	void error(String arg0);

	void error(String arg0, Throwable arg1);

	default void error(Supplier<String> messageSupplier) {
		if (this.isErrorEnabled()) {
			this.error((String) messageSupplier.get());
		}

	}

	default void error(Supplier<String> messageSupplier, Throwable throwable) {
		if (this.isErrorEnabled()) {
			this.error((String) messageSupplier.get(), throwable);
		}

	}
}