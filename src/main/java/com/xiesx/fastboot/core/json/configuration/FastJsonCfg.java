package com.xiesx.fastboot.core.json.configuration;

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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.google.common.collect.Lists;
import com.xiesx.fastboot.core.json.filter.FastContextValueFilter;
import com.xiesx.fastboot.core.json.serialize.ToStringSerializer;

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
            fastJsonConfig.setCharset(Charset.defaultCharset());
        }
        // 格式化
        if (ObjectUtil.isEmpty(fastJsonConfig.getDateFormat())) {
            fastJsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        }
        // 序列化特性
        fastJsonConfig.setWriterFeatures(ArrayUtil.addAll(FastJsonProperties.SERIALIZER_FEATURES, fastJsonConfig.getWriterFeatures()));
        // 序列化配置
        ObjectWriterProvider owp = new ObjectWriterProvider();
        owp.register(Long.class, ToStringSerializer.instance);
        owp.register(Long.TYPE, ToStringSerializer.instance);
        owp.register(BigInteger.class, ToStringSerializer.instance);
        // 序列化过滤器
        List<Filter> filters = Lists.newArrayList(fastJsonConfig.getWriterFilters());
        FastContextValueFilter fcv = new FastContextValueFilter();
        fcv.setDesensitize(fastJsonProperties.isDesensitize());// 是否开启脱敏
        filters.add(fcv);// 默认过滤器
        fastJsonConfig.setWriterFilters(filters.toArray(new Filter[filters.size()]));
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
    // fastJsonHttpMessageConverter.setDefaultCharset(Charset.defaultCharset());
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
        fastJsonHttpMessageConverter.setDefaultCharset(Charset.defaultCharset());
        // 配置
        fastJsonHttpMessageConverter.setFastJsonConfig(newFastJsonConfig());
        // 媒体类型
        fastJsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(newSupportedMediaTypes()));
        converters.add(1, fastJsonHttpMessageConverter);
    }
}
