package utils.paramo;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import utils.asm.EmptyClassVisitor;
import utils.asm6.MethodVisitor;
import utils.asm6.Type;
import utils.paramo.MethodParameter;
import utils.paramo.ParamExtractor;
import utils.paramo.ParamoException;

final class MethodFinder extends EmptyClassVisitor {
	private static final Map<String, String> primitives = new HashMap(8);
	private static final String TYPE_INT = "int";
	private static final String TYPE_BOOLEAN = "boolean";
	private static final String TYPE_BYTE = "byte";
	private static final String TYPE_CHAR = "char";
	private static final String TYPE_SHORT = "short";
	private static final String TYPE_FLOAT = "float";
	private static final String TYPE_LONG = "long";
	private static final String TYPE_DOUBLE = "double";
	private static final String ARRAY = "[]";
	private final Class declaringClass;
	private final String methodName;
	private final Class[] parameterTypes;
	private ParamExtractor paramExtractor;

	MethodFinder(Class declaringClass, String methodName, Class[] parameterTypes) {
		this.declaringClass = declaringClass;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.paramExtractor = null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (this.paramExtractor != null) {
			return null;
		} else if (!name.equals(this.methodName)) {
			return null;
		} else {
			Type[] argumentTypes = Type.getArgumentTypes(desc);
			int dwordsCount = 0;
			Type[] paramCount = argumentTypes;
			int i = argumentTypes.length;

			for (int arg9 = 0; arg9 < i; ++arg9) {
				Type t = paramCount[arg9];
				if (t.getClassName().equals("long") || t.getClassName().equals("double")) {
					++dwordsCount;
				}
			}

			int arg11 = argumentTypes.length;
			if (arg11 != this.parameterTypes.length) {
				return null;
			} else {
				for (i = 0; i < argumentTypes.length; ++i) {
					if (!this.isEqualTypeName(argumentTypes[i], this.parameterTypes[i])) {
						return null;
					}
				}

				this.paramExtractor = new ParamExtractor(Modifier.isStatic(access) ? 0 : 1,
						argumentTypes.length + dwordsCount);
				return this.paramExtractor;
			}
		}
	}

	boolean isEqualTypeName(Type argumentType, Class paramType) {
		String s = argumentType.getClassName();
		if (s.endsWith("[]")) {
			String prefix = s.substring(0, s.length() - 2);
			String bytecodeSymbol = (String) primitives.get(prefix);
			if (bytecodeSymbol != null) {
				s = '[' + bytecodeSymbol;
			} else {
				s = "[L" + prefix + ';';
			}
		}

		return s.equals(paramType.getName());
	}

	MethodParameter[] getResolvedParameters() {
		if (this.paramExtractor == null) {
			return MethodParameter.EMPTY_ARRAY;
		} else if (!this.paramExtractor.debugInfoPresent) {
			throw new ParamoException("Parameter names not available for method: " + this.declaringClass.getName() + '#'
					+ this.methodName);
		} else {
			return this.paramExtractor.getMethodParameters();
		}
	}

	static {
		primitives.put("int", "I");
		primitives.put("boolean", "Z");
		primitives.put("char", "C");
		primitives.put("byte", "B");
		primitives.put("float", "F");
		primitives.put("long", "J");
		primitives.put("double", "D");
		primitives.put("short", "S");
	}
}