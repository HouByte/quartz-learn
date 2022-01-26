package cn.flowboot.quartz.job.api;

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
public class TriggersJob implements Job {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Trigger 优先级 "+msg);
    }
}
