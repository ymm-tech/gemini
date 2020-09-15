package io.manbang.easybytecoder.plugin.mybatismock.runtime.utils;


import com.alipay.hessian.ClassNameFilter;
import com.alipay.hessian.ClassNameResolver;
import com.caucho.hessian.io.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class HessianSerializerFactory extends SerializerFactory {

	private ConcurrentHashMap serializerMap = new ConcurrentHashMap();
	private ConcurrentHashMap deserializerMap = new ConcurrentHashMap();



	public void init () {


		serializerMap.put(Byte.class, new StringValueSerializer());
		deserializerMap.put(Byte.class, new StringValueDeserializer(Byte.class));

		serializerMap.put(Short.class, new StringValueSerializer());
		deserializerMap.put(Short.class, new StringValueDeserializer(Short.class));

//		serializerMap.put(Double.class, new StringValueSerializer());
//		deserializerMap.put(Double.class, new StringValueDeserializer(Double.class));
//
//		serializerMap.put(Integer.class, new StringValueSerializer());
//		deserializerMap.put(Integer.class, new StringValueDeserializer(Integer.class));
//
//		serializerMap.put(Long.class, new StringValueSerializer());
//		deserializerMap.put(Long.class, new StringValueDeserializer(Long.class));


		serializerMap.put(BigInteger.class, new StringValueSerializer());
		deserializerMap.put(BigInteger.class, new StringValueDeserializer(BigInteger.class));

		serializerMap.put(BigDecimal.class, new StringValueSerializer());
		deserializerMap.put(BigDecimal.class, new BigDecimalDeserializer());

		serializerMap.put(Float.class, new StringValueSerializer());
		deserializerMap.put(Float.class, new NullableStringValueDeserializer(Float.class));

		try {
			deserializerMap.put(Object.class, new NullableStringValueDeserializer(String.class));
		} catch (Throwable e) {
//			System.out.println("XXXX deserializerMap");
		}

		// define custom ClassNameResolver
		ClassNameResolver resolver = new ClassNameResolver();
		resolver.addFilter(new ClassNameFilter() {
			@Override
			public int order() {
				return 0;
			}

			@Override
			public String resolve(String className) throws IOException {
				if (className.equals(Thread.class.getName())) {
					throw new IOException("Thread is not allowed.");
				}

				if (className.contains("java.util.concurrent.TimeUnit") && className.contains("$")) {
					className = className.substring(0, className.indexOf("$"));
				}
				return className;
			}
		});
		// replace default ClassNameResolver
		this.setClassNameResolver(resolver);
	}
	@Override
	public Serializer getSerializer(Class cl) throws HessianProtocolException {
		Serializer serializer = (Serializer) serializerMap.get(cl);
		if (serializer != null){
			return serializer;
		}
		return super.getSerializer(cl);
	}

	@Override
	public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
		Deserializer deserializer = (Deserializer) deserializerMap.get(cl);
		if (deserializer != null){
			return deserializer;
		}
		return super.getDeserializer(cl);
	}

}
