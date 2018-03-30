package utils.methref;

import utils.proxetta.ProxyAdvice;
import utils.proxetta.ProxyTarget;

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