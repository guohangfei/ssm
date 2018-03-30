package utils.madvoc.injector;

import java.lang.reflect.Constructor;
import jodd.bean.BeanUtil;
import jodd.bean.JoddBean;
import jodd.madvoc.MadvocException;
import jodd.madvoc.config.ScopeData.Out;

public class Target {
	protected final Class type;
	protected Object value;

	public Target(Object value) {
		this.value = value;
		this.type = null;
	}

	public Target(Object value, Class type) {
		this.value = value;
		this.type = type;
	}

	public Target(Class type) {
		this.type = type;
		this.value = null;
	}

	public Class getType() {
		return this.type;
	}

	public Class resolveType() {
		return this.type != null ? this.type : this.value.getClass();
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object readValue(String propertyName) {
		if (this.type != null) {
			int dotNdx = propertyName.indexOf(46);
			if (dotNdx == -1) {
				return this.value;
			}

			propertyName = propertyName.substring(dotNdx + 1);
		}

		return BeanUtil.declared.getProperty(this.value, propertyName);
	}

	public Object readTargetProperty(Out out) {
		return out.target == null ? this.readValue(out.name) : this.readValue(out.target);
	}

	public void writeValue(String propertyName, Object propertyValue, boolean silent) {
		if (this.type != null) {
			int dotNdx = propertyName.indexOf(46);
			if (dotNdx == -1) {
				this.value = JoddBean.defaults().getTypeConverterManager().convertType(propertyValue, this.type);
				return;
			}

			if (this.value == null) {
				this.createValueInstance();
			}

			propertyName = propertyName.substring(dotNdx + 1);
		}

		if (silent) {
			BeanUtil.declaredForcedSilent.setProperty(this.value, propertyName, propertyValue);
		} else {
			BeanUtil.declaredForced.setProperty(this.value, propertyName, propertyValue);
		}
	}

	protected void createValueInstance() {
		try {
			Constructor ex = this.type.getDeclaredConstructor((Class[]) null);
			ex.setAccessible(true);
			this.value = ex.newInstance(new Object[0]);
		} catch (Exception arg1) {
			throw new MadvocException(arg1);
		}
	}
}