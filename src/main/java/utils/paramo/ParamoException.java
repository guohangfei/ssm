package utils.paramo;

import jodd.exception.UncheckedException;

public class ParamoException extends UncheckedException {
	public ParamoException(Throwable t) {
		super(t);
	}

	public ParamoException(String message) {
		super(message);
	}

	public ParamoException(String message, Throwable t) {
		super(message, t);
	}
}