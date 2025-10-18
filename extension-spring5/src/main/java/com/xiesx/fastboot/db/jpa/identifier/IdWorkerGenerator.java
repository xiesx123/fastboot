package com.xiesx.fastboot.db.jpa.identifier;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class IdWorkerGenerator implements IdentifierGenerator {

    /** 前缀 */
    public String prefix;

    /** 数据终端ID，默认0 */
    public long workerId;

    /** 数据中心ID，默认0 */
    public long centerId;

    private Snowflake snowflake;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        this.prefix = params.getProperty("prefix");
        this.workerId = Convert.toLong(params.getProperty("workerId"), 0L);
        this.centerId = Convert.toLong(params.getProperty("centerId"), 0L);
        this.snowflake = IdUtil.getSnowflake(workerId, centerId);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        if (StrUtil.isNotBlank(this.prefix)) {
            return this.prefix + snowflake.nextId();
        }
        return snowflake.nextId();
    }

    public static Long nextId(long workerId, long datacenterId) {
        return IdUtil.getSnowflake(workerId, datacenterId).nextId();
    }

    public static Long nextId() {
        return nextId(0, 0);
    }
}
