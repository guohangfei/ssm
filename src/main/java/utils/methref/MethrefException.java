package utils.methref;

import utils.proxetta.ProxettaException;

public class MethrefException extends ProxettaException {
	public MethrefException(Throwable throwable) {
		super(throwable);
	}

	public MethrefException(String string) {
		super(string);
	}

	public MethrefException(String string, Throwable throwable) {
		super(string, throwable);
	}
}