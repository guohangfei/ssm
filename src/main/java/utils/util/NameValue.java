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

package utils.util;

/**
 * Simple name-value holder.
 */
public class NameValue<N, V> {

	protected final N name;
	protected final V value;

	/**
	 * Simple static constructor.
	 */
	public static <T,R> NameValue<T,R> of(final T name, final R value) {
		return new NameValue<>(name, value);
	}

	public NameValue(final N name, final V value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns name.
	 */
	public N name() {
		return name;
	}

	/**
	 * Returns value.
	 */
	public V value() {
		return value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		if (this == o) {
			return true;
		}

		NameValue that = (NameValue) o;

		Object n1 = name();
		Object n2 = that.name();

		if (n1 == n2 || (n1 != null && n1.equals(n2))) {
			Object v1 = value();
			Object v2 = that.value();
			if (v1 == v2 || (v1 != null && v1.equals(v2))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (name == null ? 0 : name.hashCode()) ^
				(value == null ? 0 : value.hashCode());
	}

}