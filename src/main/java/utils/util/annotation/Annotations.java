package utils.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Annotations<A extends Annotation> {
	private final Class<A> annotationClass;
	private final List<A> annotations = new ArrayList();

	public Annotations(Class<A> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public static <T extends Annotation> Annotations<T> of(Class<T> annotationClass) {
		return new Annotations(annotationClass);
	}

	public Annotations onMethod(Method method) {
		Annotation a = method.getAnnotation(this.annotationClass);
		if (a != null) {
			this.annotations.add(a);
		}

		return this;
	}

	public Annotations<A> onClass(Class type) {
		Annotation a = type.getAnnotation(this.annotationClass);
		if (a != null) {
			this.annotations.add(a);
		}

		return this;
	}

	public Annotations<A> onPackageHierarchyOf(Class type) {
		return this.onPackageHierarchy(type.getPackage());
	}

	public Annotations<A> onPackageHierarchy(Package pck) {
		String packageName = pck.getName();

		while (true) {
			if (pck != null) {
				Annotation ndx = pck.getAnnotation(this.annotationClass);
				if (ndx != null) {
					this.annotations.add(ndx);
				}
			}

			int ndx1 = packageName.lastIndexOf(46);
			if (ndx1 == -1) {
				return this;
			}

			packageName = packageName.substring(0, ndx1);
			pck = Package.getPackage(packageName);
		}
	}

	public List<A> collect() {
		return this.annotations;
	}
}