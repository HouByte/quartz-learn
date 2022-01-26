package cn.flowboot.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/26
 */
public class MyJobListener extends JobListenerSupport {
    /**
     * <p>
     * Get the name of the <code>JobListener</code>.
     * </p>
     */
    @Override
    public String getName() {
        return "myJobListener";
    }

    /**
     * 即将执行
     * @param context
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        super.jobToBeExecuted(context);
        System.out.println("【jobToBeExecuted|"+context.getFireInstanceId()+"】当前时间 "+context.getFireTime());
    }

    /**
     * 忽略本次job
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        super.jobExecutionVetoed(context);
        System.out.println("【jobExecutionVetoed|"+context.getFireInstanceId()+"】当前时间 "+context.getFireTime());

    }

    /**
     * 已经执行了
     * @param context
     * @param jobException
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        super.jobWasExecuted(context, jobException);
        System.out.println("【jobWasExecuted|"+context.getFireInstanceId()+"】当前时间 "+context.getFireTime());
    }
}
