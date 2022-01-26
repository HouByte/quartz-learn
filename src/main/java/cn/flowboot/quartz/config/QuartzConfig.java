package cn.flowboot.quartz.config;

import cn.flowboot.quartz.job.spring.MyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/26
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob1", "group1")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(){
        return TriggerBuilder.newTrigger()
                .startNow()
                .withIdentity("trigger1","group1")
                .forJob(jobDetail())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever()
                )
                .build();
    }
}
