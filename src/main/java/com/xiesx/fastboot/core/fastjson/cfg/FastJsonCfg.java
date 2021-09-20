package com.xiesx.fastboot.core.fastjson.cfg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.xiesx.fastboot.core.fastjson.filter.DesensitizeFilter;
import com.xiesx.fastboot.core.fastjson.serializer.ToBigDecimalSerializer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @title FastJsonCfg.java
 * @description
 * @author xiesx
 * @date 2021-04-04 17:59:33
 */
@EnableConfigurationProperties(FastJsonProperties.class)
@ConditionalOnClass({JSON.class, HttpMessageConverter.class})
public class FastJsonCfg implements WebMvcConfigurer {

    @Autowired
    private FastJsonProperties fastJsonProperties;

    /**
     * 默认配置
     *
     * @return
     */
    public FastJsonConfig newFastJsonConfig() {
        // 配置
        FastJsonConfig fastJsonConfig = fastJsonProperties.getConfig();
        // 编码
        if (ObjectUtil.isEmpty(fastJsonConfig.getCharset())) {
            fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        }
        // 格式化
        if (ObjectUtil.isEmpty(fastJsonConfig.getDateFormat())) {
            fastJsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        }
        // 序列化特性
        fastJsonConfig.setSerializerFeatures(ArrayUtil.addAll(FastJsonProperties.SERIALIZER_FEATURES, fastJsonConfig.getSerializerFeatures()));
        // 序列化配置
        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(BigDecimal.class, new ToBigDecimalSerializer());
        fastJsonConfig.setSerializeConfig(serializeConfig);
        // 序列化过滤器
        List<SerializeFilter> filters = Lists.newArrayList(fastJsonConfig.getSerializeFilters());
        if (fastJsonProperties.getDesensitize()) {
            filters.add(new DesensitizeFilter());// 脱敏过滤器
        }
        fastJsonConfig.setSerializeFilters(filters.toArray(new SerializeFilter[filters.size()]));
        return fastJsonConfig;
    }

    /**
     * 默认类型
     *
     * @return
     */
    public List<String> newSupportedMediaTypes() {
        List<String> medias = fastJsonProperties.getSupportedMediaTypes();
        medias.add(0, MediaType.TEXT_HTML_VALUE);
        medias.add(1, MediaType.APPLICATION_JSON_VALUE);
        return medias;
    }

    // @Bean
    // @ConditionalOnProperty(prefix = FastJsonProperties.PREFIX, name = "enabled", havingValue =
    // "true", matchIfMissing = true)
    // @ConditionalOnWebApplication
    // public HttpMessageConverters jsonHttpMessageConverter() {
    // // 转换器
    // FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
    // // 编码
    // fastJsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
    // // 配置
    // fastJsonHttpMessageConverter.setFastJsonConfig(newFastJsonConfig());
    // // 媒体类型
    // fastJsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(newSupportedMediaTypes()));
    // return new HttpMessageConverters(fastJsonHttpMessageConverter);
    // }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // string 转换器
        converters.add(0, new StringHttpMessageConverter());
        // json 转换器
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        // 编码
        fastJsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        // 配置
        fastJsonHttpMessageConverter.setFastJsonConfig(newFastJsonConfig());
        // 媒体类型
        fastJsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(newSupportedMediaTypes()));
        converters.add(1, fastJsonHttpMessageConverter);
    }
}
