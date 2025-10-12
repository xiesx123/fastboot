package com.xiesx.fastboot.core.json.configuration;

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
        // 字符类型字段如果为null，输出为""，而不是null
        // JSONWriter.Feature.WriteNullStringAsEmpty,
        // 数值类型如果为null，输出为0，而不是null
        // JSONWriter.Feature.WriteNullNumberAsZero,
        // 布尔类型如果为null，输出为false，而不是null
        // JSONWriter.Feature.WriteNullBooleanAsFalse,
        // 枚举类型用ToString输出
        // JSONWriter.Feature.WriteEnumUsingToString,
        // 是否输出值为null的字段
        // JSONWriter.Feature.WriteMapNullValue,
        // list字段如果为null，输出为[]，而不是null
        // JSONWriter.Feature.WriteNullListAsEmpty,
        // 禁用循环引用
        JSONWriter.Feature.LargeObject,
        // 忽略错误的get
        JSONWriter.Feature.IgnoreErrorGetter,
        // 输出格式化
        // JSONWriter.Feature.PrettyFormat
    };

    // 类型
    private List<String> supportedMediaTypes = Lists.newArrayList();

    // 配置
    private FastJsonConfig config = new FastJsonConfig();

    // 脱敏
    private boolean desensitize = true;
}
