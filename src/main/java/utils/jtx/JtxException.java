package utils.jtx;

import utils.exception.UncheckedException;

public class JtxException extends UncheckedException {
	public JtxException(Throwable t) {
		super(t);
	}

	public JtxException(String message) {
		super(message);
	}

	public JtxException(String message, Throwable t) {
		super(message, t);
	}
}