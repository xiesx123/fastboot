package com.xiesx.fastboot.core.json.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.google.common.collect.Lists;

import lombok.Data;

/**
 * @title FastJsonProperties.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:59:38
 */
@Data
@ConfigurationProperties(prefix = FastJsonProperties.PREFIX)
public class FastJsonProperties {

    public static final String PREFIX = "fastboot.fastjson";

    public static final SerializerFeature[] SERIALIZER_FEATURES = {
            // 字符类型字段如果为null，输出为""，而不是null
            // SerializerFeature.WriteNullStringAsEmpty,
            // 数值类型如果为null，输出为0，而不是null
            // SerializerFeature.WriteNullNumberAsZero,
            // 布尔类型如果为null，输出为false，而不是null
            // SerializerFeature.WriteNullBooleanAsFalse,
            // 枚举类型用ToString输出
            // SerializerFeature.WriteEnumUsingToString,
            // 是否输出值为null的字段
            // SerializerFeature.WriteMapNullValue,
            // list字段如果为null，输出为[]，而不是null
            // SerializerFeature.WriteNullListAsEmpty,
            // 输出格式后的日期
            SerializerFeature.WriteDateUseDateFormat,
            // 禁用循环引用
            SerializerFeature.DisableCircularReferenceDetect,
            // 忽略错误的get
            SerializerFeature.IgnoreErrorGetter,
            // 输出格式化
            // SerializerFeature.PrettyFormat
    };

    // 类型
    private List<String> supportedMediaTypes = Lists.newArrayList();

    // 配置
    private FastJsonConfig config = new FastJsonConfig();

    // 脱敏
    private boolean desensitize = true;
}
