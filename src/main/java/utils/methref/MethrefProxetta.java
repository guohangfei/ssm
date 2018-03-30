package utils.methref;

import jodd.methref.MethrefAdvice;
import jodd.proxetta.Proxetta;
import jodd.proxetta.ProxyAspect;
import jodd.proxetta.impl.ProxyProxetta;
import jodd.proxetta.impl.ProxyProxettaFactory;
import jodd.proxetta.pointcuts.AllMethodsPointcut;

public class MethrefProxetta {
	protected final ProxyProxetta proxetta;
	public static final String METHREF_CLASSNAME_SUFFIX = "$Methref";

	public MethrefProxetta() {
		ProxyAspect aspects = new ProxyAspect(MethrefAdvice.class, new AllMethodsPointcut());
		this.proxetta = (ProxyProxetta) Proxetta.proxyProxetta().withAspect(aspects);
		this.proxetta.setClassNameSuffix("$Methref");
	}

	public Class defineProxy(Class target) {
		ProxyProxettaFactory builder = this.proxetta.proxy();
		builder.setTarget(target);
		return builder.define();
	}
}