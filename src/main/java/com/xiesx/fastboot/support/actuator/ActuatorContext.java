package com.xiesx.fastboot.support.actuator;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.xiesx.fastboot.base.result.R;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import lombok.extern.log4j.Log4j2;

/**
 * @title ExecutorContext.java
 * @description
 * @author xiesx
 * @date 2021-07-19 14:15:28
 */
@Data
@Builder
@FieldNameConstants(innerTypeName = "FIELDS")
@Log4j2
public class ActuatorContext {

    @Builder.Default
    public String trace = "";

    @Builder.Default
    public String error = "";

    @Builder.Default
    private ConcurrentMap<String, Object> context = Maps.newConcurrentMap();

    /**
     * 追加
     *
     * @param data
     */
    public void put(String key, Object val) {
        context.put(key, val);
    }

    public void put(Dict data) {
        put(data, true);
    }

    public void put(Dict data, boolean print) {
        if (print) {
            log.debug("{} 新增结果 {}", trace, R.toJsonStr(data));
        }
        context.putAll(data);
    }

    /**
     * 累积
     *
     * @param data
     */
    public Dict add(Dict data) {
        Dict dict = Dict.create();
        data.forEach((k, v) -> {
            // 老值
            BigDecimal before = MapUtil.get(context, k, BigDecimal.class);
            // 新值
            BigDecimal after = NumberUtil.add(before, BigDecimal.valueOf(Convert.toLong(v)));
            // 更新值
            context.put(k, after);
            // 当前k的新v
            dict.set(k, after);
        });
        log.debug("{} {} -> 累积 {}", trace, R.toJsonStr(data), R.toJsonStr(dict));
        return dict;
    }

    /**
     * 清空
     *
     */
    public void clear() {
        context.clear();
    }

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return context.containsKey(key);
    }

    /**
     * 是否为空
     *
     * @param key
     * @return
     */
    public boolean isEmpty() {
        return context.isEmpty();
    }

    /**
     * 获取
     *
     * @return
     */
    public Dict get() {
        // log.info( "所有结果 {}", IR.toJsonStr(context));
        Dict dict = Dict.create();
        dict.putAll(context);
        MapUtil.sort(dict);
        return dict;
    }

    public Object get(String key) {
        return context.get(key);
    }

    public static void main(String[] args) {
        Dict dict = Dict.create().set("jump", Dict.create().set("b", 1).set("a", 1).set("2", 16).set("-1", 8).set("6", 11));
        Dict jump = MapUtil.get(dict, "jump", Dict.class);
        ActuatorContext context = ActuatorContext.builder().build();
        Console.log(MapUtil.sort(context.get()));
        Console.log(MapUtil.sort(context.add(jump)));
        Console.log(MapUtil.sort(context.add(Dict.create().set("2", 16))));
        Console.log(MapUtil.sort(context.get()));
    }
}
