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

package utils.json;

import utils.json.utilsJson;
import utils.json.JsonContext;
import utils.json.Path;
import utils.json.PathQuery;
import utils.json.PrettyJsonSerializer;
import utils.json.TypeJsonSerializer;
import utils.json.TypeJsonSerializerMap;
import utils.util.ArraysUtil;
import utils.util.buffer.FastCharBuffer;
import utils.util.inex.InExRules;

import java.util.HashMap;
import java.util.Map;

import static utils.json.utilsJson.DEFAULT_CLASS_METADATA_NAME;

/**
 * JSON serializer.
 * @see PrettyJsonSerializer
 */
public class JsonSerializer {

	/**
	 * Static ctor.
	 */
	public static JsonSerializer create() {
		return new JsonSerializer();
	}

	/**
	 * Static ctor for {@link PrettyJsonSerializer}.
	 */
	public static PrettyJsonSerializer createPrettyOne() {
		return new PrettyJsonSerializer();
	}

	// ---------------------------------------------------------------- config

	protected Map<utils.json.Path, TypeJsonSerializer> pathSerializersMap;
	protected TypeJsonSerializerMap typeSerializersMap;

	protected InExRules<utils.json.Path, utils.json.PathQuery, utils.json.PathQuery> rules = new InExRules<utils.json.Path, utils.json.PathQuery, utils.json.PathQuery>() {
		@Override
		public boolean accept(final utils.json.Path value, final utils.json.PathQuery rule, final boolean include) {
			return rule.matches(value);
		}
	};

	protected String classMetadataName = utils.json.utilsJson.defaults().getClassMetadataName();
	protected boolean strictStringEncoding = utils.json.utilsJson.defaults().isStrictStringEncoding();
	protected boolean deep = utils.json.utilsJson.defaults().isDeepSerialization();
	protected Class[] excludedTypes = null;
	protected String[] excludedTypeNames = null;
	protected boolean excludeNulls = false;

	/**
	 * Defines custom {@link TypeJsonSerializer} for given path.
	 */
	public JsonSerializer withSerializer(final String pathString, final TypeJsonSerializer typeJsonSerializer) {
		if (pathSerializersMap == null) {
			pathSerializersMap = new HashMap<>();
		}

		pathSerializersMap.put(Path.parse(pathString), typeJsonSerializer);

		return this;
	}

	/**
	 * Defines custom {@link TypeJsonSerializer} for given type.
	 */
	public JsonSerializer withSerializer(final Class type, final TypeJsonSerializer typeJsonSerializer) {
		if (typeSerializersMap == null) {
			typeSerializersMap = new TypeJsonSerializerMap(utils.json.utilsJson.defaults().getTypeSerializers());
		}

		typeSerializersMap.register(type, typeJsonSerializer);

		return this;
	}

	/**
	 * Adds include path query.
	 */
	public JsonSerializer include(final String include) {
		rules.include(new utils.json.PathQuery(include, true));

		return this;
	}

	/**
	 * Adds a list of included path queries.
	 */
	public JsonSerializer include(final String... includes) {
		for (String include : includes) {
			include(include);
		}
		return this;
	}

	/**
	 * Adds exclude path query.
	 */
	public JsonSerializer exclude(final String exclude) {
		rules.exclude(new utils.json.PathQuery(exclude, false));

		return this;
	}

	/**
	 * Adds a list of excluded path queries.
	 */
	public JsonSerializer exclude(final String... excludes) {
		for (String exclude : excludes) {
			exclude(exclude);
		}
		return this;
	}

	/**
	 * Adds excludes with optional parent including. When parents are included,
	 * for each exclude query its parent will be included.
	 * For example, exclude of 'aaa.bb.ccc' would include it's parent: 'aaa.bb'.
	 */
	public JsonSerializer exclude(final boolean includeParent, final String... excludes) {
		for (String exclude : excludes) {
			if (includeParent) {
				int dotIndex = exclude.lastIndexOf('.');
				if (dotIndex != -1) {
					utils.json.PathQuery pathQuery = new utils.json.PathQuery(exclude.substring(0, dotIndex), true);

					rules.include(pathQuery);
				}
			}

			utils.json.PathQuery pathQuery = new PathQuery(exclude, false);

			rules.exclude(pathQuery);
		}

		return this;
	}

	/**
	 * Sets local class meta-data name.
	 */
	public JsonSerializer setClassMetadataName(final String name) {
		classMetadataName = name;
		return this;
	}

	/**
	 * Sets local class meta-data name.
	 */
	public JsonSerializer withClassMetadata(final boolean useMetadata) {
		if (useMetadata) {
			classMetadataName = DEFAULT_CLASS_METADATA_NAME;
		}
		else {
			classMetadataName = null;
		}
		return this;
	}

	/**
	 * Defines if collections should be followed, i.e. to perform
	 * deep serialization.
	 */
	public JsonSerializer deep(final boolean includeCollections) {
		this.deep = includeCollections;
		return this;
	}

	/**
	 * Excludes type names. You can disable
	 * serialization of properties that are of some type.
	 * For example, you can disable properties of <code>InputStream</code>.
	 * You can use wildcards to describe type names.
	 */
	public JsonSerializer excludeTypes(final String... typeNames) {
		if (excludedTypeNames == null) {
			excludedTypeNames = typeNames;
		} else {
			excludedTypeNames = ArraysUtil.join(excludedTypeNames, typeNames);
		}
		return this;
	}

	/**
	 * Excludes types. Supports interfaces and subclasses as well.
	 */
	public JsonSerializer excludeTypes(final Class... types) {
		if (excludedTypes == null) {
			excludedTypes = types;
		} else {
			excludedTypes = ArraysUtil.join(excludedTypes, types);
		}
		return this;
	}

	/**
	 * Excludes <code>null</code> values while serializing.
	 */
	public JsonSerializer excludeNulls(final boolean excludeNulls) {
		this.excludeNulls = excludeNulls;
		return this;
	}

	/**
	 * Specifies strict string encoding.
	 * @see utilsJson#strictStringEncoding
	 */
	public JsonSerializer strictStringEncoding(final boolean strictStringEncoding) {
		this.strictStringEncoding = strictStringEncoding;
		return this;
	}

	// ---------------------------------------------------------------- serialize

	/**
	 * Serializes object into provided appendable.
	 */
	public void serialize(final Object source, final Appendable target) {
		utils.json.JsonContext jsonContext = createJsonContext(target);

		jsonContext.serialize(source);
	}

	/**
	 * Serializes object into source.
	 */
	public String serialize(final Object source) {
		FastCharBuffer fastCharBuffer = new FastCharBuffer();

		serialize(source, fastCharBuffer);

		return fastCharBuffer.toString();
	}

	/**
	 * Serializes the object, but returns the {@link CharSequence}.
	 */
	public CharSequence serializeToCharSequence(final Object source) {
		FastCharBuffer fastCharBuffer = new FastCharBuffer();

		serialize(source, fastCharBuffer);

		return fastCharBuffer;
	}

	// ---------------------------------------------------------------- json context

	/**
	 * Creates new JSON context.
	 */
	public utils.json.JsonContext createJsonContext(final Appendable appendable) {
		return new JsonContext(this, appendable, excludeNulls, strictStringEncoding);
	}
}