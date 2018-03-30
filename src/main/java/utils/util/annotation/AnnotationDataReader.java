package utils.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import jodd.util.ClassUtil;
import jodd.util.StringUtil;
import jodd.util.annotation.AnnotationData;

public abstract class AnnotationDataReader<A extends Annotation, D extends AnnotationData<A>> {
	protected final Annotation defaultAnnotation;
	protected final Class<A> annotationClass;

	protected AnnotationDataReader(Class<A> annotationClass, Class<? extends Annotation> defaultAnnotationClass) {
		if (annotationClass == null) {
			Class[] defaultAnnotation = ClassUtil.getGenericSupertypes(this.getClass());
			if (defaultAnnotation != null) {
				annotationClass = defaultAnnotation[0];
			}

			if (annotationClass == null || annotationClass == Annotation.class) {
				throw new IllegalArgumentException("Missing annotation from generics supertype");
			}
		}

		this.annotationClass = annotationClass;
		if (defaultAnnotationClass != null && defaultAnnotationClass != annotationClass) {
			Annotation defaultAnnotation1 = annotationClass.getAnnotation(defaultAnnotationClass);
			if (defaultAnnotation1 == null) {
				try {
					defaultAnnotation1 = (Annotation) defaultAnnotationClass.getDeclaredConstructor(new Class[0])
							.newInstance(new Object[0]);
				} catch (Exception arg4) {
					;
				}
			}

			this.defaultAnnotation = defaultAnnotation1;
		} else {
			this.defaultAnnotation = null;
		}

	}

	public Class<A> getAnnotationClass() {
		return this.annotationClass;
	}

	public boolean hasAnnotationOn(AnnotatedElement annotatedElement) {
		return annotatedElement.isAnnotationPresent(this.annotationClass);
	}

	public D readAnnotatedElement(AnnotatedElement annotatedElement) {
		Annotation annotation = annotatedElement.getAnnotation(this.annotationClass);
		return annotation == null ? null : this.createAnnotationData(annotation);
	}

	protected abstract D createAnnotationData(A arg0);

	protected String readStringElement(A annotation, String name) {
		Object annotationValue = ClassUtil.readAnnotationValue(annotation, name);
		if (annotationValue == null) {
			if (this.defaultAnnotation == null) {
				return null;
			}

			annotationValue = ClassUtil.readAnnotationValue(this.defaultAnnotation, name);
			if (annotationValue == null) {
				return null;
			}
		}

		String value = StringUtil.toSafeString(annotationValue);
		return value.trim();
	}

	protected Object readElement(A annotation, String name) {
		Object annotationValue = ClassUtil.readAnnotationValue(annotation, name);
		if (annotationValue == null && this.defaultAnnotation != null) {
			annotationValue = ClassUtil.readAnnotationValue(this.defaultAnnotation, name);
		}

		return annotationValue;
	}

	protected String readString(A annotation, String name, String defaultValue) {
		String value = this.readStringElement(annotation, name);
		if (StringUtil.isEmpty(value)) {
			value = defaultValue;
		}

		return value;
	}

	protected boolean readBoolean(A annotation, String name, boolean defaultValue) {
		Boolean value = (Boolean) this.readElement(annotation, name);
		return value == null ? defaultValue : value.booleanValue();
	}

	protected int readInt(A annotation, String name, int defaultValue) {
		Integer value = (Integer) this.readElement(annotation, name);
		return value == null ? defaultValue : value.intValue();
	}
}