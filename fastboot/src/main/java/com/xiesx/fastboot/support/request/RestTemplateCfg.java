package com.xiesx.fastboot.support.request;

import com.yomahub.tlog.resttemplate.TLogRestTemplateInterceptor;
import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateCfg {

  @Bean
  @ConditionalOnMissingBean(RestTemplate.class)
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.setInterceptors(Collections.singletonList(new TLogRestTemplateInterceptor()));
    return restTemplate;
  }

  @Bean
  @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
  public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
    int timeout = 30 * 1000;
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setReadTimeout(timeout);
    factory.setConnectTimeout(timeout);
    return factory;
  }
}
