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

package utils.petite.scope;

import utils.petite.BeanData;
import utils.petite.BeanDefinition;
import utils.petite.PetiteUtil;
import utils.petite.scope.ProtoScope;
import utils.petite.scope.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton scope pools all bean instances so they will be created only once in
 * the container context.
 */
public class SingletonScope implements utils.petite.scope.Scope {

	protected Map<String, BeanData> instances = new HashMap<>();

	@Override
	public Object lookup(final String name) {
		BeanData beanData = instances.get(name);
		if (beanData == null) {
			return null;
		}
		return beanData.instance();
	}

	@Override
	public void register(final BeanDefinition beanDefinition, final Object bean) {
		BeanData beanData = new BeanData(beanDefinition, bean);
		instances.put(beanDefinition.name(), beanData);
	}

	@Override
	public void remove(final String name) {
		instances.remove(name);
	}

	/**
	 * Allows only singleton scoped beans to be injected into the target singleton bean.
	 */
	@Override
	public boolean accept(final utils.petite.scope.Scope referenceScope) {
		Class<? extends Scope> refScopeType = referenceScope.getClass();

		if (refScopeType == ProtoScope.class) {
			return true;
		}

		if (refScopeType == SingletonScope.class) {
			return true;
		}

		return false;
	}

	/**
	 * Iterate all beans and invokes registered destroy methods.
	 */
	@Override
	public void shutdown() {
		for (BeanData beanData : instances.values()) {
			PetiteUtil.callDestroyMethods(beanData);
		}
		instances.clear();
	}
}
