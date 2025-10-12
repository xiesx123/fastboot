package com.xiesx.fastboot.core.json.configuration;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
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
        // 序列化特性
        JSONWriter.Feature[] features =
                ArrayUtil.addAll(
                        FastJsonProperties.SERIALIZER_FEATURES, config.getWriterFeatures());
        config.setWriterFeatures(features);
        // 序列化注冊
        JSON.register(Long.class, ToStringWriter.instance);
        JSON.register(Long.TYPE, ToStringWriter.instance);
        JSON.register(BigInteger.class, ToStringWriter.instance);
        // 反序列化注冊
        JSON.register(Date.class, new ToDateReader());
        // 序列化过滤器
        List<Filter> filters = Lists.newArrayList(config.getWriterFilters());
        FastContextValueFilter fcv = new FastContextValueFilter();
        fcv.setDesensitize(fastJsonProperties.isDesensitize()); // 是否开启脱敏
        filters.add(fcv); // 默认过滤器
        config.setWriterFilters(filters.toArray(new Filter[filters.size()]));
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
        // 自定义转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 编码
        converter.setDefaultCharset(Charset.defaultCharset());
        // 配置
        converter.setFastJsonConfig(newFastJsonConfig());
        // 媒体类型
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes(newSupportedMediaTypes()));
        // 转换器
        converters.add(0, new StringHttpMessageConverter()); // String 转换器
        converters.add(1, converter); // Json 转换器
    }
}
