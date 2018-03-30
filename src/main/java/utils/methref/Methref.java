package utils.methref;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;
import jodd.methref.MethrefException;
import jodd.methref.MethrefProxetta;
import jodd.proxetta.ProxettaUtil;
import jodd.util.ClassUtil;

public class Methref<C> {
	private static final MethrefProxetta proxetta = new MethrefProxetta();
	private static final Map<Class, Class> cache = new WeakHashMap();
	private final C instance;

	public Methref(Class<C> target) {
		target = ProxettaUtil.getTargetClass(target);
		Class proxyClass = (Class) cache.get(target);
		if (proxyClass == null) {
			proxyClass = proxetta.defineProxy(target);
			cache.put(target, proxyClass);
		}

		Object proxy;
		try {
			proxy = ClassUtil.newInstance(proxyClass);
		} catch (Exception arg4) {
			throw new MethrefException(arg4);
		}

		this.instance = proxy;
	}

	public static <T> Methref<T> on(Class<T> target) {
		return new Methref(target);
	}

	public static <T> T onto(Class<T> target) {
		return (new Methref(target)).to();
	}

	public C to() {
		return this.instance;
	}

	public String ref(int dummy) {
		return this.ref((Object) null);
	}

	public String ref(short dummy) {
		return this.ref((Object) null);
	}

	public String ref(byte dummy) {
		return this.ref((Object) null);
	}

	public String ref(char dummy) {
		return this.ref((Object) null);
	}

	public String ref(long dummy) {
		return this.ref((Object) null);
	}

	public String ref(float dummy) {
		return this.ref((Object) null);
	}

	public String ref(double dummy) {
		return this.ref((Object) null);
	}

	public String ref(boolean dummy) {
		return this.ref((Object) null);
	}

	public String ref(Object dummy) {
		if (dummy != null) {
			if (dummy instanceof String) {
				return (String) dummy;
			} else {
				throw new MethrefException("Target method not collected");
			}
		} else {
			return this.ref();
		}
	}

	public String ref() {
		if (this.instance == null) {
			return null;
		} else {
			try {
				Field ex = this.instance.getClass().getDeclaredField("$__methodName$0");
				ex.setAccessible(true);
				Object name = ex.get(this.instance);
				if (name == null) {
					throw new MethrefException("Target method not collected");
				} else {
					return name.toString();
				}
			} catch (Exception arg2) {
				if (arg2 instanceof MethrefException) {
					throw (MethrefException) arg2;
				} else {
					throw new MethrefException("Methref field not found", arg2);
				}
			}
		}
	}
}