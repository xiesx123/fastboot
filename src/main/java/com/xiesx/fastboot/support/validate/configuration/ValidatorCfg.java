package com.xiesx.fastboot.support.validate.configuration;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @title ValidatorCfg.java
 * @description 效验配置
 * @author xiesx
 * @date 2020-7-21 22:44:43
 */
@Configuration
@ConditionalOnClass({HibernateValidator.class})
public class ValidatorCfg {

    @Bean
    @ConditionalOnMissingBean(MethodValidationPostProcessor.class)
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        /** 默认是普通模式，会返回所有的验证不通过信息集合 */
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        /** 设置validator模式为快速失败返回 */
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    @Bean
    @ConditionalOnMissingBean(Validator.class)
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
