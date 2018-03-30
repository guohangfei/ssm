package utils.lagarto;

import utils.exception.UncheckedException;

public class LagartoException extends UncheckedException {
	public LagartoException(Throwable t) {
		super("Parsing error.", t);
	}

	public LagartoException(String message) {
		super(message);
	}
}