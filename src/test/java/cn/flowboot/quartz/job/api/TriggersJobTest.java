package cn.flowboot.quartz.job.api;

import junit.framework.TestCase;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.quartz.DateBuilder.futureDate;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class TriggersJobTest extends TestCase {

    public void testExecute() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //开启定时任务
        scheduler.start();

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(TriggersJob.class)
                .withIdentity("jobDetail","group1")
                .build();

        Date startTime = futureDate(10, DateBuilder.IntervalUnit.SECOND);
        //创建触发规则
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .usingJobData("msg","hello trigger1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .withPriority(1)
                .build();
        //创建触发规则
        Trigger trigger2 = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .usingJobData("msg","hello trigger2")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .withPriority(2)
                .build();
        Set<Trigger> triggers = new HashSet<>();
        triggers.add(trigger1);
        triggers.add(trigger2);
        //任务设置
        scheduler.scheduleJob(jobDetail,triggers,false);

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
