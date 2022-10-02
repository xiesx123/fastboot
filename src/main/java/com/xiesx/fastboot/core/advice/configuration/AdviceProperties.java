package com.xiesx.fastboot.core.advice.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @title AdviceProperties.java
 * @description
 * @author xiesx
 * @date 2022-10-01 21:58:15
 */
@Data
@ConfigurationProperties(prefix = AdviceProperties.PREFIX)
public class AdviceProperties {

    public static final String PREFIX = "fastboot.advice";

    private List<String> bodyIgnoresUrls = Lists.newArrayList();

    public List<String> getBodyIgnoresUrls() {
        List<String> urls = Lists.newArrayList();
        for (String url : bodyIgnoresUrls) {
            urls.addAll(StrUtil.split(url, ',', true, true));
        }
        return urls.stream().distinct().collect(Collectors.toList());
    }
}
