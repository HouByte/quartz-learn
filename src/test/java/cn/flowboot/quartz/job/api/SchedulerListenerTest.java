package cn.flowboot.quartz.job.api;


import cn.flowboot.quartz.listener.MySchedulerListener;
import cn.flowboot.quartz.listener.MyTriggerListener;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class SchedulerListenerTest {

    @Test
    public void execute() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.getListenerManager().addSchedulerListener(new MySchedulerListener());
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    scheduler.shutdown();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        });
        //开启定时任务
        scheduler.start();

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("jobDetail","group1")
                .usingJobData("name","hello world")
                .build();
        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .build();
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener());
        //任务设置
        scheduler.scheduleJob(jobDetail,trigger);
        try {
            //防止关闭
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //关闭定时任务
        scheduler.shutdown();
    }
}
