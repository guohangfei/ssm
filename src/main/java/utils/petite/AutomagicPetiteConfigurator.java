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

package utils.petite;

import utils.io.findfile.ClassScanner;
import utils.log.Logger;
import utils.log.LoggerFactory;
import utils.petite.PetiteContainer;
import utils.petite.PetiteException;
import utils.petite.meta.PetiteBean;
import java.util.function.Consumer;

/**
 * Auto-magically configures Petite container by analyzing the classpath.
 * <p>
 * Scans all classes on classpath and in jar files, and scans for {@link PetiteBean}
 * annotation (not by loading the class!). If annotation is founded, class will be loaded and
 * registered as Petite bean.
 */
public class AutomagicPetiteConfigurator {

	private static final Logger log = LoggerFactory.getLogger(AutomagicPetiteConfigurator.class);
	private final ClassScanner classScanner = new ClassScanner();
	private utils.petite.PetiteContainer container;
	private final byte[] petiteBeanAnnotationBytes;
	protected long elapsed;

	public AutomagicPetiteConfigurator() {
		petiteBeanAnnotationBytes = ClassScanner.bytecodeSignatureOfType(PetiteBean.class);
	}

	/**
	 * Return elapsed number of milliseconds for configuration. 
	 */
	public long elapsedTime() {
		return elapsed;
	}

	public AutomagicPetiteConfigurator withScanner(final Consumer<ClassScanner> scannerConsumer) {
		scannerConsumer.accept(classScanner);
		return this;
	}

	/**
	 * Configures {@link utils.petite.PetiteContainer} with specified class path.
	 */
	public void configure(final PetiteContainer petiteContainer) {
		this.container = petiteContainer;

		classScanner.smartModeEntries();
		classScanner.onEntry(ENTRY_CONSUMER);
		classScanner.scanDefaultClasspath();

		elapsed = System.currentTimeMillis();
		try {
			classScanner.start();
		} catch (Exception ex) {
			throw new utils.petite.PetiteException("Scan classpath error", ex);
		}
		elapsed = System.currentTimeMillis() - elapsed;
		log.info("Petite configured in " + elapsed + " ms. Total beans: " + petiteContainer.beansCount());
	}

	/**
	 * Scans all classes and registers only those annotated with {@link PetiteBean}.
	 * Because of performance purposes, classes are not dynamically loaded; instead, their
	 * file content is examined.
	 */
	private Consumer<ClassScanner.EntryData> ENTRY_CONSUMER = new Consumer<ClassScanner.EntryData>() {
		@Override
		public void accept(final ClassScanner.EntryData entryData) {
			String entryName = entryData.name();
			if (!entryData.isTypeSignatureInUse(petiteBeanAnnotationBytes)) {
				return;
			}
			Class<?> beanClass;

			try {
				beanClass = classScanner.loadClass(entryName);
			} catch (ClassNotFoundException cnfex) {
				throw new PetiteException("Unable to load class: " + cnfex, cnfex);
			}

			if (beanClass == null) {
				return;
			}

			PetiteBean petiteBean = beanClass.getAnnotation(PetiteBean.class);
			if (petiteBean == null) {
				return;
			}
			container.registerPetiteBean(beanClass, null, null, null, false, null);
		}

	};
}