package utils.log;

import jodd.log.Logger;

@FunctionalInterface
public interface LoggerProvider<L extends Logger> {
	L createLogger(String arg0);
}