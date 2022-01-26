package cn.flowboot.quartz.job.api;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1>Scheduler Test Utils</h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class SchedulerTestUtils {

    public static void simpleTest(Class<? extends Job> jobClass){
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getName(),jobClass.getName())
                .build();
        test(jobDetail,10);

    }
    public static void simpleTest(Class<? extends Job> jobClass,int interval){
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getName(),jobClass.getName())
                .build();
        test(jobDetail,interval);

    }

    public static void test(JobDetail jobDetail,int interval){

        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever()
                )
                .build();
        test(jobDetail,trigger);
    }

    public static void test(JobDetail jobDetail,Trigger trigger){
        try {
            testUtils(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static void testUtils(JobDetail jobDetail,Trigger trigger) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //开启定时任务
        scheduler.start();


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
