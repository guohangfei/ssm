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

package utils.proxetta;

/**
 * Invocation advice represents a method that will replace some invocation pointcut.
 */
public class InvokeReplacer {

	public static final InvokeReplacer NONE = new InvokeReplacer();

	protected final String owner;
	protected final String methodName;

	public InvokeReplacer(final Class target, final String methodName) {
		this(target.getCanonicalName(), methodName);
	}

	public InvokeReplacer(final String classCanonicalName, final String methodName) {
		this.owner = classCanonicalName.replace('.', '/');
		this.methodName = methodName;
	}

	InvokeReplacer() {
		this.owner = null;
		this.methodName = null;
	}

	public static InvokeReplacer with(final String classCanonicalName, final String methodName) {
		return new InvokeReplacer(classCanonicalName, methodName);
	}

	public static InvokeReplacer with(final Class target, final String methodName) {
		return new InvokeReplacer(target, methodName);
	}

	// ---------------------------------------------------------------- getters

	public String getOwner() {
		return owner;
	}

	public String getMethodName() {
		return methodName;
	}

	// ---------------------------------------------------------------- settings

	protected boolean passOwnerName;
	protected boolean passMethodName;
	protected boolean passMethodSignature;
	protected boolean passThis;
	protected boolean passTargetClass;

	public boolean isPassOwnerName() {
		return passOwnerName;
	}

	public void setPassOwnerName(final boolean passOwnerName) {
		this.passOwnerName = passOwnerName;
	}

	public InvokeReplacer passOwnerName(final boolean passOwnerName) {
		this.passOwnerName = passOwnerName;
		return this;
	}

	public boolean isPassMethodName() {
		return passMethodName;
	}

	public void setPassMethodName(final boolean passMethodName) {
		this.passMethodName = passMethodName;
	}

	public InvokeReplacer passMethodName(final boolean passMethodName) {
		this.passMethodName = passMethodName;
		return this;
	}

	public boolean isPassMethodSignature() {
		return passMethodSignature;
	}

	public void setPassMethodSignature(final boolean passMethodSignature) {
		this.passMethodSignature = passMethodSignature;
	}

	public InvokeReplacer passMethodSignature(final boolean passMethodSignature) {
		this.passMethodSignature = passMethodSignature;
		return this;
	}

	public boolean isPassThis() {
		return passThis;
	}

	public void setPassThis(final boolean passThis) {
		this.passThis = passThis;
	}

	public InvokeReplacer passThis(final boolean passThis) {
		this.passThis = passThis;
		return this;
	}

	public boolean isPassTargetClass() {
		return passTargetClass;
	}

	public void setPassTargetClass(final boolean passTargetClass) {
		this.passTargetClass = passTargetClass;
	}

	public InvokeReplacer passTargetClass(final boolean passTargetClass) {
		this.passTargetClass = passTargetClass;
		return this;
	}

	/**
	 * Returns <code>true</code> if this <code>InvokeReplaces</code> is {@link #NONE}.
	 */
	public boolean isNone() {
		return owner == null;
	}
}
