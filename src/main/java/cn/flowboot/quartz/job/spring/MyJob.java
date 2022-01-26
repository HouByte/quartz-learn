package cn.flowboot.quartz.job.spring;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <h1>Spring Boot Job demo</h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class MyJob extends QuartzJobBean {


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.printf("[QuartzJobBean] %s 正在执行, 下一次执行 %s \n",context.getScheduledFireTime(),context.getNextFireTime());

    }
}
