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

import utils.bean.utilsBean;
import utils.introspector.ClassDescriptor;
import utils.json.utilsJson;
import utils.json.JsonArray;
import utils.json.JsonObject;
import utils.json.TypeJsonSerializer;
import utils.json.impl.ArraysJsonSerializer;
import utils.json.impl.BooleanArrayJsonSerializer;
import utils.json.impl.BooleanJsonSerializer;
import utils.json.impl.ByteArrayJsonSerializer;
import utils.json.impl.CalendarJsonSerializer;
import utils.json.impl.CharSequenceJsonSerializer;
import utils.json.impl.CharacterJsonSerializer;
import utils.json.impl.ClassJsonSerializer;
import utils.json.impl.DateJsonSerializer;
import utils.json.impl.DoubleArrayJsonSerializer;
import utils.json.impl.DoubleJsonSerializer;
import utils.json.impl.EnumJsonSerializer;
import utils.json.impl.FileJsonSerializer;
import utils.json.impl.FloatArrayJsonSerializer;
import utils.json.impl.FloatJsonSerializer;
import utils.json.impl.IntArrayJsonSerializer;
import utils.json.impl.IterableJsonSerializer;
import utils.json.impl.JsonArraySerializer;
import utils.json.impl.JsonObjectSerializer;
import utils.json.impl.JulianDateSerializer;
import utils.json.impl.LocalDateTimeSerializer;
import utils.json.impl.LongArrayJsonSerializer;
import utils.json.impl.MapJsonSerializer;
import utils.json.impl.NumberJsonSerializer;
import utils.json.impl.ObjectJsonSerializer;
import utils.json.impl.UUIDJsonSerializer;
import utils.util.JulianDate;
import utils.util.collection.ClassMap;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Map of {@link utils.json.TypeJsonSerializer json type serializers}.
 */
public class TypeJsonSerializerMap {

	/**
	 * Returns default instance.
	 */
	public static TypeJsonSerializerMap get() {
		return utilsJson.defaults().getTypeSerializers();
	}

	private final TypeJsonSerializerMap defaultSerializerMap;

	/**
	 * Creates new serializers map and registers defaults.
	 */
	public TypeJsonSerializerMap() {
		registerDefaults();
		defaultSerializerMap = null;
	}

	/**
	 * Creates new empty serializer map with given defaults map.
	 */
	public TypeJsonSerializerMap(final TypeJsonSerializerMap defaultSerializerMap) {
		this.defaultSerializerMap = defaultSerializerMap;
	}

	protected final ClassMap<utils.json.TypeJsonSerializer> map = new ClassMap<>();
	protected final ClassMap<utils.json.TypeJsonSerializer> cache = new ClassMap<>();

