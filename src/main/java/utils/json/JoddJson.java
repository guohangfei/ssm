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

package utils.json;

import jodd.json.TypeJsonSerializerMap;
import jodd.json.meta.JSON;
import jodd.json.meta.JsonAnnotationManager;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Jodd JSON module.
 */
public class JoddJson {

	private static final JoddJson instance = new JoddJson();

	/**
	 * Returns the module instance.
	 */
	public static JoddJson defaults() {
		return instance;
	}

	// ---------------------------------------------------------------- settings

	public static final String DEFAULT_CLASS_METADATA_NAME = "__class";

	private TypeJsonSerializerMap typeSerializers = new TypeJsonSerializerMap();
	private JsonAnnotationManager annotationManager = new JsonAnnotationManager();
	private Class<? extends Annotation> jsonAnnotation = JSON.class;
	private String classMetadataName = null;
	private boolean deepSerialization = false;
	private boolean useAltPathsByParser = false;
	private Class[] excludedTypes = null;
	private String[] excludedTypeNames = null;
	private boolean serializationSubclassAware = true;
	private boolean strictStringEncoding = false;

	/**
	 * Returns {@link TypeJsonSerializerMap type serializer map}
	 */
	public TypeJsonSerializerMap getTypeSerializers() {
		return typeSerializers;
	}

	/**
	 * Defines new type serializer map.
	 */
	public JoddJson setTypeSerializers(final TypeJsonSerializerMap typeSerializers) {
		Objects.requireNonNull(typeSerializers);
		this.typeSerializers = typeSerializers;
		return this;
	}

	/**
	 * Returns {@link JsonAnnotationManager}.
	 */
	public JsonAnnotationManager getAnnotationManager() {
		return annotationManager;
	}

	/**
	 * Sets new {@link JsonAnnotationManager}.
	 */
	public JoddJson setAnnotationManager(final JsonAnnotationManager annotationManager) {
		Objects.requireNonNull(annotationManager);
		this.annotationManager = annotationManager;
		return this;
	}


	/**
	 * Returns the annotation used for marking the properties.
	 */
	public Class<? extends Annotation> getJsonAnnotation() {
		return jsonAnnotation;
	}

	/**
	 * Defines new custom JSON annotation for marking the JSON properties that are going to be serialized.
	 */
	public void setJsonAnnotation(final Class<? extends Annotation> jsonAnnotation) {
		this.jsonAnnotation = jsonAnnotation;
	}

	/**
	 * @see #setClassMetadataName(String)
	 */
	public String getClassMetadataName() {
		return classMetadataName;
	}

	/**
	 * Specifies if 'class' metadata is used and its value. When set, class metadata
	 * is used by {@link jodd.json.JsonSerializer} and all objects
	 * will have additional field with the class type in the resulting JSON.
	 * {@link jodd.json.JsonParser} will also consider this flag to build
	 * correct object type. If <code>null</code>, class information is not used.
	 */
	public void setClassMetadataName(final String classMetadataName) {
		this.classMetadataName = classMetadataName;
	}

	public boolean isDeepSerialization() {
		return deepSerialization;
	}

	/**
	 * Defines default behavior of a {@link jodd.json.JsonSerializer}.
	 * If set to <code>true</code>, objects will be serialized
	 * deep, so all collections and arrays will get serialized.
	 */
	public void setDeepSerialization(final boolean deepSerialization) {
		this.deepSerialization = deepSerialization;
	}

	/**
	 * @see #setUseAltPathsByParser(boolean)
	 */
	public boolean isUseAltPathsByParser() {
		return useAltPathsByParser;
	}

	/**
	 * Defines if parser will use extended paths information
	 * and path matching.
	 */
	public void setUseAltPathsByParser(final boolean useAltPathsByParser) {
		this.useAltPathsByParser = useAltPathsByParser;
	}

	/**
	 * @see #setExcludedTypes(Class[])
	 */
	public Class[] getExcludedTypes() {
		return excludedTypes;
	}

	/**
	 * Defines list of excluded types for serialization.
	 */
	public void setExcludedTypes(final Class... excludedTypes) {
		this.excludedTypes = excludedTypes;
	}

	/**
	 * @see #setExcludedTypeNames(String...)
	 */
	public String[] getExcludedTypeNames() {
		return excludedTypeNames;
	}

	/**
	 * Defines a list of excluded types names for serialization. Type name
	 * can contain wildcards (<code>*</code> and <code>?</code>).
	 */
	public void setExcludedTypeNames(final String... excludedTypeNames) {
		this.excludedTypeNames = excludedTypeNames;
	}

	/**
	 * @see #setSerializationSubclassAware(boolean)
	 */
	public boolean isSerializationSubclassAware() {
		return serializationSubclassAware;
	}

	/**
	 * When set searches for first annotated class or interface and use it's data.
	 */
	public void setSerializationSubclassAware(final boolean serializationSubclassAware) {
		this.serializationSubclassAware = serializationSubclassAware;
	}

	/**
	 * @see #setStrictStringEncoding(boolean)
	 */
	public boolean isStrictStringEncoding() {
		return strictStringEncoding;
	}

	/**
	 * Sets the strict JSON encoding.
	 * JSON specification specifies that certain characters should be
	 * escaped (see: http://json.org/). However, in the real world, not all
	 * needs to be escaped: especially the 'solidus' character (/). If this one
	 * is escaped, many things can go wrong, from URLs to Base64 encodings.
	 * This flag controls the behavior of strict encoding. By default, the
	 * strict encoding is set to {@code false}.
	 */
	public void setStrictStringEncoding(final boolean strictStringEncoding) {
		this.strictStringEncoding = strictStringEncoding;
	}

}