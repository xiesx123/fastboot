package com.xiesx.fastboot.db.jpa.identifier;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;

/**
 * @title IdWorkerGenerator.java
 * @description
 * @author xiesx
 * @date 2021-04-04 18:03:55
 */
public class IdWorkerGenerator implements Configurable, IdentifierGenerator {

    public String prefix;

    public long workerId;

    public long centerId;

    private Snowflake snowflake;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.prefix = params.getProperty("prefix");
        this.workerId = Convert.toLong(params.getProperty("workerId"), 0L);
        this.centerId = Convert.toLong(params.getProperty("centerId"), 0L);
        this.snowflake = IdUtil.getSnowflake(workerId, centerId);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if (CharSequenceUtil.isNotBlank(this.prefix)) {
            return this.prefix + snowflake.nextId();
        }
        return snowflake.nextId();
    }

    public static long nextId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static long nextId() {
        return nextId(0, 0);
    }
}
