package cn.flowboot.quartz.job.api;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
@PersistJobDataAfterExecution
public class PersistJobDataAfterExecutionJob implements Job {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println(jobExecutionContext.getScheduledFireTime()+" 参数更新 : "+(count++));
        jobExecutionContext.getJobDetail().getJobDataMap().put("count",count);
    }
}
