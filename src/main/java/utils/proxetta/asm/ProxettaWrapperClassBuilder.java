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

import utils.asm.AsmUtil;
import utils.asm6.ClassVisitor;
import utils.asm6.FieldVisitor;
import utils.asm6.MethodVisitor;
import utils.asm6.Opcodes;
import utils.proxetta.ProxyAspect;
import utils.proxetta.asm.MethodSignatureVisitor;
import utils.proxetta.asm.ProxettaAsmUtil;
import utils.proxetta.asm.ProxettaClassBuilder;
import utils.proxetta.asm.ProxettaMethodBuilder;
import utils.proxetta.asm.ProxyAspectData;
import utils.proxetta.asm.TargetClassInfoReader;

import java.util.List;

import static utils.asm6.Opcodes.ACC_ABSTRACT;
import static utils.asm6.Opcodes.ACC_NATIVE;
import static utils.asm6.Opcodes.ALOAD;
import static utils.asm6.Opcodes.GETFIELD;
import static utils.asm6.Opcodes.INVOKEINTERFACE;
import static utils.asm6.Opcodes.INVOKEVIRTUAL;
import static utils.proxetta.asm.ProxettaAsmUtil.CLINIT;
import static utils.proxetta.asm.ProxettaAsmUtil.INIT;
import static utils.proxetta.asm.ProxettaAsmUtil.loadVirtualMethodArguments;
import static utils.proxetta.asm.ProxettaAsmUtil.visitReturn;

public class ProxettaWrapperClassBuilder extends ProxettaClassBuilder {

	protected final Class targetClassOrInterface;
	protected final Class targetInterface;
	protected final String targetFieldName;

	public ProxettaWrapperClassBuilder(
		final Class targetClassOrInterface,
		final Class targetInterface,
		final String targetFieldName,
		final ClassVisitor dest,
		final ProxyAspect[] aspects,
		final String suffix,
		final String reqProxyClassName,
		final TargetClassInfoReader targetClassInfoReader) {

		super(dest, aspects, suffix, reqProxyClassName, targetClassInfoReader);
		this.targetClassOrInterface = targetClassOrInterface;
		this.targetInterface = targetInterface;
		this.targetFieldName = targetFieldName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final int version, int access, final String name, final String signature, final String superName, String[] interfaces) {

		wd.init(name, superName, this.suffix, this.reqProxyClassName);

		// no superclass
		wd.superName = AsmUtil.SIGNATURE_JAVA_LANG_OBJECT;

		// change access of destination
		access &= ~AsmUtil.ACC_ABSTRACT;
		access &= ~AsmUtil.ACC_INTERFACE;

		// write destination class
		if (targetClassOrInterface.isInterface()) {
			// target is interface
			wd.wrapInterface = true;

			interfaces = new String[] {targetClassOrInterface.getName().replace('.', '/')};
		} else {
			// target is class
			wd.wrapInterface = false;

			if (targetInterface != null) {
				// interface provided
				interfaces = new String[] {targetInterface.getName().replace('.', '/')};
			} else {
				// no interface provided, use all
				//interfaces = null;
			}
		}
		wd.dest.visit(version, access, wd.thisReference, signature, wd.superName, interfaces);

		wd.proxyAspects = new utils.proxetta.asm.ProxyAspectData[aspects.length];
		for (int i = 0; i < aspects.length; i++) {
			wd.proxyAspects[i] = new utils.proxetta.asm.ProxyAspectData(wd, aspects[i], i);
		}

		// create new field wrapper field and store it's reference into work-data
		wd.wrapperRef = targetFieldName;
		wd.wrapperType = 'L' + name + ';';
		FieldVisitor fv  = wd.dest.visitField(AsmUtil.ACC_PUBLIC, wd.wrapperRef, wd.wrapperType, null, null);
		fv.visitEnd();

		createEmptyCtor();
	}

	/**
	 * Created empty default constructor.
	 */
	protected void createEmptyCtor() {
		MethodVisitor mv = wd.dest.visitMethod(AsmUtil.ACC_PUBLIC, INIT, "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(
			Opcodes.INVOKESPECIAL,
			AsmUtil.SIGNATURE_JAVA_LANG_OBJECT,
			INIT, "()V",
			false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		utils.proxetta.asm.MethodSignatureVisitor msign = targetClassInfo.lookupMethodSignatureVisitor(access, name, desc, wd.superReference);
		if (msign == null) {
			return null;
		}

		// ignore all destination constructors
		if (name.equals(INIT)) {
			return null;
		}
		// ignore all destination static block
		if (name.equals(CLINIT)) {
			return null;
		}

		return applyProxy(msign);
	}

	@Override
	protected utils.proxetta.asm.ProxettaMethodBuilder applyProxy(final utils.proxetta.asm.MethodSignatureVisitor msign) {
		List<utils.proxetta.asm.ProxyAspectData> aspectList = matchMethodPointcuts(msign);

		if (aspectList == null) {
			wd.proxyApplied = true;
			createSimpleMethodWrapper(msign);
			return null;
		}

		wd.proxyApplied = true;
		return new ProxettaMethodBuilder(msign, wd, aspectList);

	}

	/**
	 * Creates simple method wrapper without proxy.
	 */
	protected void createSimpleMethodWrapper(final MethodSignatureVisitor msign) {

		int access = msign.getAccessFlags();

		access &= ~ACC_ABSTRACT;
		access &= ~ACC_NATIVE;

		MethodVisitor mv = wd.dest.visitMethod(
				access, msign.getMethodName(), msign.getDescription(), msign.getAsmMethodSignature(), msign.getExceptions());
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, wd.thisReference, wd.wrapperRef, wd.wrapperType);
		loadVirtualMethodArguments(mv, msign);

		if (wd.wrapInterface) {
			mv.visitMethodInsn(
				INVOKEINTERFACE,
				wd.wrapperType.substring(1, wd.wrapperType.length() - 1),
				msign.getMethodName(),
				msign.getDescription(),
				true);
		} else {
			mv.visitMethodInsn(
				INVOKEVIRTUAL,
				wd.wrapperType.substring(1, wd.wrapperType.length() - 1),
				msign.getMethodName(),
				msign.getDescription(),
				false);
		}

		ProxettaAsmUtil.prepareReturnValue(mv, msign, 0);
		visitReturn(mv, msign, true);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	@Override
	public void visitEnd() {
		makeStaticInitBlock();

		processSuperMethods();

		wd.dest.visitEnd();

	}
}
