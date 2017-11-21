package com.springboot.spark.spring.serialization;

import org.apache.spark.SparkConf;
import org.apache.spark.serializer.KryoSerializer;
import org.apache.spark.serializer.SerializerInstance;

public class SpringAwareSerializer extends KryoSerializer {

  /**
   * The serial version UID
   */
  private static final long serialVersionUID = 6392630290444431030L;

  public SpringAwareSerializer(SparkConf conf) {
    super(conf);
  }

  @Override
  public SerializerInstance newInstance() {
    return new SpringAwareSerializerInstance(this);
  }

}
