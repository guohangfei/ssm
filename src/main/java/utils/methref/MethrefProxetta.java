package utils.methref;

import utils.methref.MethrefAdvice;
import utils.proxetta.Proxetta;
import utils.proxetta.ProxyAspect;
import utils.proxetta.impl.ProxyProxetta;
import utils.proxetta.impl.ProxyProxettaFactory;
import utils.proxetta.pointcuts.AllMethodsPointcut;

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