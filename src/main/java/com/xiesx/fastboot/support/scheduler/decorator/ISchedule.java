package com.xiesx.fastboot.support.scheduler.decorator;

/**
 * @title SimpleInterface.java
 * @description 装饰起接口
 * @author xiesx
 * @date 2020-3-4 1:07:35
 */
public interface ISchedule {

    /**
     * 初始化
     */
    public void init();

    /**
     * 是否启动
     */
    public boolean isStart();
}
