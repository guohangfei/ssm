// Copyright (c) 2003-present, utils Team (http://utils.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package utils.proxetta.asm;

import utils.asm.AnnotationVisitorAdapter;
import utils.asm.AsmUtil;
import utils.asm.EmptyClassVisitor;
import utils.asm.EmptyMethodVisitor;
import utils.asm6.AnnotationVisitor;
import utils.asm6.MethodVisitor;
import utils.proxetta.utilsProxetta;
import utils.proxetta.ProxettaException;
import utils.proxetta.ProxyTarget;
import utils.proxetta.ProxyTargetReplacement;
import utils.proxetta.asm.HistoryMethodAdapter;
import utils.proxetta.asm.MethodSignatureVisitor;
import utils.proxetta.asm.ProxettaAsmUtil;
import utils.proxetta.asm.ProxyAspectData;
import utils.proxetta.asm.TargetMethodData;
import utils.proxetta.asm.WorkData;

import java.util.List;

import static utils.asm6.Opcodes.ACC_ABSTRACT;
import static utils.asm6.Opcodes.ACC_NATIVE;
import static utils.asm6.Opcodes.ALOAD;
import static utils.asm6.Opcodes.ARETURN;
import static utils.asm6.Opcodes.ASTORE;
import static utils.asm6.Opcodes.GETFIELD;
import static utils.asm6.Opcodes.INVOKEINTERFACE;
import static utils.asm6.Opcodes.INVOKESPECIAL;
import static utils.asm6.Opcodes.INVOKESTATIC;
import static utils.asm6.Opcodes.INVOKEVIRTUAL;
import static utils.asm6.Opcodes.POP;
import static utils.asm6.Opcodes.POP2;
import static utils.proxetta.asm.ProxettaAsmUtil.adviceFieldName;
import static utils.proxetta.asm.ProxettaAsmUtil.adviceMethodName;
import static utils.proxetta.asm.ProxettaAsmUtil.castToReturnType;
import static utils.proxetta.asm.ProxettaAsmUtil.checkArgumentIndex;
import static utils.proxetta.asm.ProxettaAsmUtil.isArgumentMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isArgumentTypeMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isArgumentsCountMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isCreateArgumentsArrayMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isCreateArgumentsClassArrayMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isInfoMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isInvokeMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isReturnTypeMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isReturnValueMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isSetArgumentMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetClassAnnotationMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetClassMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetMethodAnnotationMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetMethodDescriptionMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetMethodNameMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.isTargetMethodSignatureMethod;
import static utils.proxetta.asm.ProxettaAsmUtil.loadSpecialMethodArguments;
import static utils.proxetta.asm.ProxettaAsmUtil.loadStaticMethodArguments;
import static utils.proxetta.asm.ProxettaAsmUtil.loadVirtualMethodArguments;
import static utils.proxetta.asm.ProxettaAsmUtil.prepareReturnValue;
import static utils.proxetta.asm.ProxettaAsmUtil.storeMethodArgumentFromObject;
import static utils.proxetta.asm.ProxettaAsmUtil.visitReturn;

@SuppressWarnings({"AnonymousClassVariableHidesContainingMethodVariable"})
public class ProxettaMethodBuilder extends EmptyMethodVisitor {

	public static final String TARGET_CLASS_NAME = ProxyTarget.class.getSimpleName();        // extract ProxyTarget name for recognition

	protected final MethodSignatureVisitor msign;
	protected final utils.proxetta.asm.WorkData wd;
	protected final List<utils.proxetta.asm.ProxyAspectData> aspectList;

	public ProxettaMethodBuilder(final MethodSignatureVisitor msign, final WorkData wd, final List<utils.proxetta.asm.ProxyAspectData> aspectList) {
		this.msign = msign;
		this.wd = wd;
		this.aspectList = aspectList;
		createFirstChainDelegate_Start();
	}

	// ---------------------------------------------------------------- visits

