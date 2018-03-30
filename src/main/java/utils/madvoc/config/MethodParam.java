package utils.madvoc.config;

import java.lang.annotation.Annotation;

public class MethodParam {
	private final Class type;
	private final String name;
	private final Class<? extends Annotation> annotationType;

	public MethodParam(Class type, String name, Class<? extends Annotation> annotationType) {
		this.type = type;
		this.name = name;
		this.annotationType = annotationType;
	}

	public Class type() {
		return this.type;
	}

	public String name() {
		return this.name;
	}

	public Class<? extends Annotation> annotationType() {
		return this.annotationType;
	}
}