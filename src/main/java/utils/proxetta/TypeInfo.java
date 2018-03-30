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

import utils.proxetta.AnnotationInfo;

/**
 * Holds information about the types. Used for methods return type and arguments.
 */
public interface TypeInfo {

	/**
	 * Returns annotation info of type. SOme types (like for returning value)
	 * can't have annotation information.
	 */
	AnnotationInfo[] getAnnotations();

	/**
	 * Returns bytecode opcode.
	 */
	char getOpcode();

	/**
	 * Returns java-like, e.g. {@code "java.lang.Integer"}.
	 */
	String getType();

	/**
	 * Returns bytecode-like type name, e.g. {@code "Ljava/lang/Integer;"}.
	 * Note that generics type names are <b>not</b> resolved.
	 * @see #getRawName()
	 */
	String getName();

	/**
	 * Returns bytecode-like type name, e.g. {@code "Ljava/lang/Integer;"}.
	 * @see #getName()
	 */
	String getRawName();
}
