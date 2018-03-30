package utils.log;

import utils.log.Logger;

@FunctionalInterface
public interface LoggerProvider<L extends Logger> {
	L createLogger(String arg0);
}