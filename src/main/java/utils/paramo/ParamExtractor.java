package utils.paramo;

import jodd.asm.EmptyMethodVisitor;
import jodd.asm6.Label;
import jodd.paramo.MethodParameter;
import jodd.util.ArraysUtil;

final class ParamExtractor extends EmptyMethodVisitor {
	private final int paramCount;
	private final int ignoreCount;
	private MethodParameter[] methodParameters;
	private int currentParam;
	boolean debugInfoPresent;

	ParamExtractor(int ignoreCount, int paramCount) {
		this.ignoreCount = ignoreCount;
		this.paramCount = paramCount;
		this.methodParameters = new MethodParameter[paramCount];
		this.currentParam = 0;
		this.debugInfoPresent = paramCount == 0;
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		if (index >= this.ignoreCount && index < this.ignoreCount + this.paramCount) {
			if (!name.equals("arg" + this.currentParam)) {
				this.debugInfoPresent = true;
			}

			if (signature == null) {
				signature = desc;
			}

			this.methodParameters[this.currentParam] = new MethodParameter(name, signature);
			++this.currentParam;
		}

	}

	public void visitEnd() {
		if (this.methodParameters.length > this.currentParam) {
			this.methodParameters = (MethodParameter[]) ArraysUtil.subarray(this.methodParameters, 0,
					this.currentParam);
		}

	}

	MethodParameter[] getMethodParameters() {
		return this.methodParameters;
	}
}