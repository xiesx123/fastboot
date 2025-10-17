package com.xiesx.fastboot.core.logger.pkaq;

import cn.hutool.core.util.StrUtil;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpyLogger implements MessageFormattingStrategy {

    private static final String SQL_FORAMT = "took {} ms | {} | {} ";

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url) {
        return StrUtil.format(SQL_FORAMT, elapsed, category, sql);
    }
}
