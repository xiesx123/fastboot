package com.xiesx.fastboot.support.scheduler.decorator;

public class AbstractDecorator implements ISchedule {

    /** 装饰对象 */
    protected ISchedule schedule;

    /** 构造 */
    public AbstractDecorator() {}

    /** 构造 */
    public AbstractDecorator(ISchedule schedule) {
        this.schedule = schedule;
    }

    /** 初始化 */
    @Override
    public void init() {
        schedule.init();
    }

    /** 是否启动 */
    @Override
    public boolean isRun() {
        return false;
    }
}
