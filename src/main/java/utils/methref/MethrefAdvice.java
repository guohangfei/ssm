package utils.methref;

import jodd.proxetta.ProxyAdvice;
import jodd.proxetta.ProxyTarget;

public class MethrefAdvice implements ProxyAdvice {
	String methodName;

	public Object execute() {
		this.methodName = ProxyTarget.targetMethodName();
		Class returnType = ProxyTarget.returnType();
		return returnType == String.class
				? ProxyTarget.returnValue(ProxyTarget.targetMethodName())
				: ProxyTarget.returnValue((Object) null);
	}
}