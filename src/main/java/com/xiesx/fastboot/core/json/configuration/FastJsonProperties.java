package com.xiesx.fastboot.core.json.configuration;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.google.common.collect.Lists;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = FastJsonProperties.PREFIX)
public class FastJsonProperties {

    public static final String PREFIX = "fastboot.fastjson";

    public static final JSONWriter.Feature[] SERIALIZER_FEATURES = {
        // 输出格式化
        // JSONWriter.Feature.PrettyFormat
        // 输出值为null的字段
        // JSONWriter.Feature.WriteMapNullValue,
        // 字符类型字段如果为null，输出为""，而不是null
        // JSONWriter.Feature.WriteNullStringAsEmpty,
        // 数值类型如果为null，输出为0，而不是null
        // JSONWriter.Feature.WriteNullNumberAsZero,
        // 布尔类型如果为null，输出为false，而不是null
        // JSONWriter.Feature.WriteNullBooleanAsFalse,
        // 枚举类型用ToString输出
        // JSONWriter.Feature.WriteEnumUsingToString,
        // list字段如果为null，输出为[]，而不是null
        // JSONWriter.Feature.WriteNullListAsEmpty,
        //  处理大对象
        JSONWriter.Feature.LargeObject,
        // 忽略错误的Get
        JSONWriter.Feature.IgnoreErrorGetter,
    };

    public static final JSONReader.Feature[] DESERIALIZER_FEATURES = {
        // 反序列化错误时返回 null 而不是抛异常
        JSONReader.Feature.NullOnError,
        // 忽略未实现 Serializable 接口的类
        JSONReader.Feature.IgnoreNoneSerializable,
    };

    // 类型
    private List<String> supportedMediaTypes = Lists.newArrayList();

    // 配置
    private FastJsonConfig config = new FastJsonConfig();

    // 脱敏
    private boolean desensitize = true;
}
