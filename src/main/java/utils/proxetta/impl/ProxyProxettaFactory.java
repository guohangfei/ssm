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

package utils.proxetta.impl;

import utils.asm6.ClassReader;
import utils.proxetta.ProxettaFactory;
import utils.proxetta.ProxyAspect;
import utils.proxetta.asm.ProxettaClassBuilder;
import utils.proxetta.asm.TargetClassInfoReader;
import utils.proxetta.asm.WorkData;
import utils.proxetta.impl.ProxyProxetta;

import java.io.InputStream;

/**
 * Creates the proxy subclass using ASM library.
 */
public class ProxyProxettaFactory extends ProxettaFactory<ProxyProxettaFactory, utils.proxetta.impl.ProxyProxetta> {

	public ProxyProxettaFactory(final ProxyProxetta proxyProxetta) {
		super(proxyProxetta);
	}

	@Override
	public ProxyProxettaFactory setTarget(final InputStream target) {
		return super.setTarget(target);
	}

	@Override
	public ProxyProxettaFactory setTarget(final String targetName) {
		return super.setTarget(targetName);
	}

	@Override
	public ProxyProxettaFactory setTarget(final Class target) {
		return super.setTarget(target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WorkData process(final ClassReader cr, final TargetClassInfoReader targetClassInfoReader) {

		ProxettaClassBuilder pcb = new ProxettaClassBuilder(
				destClassWriter,
				proxetta.getAspects(new ProxyAspect[0]),
				resolveClassNameSuffix(),
				requestedProxyClassName,
				targetClassInfoReader);

		cr.accept(pcb, 0);

		return pcb.getWorkData();
	}

}