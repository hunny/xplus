package com.springboot.spark.spring.serialization;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.apache.spark.serializer.DeserializationStream;
import org.apache.spark.serializer.KryoSerializer;
import org.apache.spark.serializer.KryoSerializerInstance;
import org.apache.spark.serializer.SerializationStream;

import com.springboot.spark.spring.util.SpringBuilder;

import scala.reflect.ClassTag;

public class SpringAwareSerializerInstance extends KryoSerializerInstance {

  public SpringAwareSerializerInstance(KryoSerializer ks) {
    super(ks, false);
  }

  @Override
  public <T> T deserialize(ByteBuffer bytes, ClassLoader loader, ClassTag<T> evidence$5) {
    final T deserialized = super.deserialize(bytes, loader, evidence$5);
    // autowire dependencies
    SpringBuilder.autowire(deserialized);
    return deserialized;
  }

  @Override
  public <T> T deserialize(ByteBuffer bytes, ClassTag<T> classTag) {
    final T deserialized = super.deserialize(bytes, classTag);
    // autowire dependencies
    SpringBuilder.autowire(deserialized);
    return deserialized;
  }

  @Override
  public DeserializationStream deserializeStream(InputStream s) {
    return new SpringAwareDeserializationStream(super.deserializeStream(s));
  }

  @Override
  public <T> ByteBuffer serialize(T t, ClassTag<T> evidence$3) {
    return super.serialize(t, evidence$3);
  }

  @Override
  public SerializationStream serializeStream(OutputStream s) {
    return super.serializeStream(s);
  }

}
