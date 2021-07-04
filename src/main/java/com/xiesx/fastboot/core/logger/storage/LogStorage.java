package com.xiesx.fastboot.core.logger.storage;

import javax.servlet.http.HttpServletRequest;

/**
 * @title LoggerStorage.java
 * @description
 * @author xiesx
 * @date 2021-04-17 18:52:18
 */
public interface LogStorage {

    /**
     * @param request
     * @param result
     */
    void record(HttpServletRequest request, Object result);
}
