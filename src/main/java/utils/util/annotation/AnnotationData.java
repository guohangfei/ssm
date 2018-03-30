package utils.util.annotation;

import java.lang.annotation.Annotation;

public abstract class AnnotationData<N extends Annotation> {
	protected final N annotation;

	protected AnnotationData(N annotation) {
		this.annotation = annotation;
	}

	public N annotation() {
		return this.annotation;
	}
}