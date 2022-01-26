package cn.flowboot.quartz.job.api;

import org.quartz.*;

import java.time.LocalDate;

/**
 * <h1>第一个Job demo</h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class MyJob implements Job {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

//        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        String name = jobDataMap.getString("name");
        System.out.println("name参数值："+getName());
        System.out.printf("%s 正在执行, 下一次执行 %s \n",jobExecutionContext.getScheduledFireTime(),jobExecutionContext.getNextFireTime());
    }
}
