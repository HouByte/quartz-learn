package cn.flowboot.quartz.job.api;

import junit.framework.TestCase;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class PersistJobDataAfterExecutionJobTest extends TestCase {

    public void testExecute() {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(PersistJobDataAfterExecutionJob.class)
                .withIdentity("PersistJobDataAfterExecution","group1")
                .usingJobData("count",1)
                .build();

        SchedulerTestUtils.test(jobDetail,10);
    }
}
