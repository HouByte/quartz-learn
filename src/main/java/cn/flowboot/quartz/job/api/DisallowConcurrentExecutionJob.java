package cn.flowboot.quartz.job.api;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
@DisallowConcurrentExecution
public class DisallowConcurrentExecutionJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(jobExecutionContext.getScheduledFireTime()+" 并发测试：开始");
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(jobExecutionContext.getScheduledFireTime()+" 并发测试：结束，用时"+jobExecutionContext.getJobRunTime());
    }
}
