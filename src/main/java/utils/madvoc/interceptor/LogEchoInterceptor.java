package utils.madvoc.interceptor;

import utils.log.Logger;
import utils.log.LoggerFactory;
import utils.madvoc.interceptor.EchoInterceptor;

public class LogEchoInterceptor extends EchoInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LogEchoInterceptor.class);

	protected void out(String message) {
		log.debug(message);
	}
}