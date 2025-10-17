package com.xiesx.fastboot.core.advice.configuration;

import cn.hutool.core.util.StrUtil;

import com.google.common.collect.Lists;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;

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
