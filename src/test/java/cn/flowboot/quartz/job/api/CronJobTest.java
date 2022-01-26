package cn.flowboot.quartz.job.api;

import junit.framework.TestCase;
import org.quartz.*;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class CronJobTest extends TestCase {

    public void testExecute() {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(CronJob.class)
                .withIdentity("jobDetail","group1")
                .build();

        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? "))
                .build();
        SchedulerTestUtils.test(jobDetail,trigger);
    }
}
