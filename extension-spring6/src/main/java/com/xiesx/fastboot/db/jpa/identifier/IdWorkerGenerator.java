package com.xiesx.fastboot.db.jpa.identifier;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

import java.lang.reflect.Member;
import java.util.EnumSet;

public class IdWorkerGenerator implements BeforeExecutionGenerator {

    private static final long serialVersionUID = 1L;

    /** 前缀 */
    public String prefix;

    /** 数据中心ID，默认0 */
    public long centerId;

    /** 数据终端ID，默认0 */
    public long workerId;

    private Snowflake snowflake;

    public IdWorkerGenerator(
            GeneratedIdWorker config, Member member, CustomIdGeneratorCreationContext context) {
        this.prefix = config.prefix();
        this.workerId = Convert.toLong(config.workerId(), 0L);
        this.centerId = Convert.toLong(config.centerId(), 0L);
        this.snowflake = IdUtil.getSnowflake(workerId, centerId);
    }

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object owner,
            Object currentValue,
            EventType eventType) {
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

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
