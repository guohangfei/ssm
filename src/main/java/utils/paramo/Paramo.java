package utils.paramo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import jodd.asm6.ClassReader;
import jodd.io.StreamUtil;
import jodd.paramo.MethodFinder;
import jodd.paramo.MethodParameter;
import jodd.paramo.ParamoException;
import jodd.util.ClassLoaderUtil;

public class Paramo {
	protected static final String CTOR_METHOD = "<init>";

	public static MethodParameter[] resolveParameters(AccessibleObject methodOrCtor) {
		Class[] paramTypes;
		Class declaringClass;
		String name;
		if (methodOrCtor instanceof Method) {
			Method stream = (Method) methodOrCtor;
			paramTypes = stream.getParameterTypes();
			name = stream.getName();
			declaringClass = stream.getDeclaringClass();
		} else {
			Constructor stream1 = (Constructor) methodOrCtor;
			paramTypes = stream1.getParameterTypes();
			declaringClass = stream1.getDeclaringClass();
			name = "<init>";
		}

		if (paramTypes.length == 0) {
			return MethodParameter.EMPTY_ARRAY;
		} else {
			InputStream stream2;
			try {
				stream2 = ClassLoaderUtil.getClassAsStream(declaringClass);
			} catch (IOException arg13) {
				throw new ParamoException("Failed to read class bytes: " + declaringClass.getName(), arg13);
			}

			if (stream2 == null) {
				throw new ParamoException("Class not found: " + declaringClass);
			} else {
				MethodParameter[] arg6;
				try {
					ClassReader ioex = new ClassReader(stream2);
					MethodFinder visitor = new MethodFinder(declaringClass, name, paramTypes);
					ioex.accept(visitor, 0);
					arg6 = visitor.getResolvedParameters();
				} catch (IOException arg11) {
					throw new ParamoException(arg11);
				} finally {
					StreamUtil.close(stream2);
				}

				return arg6;
			}
		}
	}
}