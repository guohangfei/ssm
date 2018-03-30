package utils.paramo;

public class MethodParameter {
	public static final MethodParameter[] EMPTY_ARRAY = new MethodParameter[0];
	protected final String name;
	protected final String signature;

	public MethodParameter(String name, String signature) {
		this.name = name;
		this.signature = signature;
	}

	public String getName() {
		return this.name;
	}

	public String getSignature() {
		return this.signature;
	}
}