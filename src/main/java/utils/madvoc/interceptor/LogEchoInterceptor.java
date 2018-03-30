package utils.madvoc.interceptor;

import jodd.log.Logger;
import jodd.log.LoggerFactory;
import jodd.madvoc.interceptor.EchoInterceptor;

public class LogEchoInterceptor extends EchoInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LogEchoInterceptor.class);

	protected void out(String message) {
		log.debug(message);
	}
}