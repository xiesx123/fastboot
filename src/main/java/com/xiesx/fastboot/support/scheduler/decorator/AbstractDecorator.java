package com.xiesx.fastboot.support.scheduler.decorator;

/**
 * @title AbstractDecorator.java
 * @description
 * @author xiesx
 * @date 2022-02-27 12:42:23
 */
public class AbstractDecorator implements ISchedule {

    /**
     * 被装饰对象
     */
    protected ISchedule schedule;

    /**
     * 构造
     */
    public AbstractDecorator() {}

    /**
     * 构造
     *
     * @param schedule
     */
    public AbstractDecorator(ISchedule schedule) {
        this.schedule = schedule;
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        schedule.init();
    }

    /**
     * 是否启动
     */
    @Override
    public boolean isRun() {
        return false;
    }
}
