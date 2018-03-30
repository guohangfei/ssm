package utils.madvoc.interceptor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jodd.bean.JoddBean;
import jodd.introspector.ClassDescriptor;
import jodd.introspector.PropertyDescriptor;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.interceptor.ActionInterceptor;

public abstract class AnnotatedPropertyInterceptor implements ActionInterceptor {
	protected final Class<Annotation> annotations;
	protected Map<Class<?>, PropertyDescriptor[]> annotatedProperties = new HashMap();
	protected static final PropertyDescriptor[] EMPTY = new PropertyDescriptor[0];

	protected AnnotatedPropertyInterceptor(Class<Annotation> annotations) {
		this.annotations = annotations;
	}

	public Object intercept(ActionRequest actionRequest) throws Exception {
		Object action = actionRequest.getAction();
		Class actionType = action.getClass();
		PropertyDescriptor[] allProperties = this.lookupAnnotatedProperties(actionType);
		PropertyDescriptor[] arg4 = allProperties;
		int arg5 = allProperties.length;

		for (int arg6 = 0; arg6 < arg5; ++arg6) {
			PropertyDescriptor propertyDescriptor = arg4[arg6];
			this.onAnnotatedProperty(actionRequest, propertyDescriptor);
		}

		return actionRequest.invoke();
	}

	protected abstract void onAnnotatedProperty(ActionRequest arg0, PropertyDescriptor arg1);

	protected PropertyDescriptor[] lookupAnnotatedProperties(Class type) {
		PropertyDescriptor[] properties = (PropertyDescriptor[]) this.annotatedProperties.get(type);
		if (properties != null) {
			return properties;
		} else {
			ClassDescriptor cd = JoddBean.defaults().getClassIntrospector().lookup(type);
			PropertyDescriptor[] allProperties = cd.getAllPropertyDescriptors();
			ArrayList list = new ArrayList();
			PropertyDescriptor[] arg5 = allProperties;
			int arg6 = allProperties.length;

			for (int arg7 = 0; arg7 < arg6; ++arg7) {
				PropertyDescriptor propertyDescriptor = arg5[arg7];
				Annotation ann = null;
				if (propertyDescriptor.getFieldDescriptor() != null) {
					ann = propertyDescriptor.getFieldDescriptor().getField().getAnnotation(this.annotations);
				}

				if (ann == null && propertyDescriptor.getWriteMethodDescriptor() != null) {
					ann = propertyDescriptor.getWriteMethodDescriptor().getMethod().getAnnotation(this.annotations);
				}

				if (ann == null && propertyDescriptor.getReadMethodDescriptor() != null) {
					ann = propertyDescriptor.getReadMethodDescriptor().getMethod().getAnnotation(this.annotations);
				}

				if (ann != null) {
					list.add(propertyDescriptor);
				}
			}

			if (list.isEmpty()) {
				properties = EMPTY;
			} else {
				properties = (PropertyDescriptor[]) list.toArray(new PropertyDescriptor[list.size()]);
			}

			this.annotatedProperties.put(type, properties);
			return properties;
		}
	}
}