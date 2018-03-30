// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
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
 * Jodd PROXETTA module.
 */
public class JoddProxetta {

	private static final JoddProxetta instance = new JoddProxetta();

	public static JoddProxetta defaults() {
		return instance;
	}

	// ---------------------------------------------------------------- instance

	private String executeMethodName = "execute";
	private String proxyClassNameSuffix = "$$Proxetta";
	private String invokeProxyClassNameSuffix = "$$Clonetou";
	private String wrapperClassNameSuffix = "$$Wraporetto";
	private String methodPrefix = "$__";
	private String methodDivider = "$";
	private String clinitMethodName = "$clinit";
	private String initMethodName = "$init";
	private String fieldPrefix = "$__";
	private String fieldDivider = "$";
	private String wrapperTargetFieldName = "_target";

	/**
	 * @see #setExecuteMethodName(String)
	 */
	public String getExecuteMethodName() {
		return executeMethodName;
	}

	/**
	 * {@link jodd.proxetta.ProxyAdvice#execute()}
	 */
	public void setExecuteMethodName(final String executeMethodName) {
		this.executeMethodName = executeMethodName;
	}

	/**
	 * Returns proxy class name suffix.
	 */
	public String getProxyClassNameSuffix() {
		return proxyClassNameSuffix;
	}

	/**
	 * Defines proxy class name suffix.
	 */
	public void setProxyClassNameSuffix(final String proxyClassNameSuffix) {
		this.proxyClassNameSuffix = proxyClassNameSuffix;
	}

	/**
	 * Returns invoke proxy class name suffix.
	 */
	public String getInvokeProxyClassNameSuffix() {
		return invokeProxyClassNameSuffix;
	}

	/**
	 * Defines invoke proxy class name suffix.
	 */
	public void setInvokeProxyClassNameSuffix(final String invokeProxyClassNameSuffix) {
		this.invokeProxyClassNameSuffix = invokeProxyClassNameSuffix;
	}

	/**
	 * Returns wrapper class name suffix.
	 */
	public String getWrapperClassNameSuffix() {
		return wrapperClassNameSuffix;
	}

	/**
	 * Defines wrapper class name suffix.
	 */
	public void setWrapperClassNameSuffix(final String wrapperClassNameSuffix) {
		this.wrapperClassNameSuffix = wrapperClassNameSuffix;
	}

	/**
	 * Returns prefix for advice method names.
	 */
	public String getMethodPrefix() {
		return methodPrefix;
	}

	/**
	 * Defines prefix for advice method names.
	 */
	public void setMethodPrefix(final String methodPrefix) {
		this.methodPrefix = methodPrefix;
	}

	/**
	 * Returns divider for method names.
	 */
	public String getMethodDivider() {
		return methodDivider;
	}

	/**
	 * Defines divider for method names.
	 */
	public void setMethodDivider(final String methodDivider) {
		this.methodDivider = methodDivider;
	}

	/**
	 * Returns method name for advice 'clinit' methods.
	 */
	public String getClinitMethodName() {
		return clinitMethodName;
	}

	/**
	 * Defines method name for advice 'clinit' methods.
	 */
	public void setClinitMethodName(final String clinitMethodName) {
		this.clinitMethodName = clinitMethodName;
	}

	/**
	 * Returns method name for advice default constructor ('init') methods.
	 */
	public String getInitMethodName() {
		return initMethodName;
	}

	/**
	 * Defines method name for advice default constructor ('init') methods.
	 */
	public void setInitMethodName(final String initMethodName) {
		this.initMethodName = initMethodName;
	}

	/**
	 * Returns prefix for advice field names.
	 */
	public String getFieldPrefix() {
		return fieldPrefix;
	}

	/**
	 * Defines prefix for advice field names.
	 */
	public void setFieldPrefix(final String fieldPrefix) {
		this.fieldPrefix = fieldPrefix;
	}

	/**
	 * Returns divider for field names.
	 */
	public String getFieldDivider() {
		return fieldDivider;
	}
	/**
	 * Defines divider for field names.
	 */
	public void setFieldDivider(final String fieldDivider) {
		this.fieldDivider = fieldDivider;
	}

	/**
	 * Defines wrapper target field name.
	 */
	public String getWrapperTargetFieldName() {
		return wrapperTargetFieldName;
	}

	/**
	 * Defines wrapper target field name.
	 */
	public void setWrapperTargetFieldName(final String wrapperTargetFieldName) {
		this.wrapperTargetFieldName = wrapperTargetFieldName;
	}

}