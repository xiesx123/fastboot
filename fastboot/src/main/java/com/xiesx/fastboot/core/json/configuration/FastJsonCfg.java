package com.xiesx.fastboot.core.json.configuration;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.google.common.collect.Lists;
import com.xiesx.fastboot.core.json.filter.FastContextValueFilter;
import com.xiesx.fastboot.core.json.serialize.ToDateReader;
import com.xiesx.fastboot.core.json.serialize.ToStringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

@EnableConfigurationProperties(FastJsonProperties.class)
@ConditionalOnClass({JSON.class, HttpMessageConverter.class})
public class FastJsonCfg implements WebMvcConfigurer {

    @Autowired FastJsonProperties fastJsonProperties;

    /** 配置 */
    public FastJsonConfig newFastJsonConfig() {
        // 默认配置
        FastJsonConfig config = fastJsonProperties.getConfig();
        // 编码
        if (ObjectUtil.isEmpty(config.getCharset())) {
            config.setCharset(Charset.defaultCharset());
        }
        // 格式化
        if (ObjectUtil.isEmpty(config.getDateFormat())) {
            config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        }
        // 序列化 Writer ==============================
        // 特性
        config.setWriterFeatures(
                ArrayUtil.addAll(
                        FastJsonProperties.SERIALIZER_FEATURES, config.getWriterFeatures()));
        // 注冊
        JSON.register(Long.class, ToStringWriter.instance);
        JSON.register(Long.TYPE, ToStringWriter.instance);
        JSON.register(BigInteger.class, ToStringWriter.instance);
        // 过滤器
        List<Filter> filters = Lists.newArrayList(config.getWriterFilters());
        FastContextValueFilter fcv = new FastContextValueFilter(); // 默认过滤器
        fcv.setDesensitize(fastJsonProperties.isDesensitize()); // 是否开启脱敏
        filters.add(fcv);
        config.setWriterFilters(filters.toArray(new Filter[filters.size()]));

        // 序列化特性 Reader ==============================
        // 反序列化特性
        config.setReaderFeatures(
                ArrayUtil.addAll(
                        FastJsonProperties.DESERIALIZER_FEATURES, config.getReaderFeatures()));
        // 注冊
        JSON.register(Date.class, new ToDateReader());
        return config;
    }

    /** 支持类型 */
    public List<String> newSupportedMediaTypes() {
        List<String> medias = fastJsonProperties.getSupportedMediaTypes();
        medias.add(0, MediaType.TEXT_HTML_VALUE);
        medias.add(1, MediaType.APPLICATION_JSON_VALUE);
        return medias;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // String 转换器
        converters.add(0, new StringHttpMessageConverter()); //
        // Json 转换器
        // FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // // 编码
        // converter.setDefaultCharset(Charset.defaultCharset());
        // // 配置
        // converter.setFastJsonConfig(newFastJsonConfig());
        // // 媒体类型
        // converter.setSupportedMediaTypes(MediaType.parseMediaTypes(newSupportedMediaTypes()));
        // converters.add(1, converter);

        String[] candidates = {
            // Spring Boot 2
            "com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter",
            // Spring Boot 3
            "com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter"
        };
        for (String clazz : candidates) {
            try {
                Class<?> c = Class.forName(clazz);
                Object converter = c.getDeclaredConstructor().newInstance();
                c.getMethod("setDefaultCharset", Charset.class)
                        .invoke(converter, Charset.defaultCharset());
                c.getMethod("setFastJsonConfig", FastJsonConfig.class)
                        .invoke(converter, newFastJsonConfig());
                c.getMethod("setSupportedMediaTypes", List.class)
                        .invoke(converter, MediaType.parseMediaTypes(newSupportedMediaTypes()));
                converters.add(1, (HttpMessageConverter<?>) converter);
            } catch (Exception e) {
                continue;
            }
        }
    }
}
