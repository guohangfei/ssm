package utils.log;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import utils.log.Logger;
import utils.log.LoggerProvider;
import utils.log.impl.NOPLogger;

public final class LoggerFactory {
	private static Function<String, Logger> loggerProvider;
	private static Map<String, Logger> loggers;

	public static void setLoggerProvider(LoggerProvider loggerProvider) {
		LoggerFactory.loggerProvider = loggerProvider::createLogger;
		if (loggers != null) {
			loggers.clear();
		}

	}

	public static Logger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	public static void enableCache() {
		loggers = new HashMap();
	}

	public static void disableCache() {
		loggers = null;
	}

	public static Logger getLogger(String name) {
		return loggers == null
				? (Logger) loggerProvider.apply(name)
				: (Logger) loggers.computeIfAbsent(name, loggerProvider);
	}

	static {
		setLoggerProvider(NOPLogger.PROVIDER);
		loggers = new HashMap();
	}
}