	/**
	 * Registers default set of {@link utils.json.TypeJsonSerializer serializers}.
	 */
	public void registerDefaults() {

		// main

		map.put(Object.class, new ObjectJsonSerializer());
		map.put(Map.class, new MapJsonSerializer());
		map.put(Iterable.class, new IterableJsonSerializer());

		map.put(JsonObject.class, new JsonObjectSerializer());
		map.put(JsonArray.class, new JsonArraySerializer());

		// arrays
		map.put(int[].class, new IntArrayJsonSerializer());
		map.put(long[].class, new LongArrayJsonSerializer());
		map.put(double[].class, new DoubleArrayJsonSerializer());
		map.put(float[].class, new FloatArrayJsonSerializer());
		map.put(boolean[].class, new BooleanArrayJsonSerializer());
		map.put(byte[].class, new ByteArrayJsonSerializer());

		map.put(Integer[].class, new ArraysJsonSerializer<Integer>() {
			@Override
			protected int getLength(final Integer[] array) {
				return array.length;
			}

			@Override
			protected Integer get(final Integer[] array, final int index) {
				return array[index];
			}
		});
		map.put(Long[].class, new ArraysJsonSerializer<Long>() {
			@Override
			protected int getLength(final Long[] array) {
				return array.length;
			}

			@Override
			protected Long get(final Long[] array, final int index) {
				return array[index];
			}
		});
		map.put(Arrays.class, new ArraysJsonSerializer());

		// strings

		utils.json.TypeJsonSerializer jsonSerializer = new CharSequenceJsonSerializer();

		map.put(String.class, jsonSerializer);
		map.put(StringBuilder.class, jsonSerializer);
		map.put(CharSequence.class, jsonSerializer);

		// number

		jsonSerializer = new NumberJsonSerializer();

		map.put(Number.class, jsonSerializer);

		map.put(Integer.class, jsonSerializer);
		map.put(int.class, jsonSerializer);

		map.put(Long.class, jsonSerializer);
		map.put(long.class, jsonSerializer);

		DoubleJsonSerializer doubleJsonSerializer = new DoubleJsonSerializer();
		map.put(Double.class, doubleJsonSerializer);
		map.put(double.class, doubleJsonSerializer);

		FloatJsonSerializer floatJsonSerializer = new FloatJsonSerializer();
		map.put(Float.class, floatJsonSerializer);
		map.put(float.class, floatJsonSerializer);

		map.put(BigInteger.class, jsonSerializer);
		map.put(BigDecimal.class, jsonSerializer);

		// other

		map.put(Boolean.class, new BooleanJsonSerializer());
		map.put(boolean.class, new BooleanJsonSerializer());
		map.put(Date.class, new DateJsonSerializer());
		map.put(Calendar.class, new CalendarJsonSerializer());
		map.put(JulianDate.class, new JulianDateSerializer());
		map.put(LocalDateTime.class, new LocalDateTimeSerializer());
		map.put(Enum.class, new EnumJsonSerializer());
		map.put(File.class, new FileJsonSerializer(FileJsonSerializer.Type.PATH));

		//map.put(Collection.class, new CollectionJsonSerializer());

		jsonSerializer = new CharacterJsonSerializer();

		map.put(Character.class, jsonSerializer);
		map.put(char.class, jsonSerializer);

		map.put(UUID.class, new UUIDJsonSerializer());

		map.put(Class.class, new ClassJsonSerializer());

		// clear cache
		cache.clear();
	}

	/**
	 * Registers new serializer.
	 */
	public void register(final Class type, final utils.json.TypeJsonSerializer typeJsonSerializer) {
		map.put(type, typeJsonSerializer);
		cache.clear();
	}

	/**
	 * Lookups for the {@link utils.json.TypeJsonSerializer serializer} for given type.
	 * If serializer not found, then all interfaces and subclasses of the type are checked.
	 * Finally, if no serializer is found, object's serializer is returned.
	 */
	public utils.json.TypeJsonSerializer lookup(final Class type) {
		utils.json.TypeJsonSerializer tjs = cache.unsafeGet(type);

		if (tjs != null) {
			return tjs;
		}

		tjs = _lookup(type);

		cache.put(type, tjs);

		return tjs;
	}

	/**
	 * Get type serializer from map. First the current map is used.
	 * If element is missing, default map will be used, if exist.
	 */
	protected utils.json.TypeJsonSerializer lookupSerializer(final Class type) {
		utils.json.TypeJsonSerializer tjs = map.unsafeGet(type);

		if (tjs == null) {
			if (defaultSerializerMap != null) {
				tjs = defaultSerializerMap.map.unsafeGet(type);
			}
		}

		return tjs;
	}

	protected utils.json.TypeJsonSerializer _lookup(final Class type) {
		synchronized (map) {
			TypeJsonSerializer tjs = lookupSerializer(type);

			if (tjs != null) {
				return tjs;
			}

			ClassDescriptor cd = utilsBean.defaults().getClassIntrospector().lookup(type);

			// check array

			if (cd.isArray()) {
				return lookupSerializer(Arrays.class);
			}

			// now iterate interfaces

			Class[] interfaces = cd.getAllInterfaces();

			for (Class interfaze : interfaces) {
				tjs = lookupSerializer(interfaze);

				if (tjs != null) {
					return tjs;
				}
			}

			// now iterate all superclases

			Class[] superclasses = cd.getAllSuperclasses();

			for (Class clazz : superclasses) {
				tjs = lookupSerializer(clazz);

				if (tjs != null) {
					return tjs;
				}
			}

			// nothing found, go with the Object

			return lookupSerializer(Object.class);
		}
	}

}