	/**
	 * Copies target method annotations.
	 */
	@Override
	public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
		AnnotationVisitor destAnn = methodVisitor.visitAnnotation(desc, visible); // [A4]
		return new AnnotationVisitorAdapter(destAnn);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		AnnotationVisitor destAnn = methodVisitor.visitAnnotationDefault();
		return new AnnotationVisitorAdapter(destAnn);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
		AnnotationVisitor destAnn = methodVisitor.visitParameterAnnotation(parameter, desc, visible);
		return new AnnotationVisitorAdapter(destAnn);
	}


	/**
	 * Finally, builds proxy methods if applied to current method.
	 */
	@Override
	public void visitEnd() {
		createFirstChainDelegate_Continue(tmd);
		for (int p = 0; p < tmd.proxyData.length; p++) {
			tmd.selectCurrentProxy(p);
			createProxyMethod(tmd);
		}
	}


	// ---------------------------------------------------------------- creating

	protected utils.proxetta.asm.TargetMethodData tmd;
	protected MethodVisitor methodVisitor;

	/**
	 * Starts creation of first chain delegate.
	 */
	protected void createFirstChainDelegate_Start() {
		// check invalid access flags
		int access = msign.getAccessFlags();
		if ((access & AsmUtil.ACC_FINAL) != 0) {   // detect final
			throw new ProxettaException("Unable to create proxy for final method: " + msign +". Remove final modifier or change the pointcut definition.");
		}

		// create proxy methods
		tmd = new utils.proxetta.asm.TargetMethodData(msign, aspectList);

		access &= ~ACC_NATIVE;
		access &= ~ACC_ABSTRACT;

		methodVisitor = wd.dest.visitMethod(
				access, tmd.msign.getMethodName(), tmd.msign.getDescription(), tmd.msign.getAsmMethodSignature(), null);
	}

	/**
	 * Continues the creation of the very first method in calling chain that simply delegates invocation to the first proxy method.
	 * This method mirrors the target method.
	 */
	protected void createFirstChainDelegate_Continue(final utils.proxetta.asm.TargetMethodData tmd) {
		methodVisitor.visitCode();

		if (tmd.msign.isStatic) {
			loadStaticMethodArguments(methodVisitor, tmd.msign);
			methodVisitor.visitMethodInsn(
				INVOKESTATIC,
				wd.thisReference,
				tmd.firstMethodName(),
				tmd.msign.getDescription(),
				false);
		} else {
			loadSpecialMethodArguments(methodVisitor, tmd.msign);
			methodVisitor.visitMethodInsn(
				INVOKESPECIAL,
				wd.thisReference,
				tmd.firstMethodName(),
				tmd.msign.getDescription(),
				false);
		}

		visitReturn(methodVisitor, tmd.msign, false);

		methodVisitor.visitMaxs(0, 0);
		methodVisitor.visitEnd();
	}

	protected boolean proxyInfoRequested;

	/**
	 * Creates proxy methods over target method, For each matched proxy, new proxy method is created
	 * by taking advice bytecode and replaces usages of {@link ProxyTarget}.
	 * <p>
	 * Invocation chain example: {@code name -> name$p0 -> name$p1 -> name$p4 -> super}.
	 */
	public void createProxyMethod(final utils.proxetta.asm.TargetMethodData td) {
		final utils.proxetta.asm.ProxyAspectData aspectData = td.getProxyData();

		int access = td.msign.getAccessFlags();

		access &= ~ACC_NATIVE;
		access &= ~ACC_ABSTRACT;
		access = ProxettaAsmUtil.makePrivateFinalAccess(access);

		final MethodVisitor mv = wd.dest.visitMethod(access, td.methodName(), td.msign.getDescription(), null, null);
		mv.visitCode();

		//*** VISIT ADVICE - called for each aspect and each method
		aspectData.getAdviceClassReader().accept(new EmptyClassVisitor() {

			@Override
			public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {

				if (!name.equals(utilsProxetta.defaults().getExecuteMethodName())) {
					return null;
				}

				return new utils.proxetta.asm.HistoryMethodAdapter(mv) {

					@Override
					public void visitFieldInsn(final int opcode, String owner, String name, final String desc) {
						if (owner.equals(aspectData.adviceReference)) {
							owner = wd.thisReference;              // [F5]
							name = adviceFieldName(name, aspectData.aspectIndex);
						}
						super.visitFieldInsn(opcode, owner, name, desc);
					}


					@Override
					public void visitVarInsn(final int opcode, int var) {
						var += (var == 0 ? 0 : td.msign.getAllArgumentsSize());

						if (proxyInfoRequested) {
							proxyInfoRequested = false;
							if (opcode == ASTORE) {
								ProxyTargetReplacement.info(mv, td.msign, var);
							}
						}

						super.visitVarInsn(opcode, var);   // [F1]
					}

					@Override
					public void visitIincInsn(int var, final int increment) {
						var += (var == 0 ? 0 : td.msign.getAllArgumentsSize());
						super.visitIincInsn(var, increment);  // [F1]
					}

					@Override
					public void visitInsn(final int opcode) {
						if (opcode == ARETURN) {
							visitReturn(mv, td.msign, true);
							return;
						}
						if (traceNext) {
							if ((opcode == POP) || (opcode == POP2)) {      // [F3] - invoke invoked without assignment
								return;
							}
						}
						super.visitInsn(opcode);
					}

					@SuppressWarnings({"ParameterNameDiffersFromOverriddenParameter"})
					@Override
					public void visitMethodInsn(final int opcode, String string, String mname, final String mdesc, final boolean isInterface) {
						if ((opcode == INVOKEVIRTUAL) || (opcode == INVOKEINTERFACE) || (opcode == INVOKESPECIAL)) {
							if (string.equals(aspectData.adviceReference)) {
								string = wd.thisReference;
								mname = adviceMethodName(mname, aspectData.aspectIndex);
							}
						} else

						if (opcode == INVOKESTATIC) {
							if (string.equals(aspectData.adviceReference)) {
								string = wd.thisReference;
								mname = adviceMethodName(mname, aspectData.aspectIndex);
							} else

							if (string.endsWith('/' + TARGET_CLASS_NAME)) {

								if (isInvokeMethod(mname, mdesc)) {           // [R7]
									if (td.isLastMethodInChain()) {                            // last proxy method just calls super target method

										if (!wd.isWrapper()) {
											// PROXY
											loadSpecialMethodArguments(mv, td.msign);
											mv.visitMethodInsn(INVOKESPECIAL, wd.superReference, td.msign.getMethodName(), td.msign.getDescription(), isInterface);
										} else {
											// WRAPPER
											mv.visitVarInsn(ALOAD, 0);
											mv.visitFieldInsn(GETFIELD, wd.thisReference, wd.wrapperRef, wd.wrapperType);
											loadVirtualMethodArguments(mv, td.msign);
											if (wd.wrapInterface) {
												mv.visitMethodInsn(
													INVOKEINTERFACE,
													wd.wrapperType.substring(1, wd.wrapperType.length() - 1),
													td.msign.getMethodName(),
													td.msign.getDescription(),
													true);
											} else {
												mv.visitMethodInsn(
													INVOKEVIRTUAL,
													wd.wrapperType.substring(1, wd.wrapperType.length() - 1),
													td.msign.getMethodName(),
													td.msign.getDescription(),
													isInterface);
											}
										}

										prepareReturnValue(mv, td.msign, aspectData.maxLocalVarOffset);     // [F4]
										traceNext = true;
									} else {                                                    // calls next proxy method
										loadSpecialMethodArguments(mv, td.msign);
										mv.visitMethodInsn(INVOKESPECIAL, wd.thisReference, td.nextMethodName(), td.msign.getDescription(), isInterface);
										visitReturn(mv, td.msign, false);
									}
									return;
								}

								if (isArgumentsCountMethod(mname, mdesc)) {		// [R2]
									ProxyTargetReplacement.argumentsCount(mv, td.msign);
									return;
								}

								if (isArgumentTypeMethod(mname, mdesc)) {      // [R3]
									int argIndex = this.getArgumentIndex();
									ProxyTargetReplacement.argumentType(mv, td.msign, argIndex);
									return;
								}

								if (isArgumentMethod(mname, mdesc)) {           // [R4]
									int argIndex = this.getArgumentIndex();
									ProxyTargetReplacement.argument(mv, td.msign, argIndex);
									return;
								}

								if (isSetArgumentMethod(mname, mdesc)) {           // [R5]
									int argIndex = this.getArgumentIndex();
									checkArgumentIndex(td.msign, argIndex);
									mv.visitInsn(POP);
									storeMethodArgumentFromObject(mv, td.msign, argIndex);
									return;
								}

								if (isCreateArgumentsArrayMethod(mname, mdesc)) {  // [R6]
									ProxyTargetReplacement.createArgumentsArray(mv, td.msign);
									return;
								}

								if (isCreateArgumentsClassArrayMethod(mname, mdesc)) {     // [R11]
									ProxyTargetReplacement.createArgumentsClassArray(mv, td.msign);
									return;
								}

								if (isTargetMethod(mname, mdesc)) {       // [R9.1]
									mv.visitVarInsn(ALOAD, 0);
									return;
								}

								if (isTargetClassMethod(mname, mdesc)) {       // [R9]
									ProxyTargetReplacement.targetClass(mv, td.msign);
									//ProxyTargetReplacement.targetClass(mv, wd.superReference);
									return;
								}

								if (isTargetMethodNameMethod(mname, mdesc)) {  // [R10]
									ProxyTargetReplacement.targetMethodName(mv, td.msign);
									return;
								}

								if (isTargetMethodSignatureMethod(mname, mdesc)) {
									ProxyTargetReplacement.targetMethodSignature(mv, td.msign);
									return;
								}

								if (isTargetMethodDescriptionMethod(mname, mdesc)) {
									ProxyTargetReplacement.targetMethodDescription(mv, td.msign);
									return;
								}

								if (isInfoMethod(mname, mdesc)) {
									// we are NOT replacing info() here! First, we need to figure out
									// what is the operand for the very next ASTORE instructions
									// since we need to create an object and store it in this
									// register - and reuse it, in replacement code.

									//ProxyTargetReplacement.info(mv, td.msign);
									proxyInfoRequested = true;
									return;
								}

								if (isReturnTypeMethod(mname, mdesc)) {        // [R11]
									ProxyTargetReplacement.returnType(mv, td.msign);
									return;
								}

								if (isReturnValueMethod(mname, mdesc)) {
									castToReturnType(mv, td.msign);
									return;
								}

								if (isTargetMethodAnnotationMethod(mname, mdesc)) {
									String[] args = getLastTwoStringArguments();

									// pop current two args
									mv.visitInsn(POP);
									mv.visitInsn(POP);

									ProxyTargetReplacement.targetMethodAnnotation(mv, td.msign, args);
									return;
								}

								if (isTargetClassAnnotationMethod(mname, mdesc)) {
									String[] args = getLastTwoStringArguments();

									// pop current two args
									mv.visitInsn(POP);
									mv.visitInsn(POP);

									ProxyTargetReplacement.targetClassAnnotation(mv, td.msign.getClassInfo(), args);
									return;
								}
							}
						}
						super.visitMethodInsn(opcode, string, mname, mdesc, isInterface);
					}

				};
			}

		}, 0);
	}
}