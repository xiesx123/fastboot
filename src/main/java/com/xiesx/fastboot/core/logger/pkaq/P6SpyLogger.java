package com.xiesx.fastboot.core.logger.pkaq;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import cn.hutool.core.util.StrUtil;

/**
 * @title P6SpyLogger.java
 * @description
 * @author xiesx
 * @date 2021-04-17 18:17:58
 */
public class P6SpyLogger implements MessageFormattingStrategy {

    private static final String SQL_FORAMT = "| time {}ms | {} | {} ";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StrUtil.format(SQL_FORAMT, elapsed, category, sql);
    }
}
