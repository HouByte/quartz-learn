# Quartz 学习笔记

## 使用示例
### 添加依赖
```xml
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.3.2</version>
</dependency>
```
### 添加配置
```properties
# 实例名称
org.quartz.scheduler.instanceName=myScheduler
# 线程数
org.quartz.threadPool.threadCount=3
# 使用内存存储
org.quartz.jobStore.class=org.quartz.simpl.RAMJobStore
```
### 第一个定时任务任务
```java
package cn.flowboot.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;

/**
 * <h1>第一个Job demo</h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.printf("%s 正在执行, 下一次执行 %s \n",jobExecutionContext.getScheduledFireTime(),jobExecutionContext.getNextFireTime());
    }
}
```

### 使用参数


```java
 //创建任务
JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
        .withIdentity("jobDetail","group1")
        //添加参数
        .usingJobData("name","hello world")
        .build();
```
获取方法一: 参数中获取
```java
JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
String name = jobDataMap.getString("name");
System.out.println("name参数值："+name);
```
获取方法二: 属性获取
```java
private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

//        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        String name = jobDataMap.getString("name");
        System.out.println("name参数值："+getName());
        System.out.printf("%s 正在执行, 下一次执行 %s \n",jobExecutionContext.getScheduledFireTime(),jobExecutionContext.getNextFireTime());
    }
```


### 测试demo工具
将重复性的代码提取为工具，仅为了测试quartz功能
```java
package cn.flowboot.quartz.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1>Scheduler Test Utils</h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class SchedulerTestUtils {

    public static void simpleTest(Class<? extends Job> jobClass){
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getName(),jobClass.getName())
                .build();
        test(jobDetail,10);

    }
    public static void simpleTest(Class<? extends Job> jobClass,int interval){
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getName(),jobClass.getName())
                .build();
        test(jobDetail,interval);

    }

    public static void test(JobDetail jobDetail,int interval){

        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever()
                )
                .build();
        test(jobDetail,trigger);
    }

    public static void test(JobDetail jobDetail,Trigger trigger){
        try {
            testUtils(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static void testUtils(JobDetail jobDetail,Trigger trigger) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //开启定时任务
        scheduler.start();


        //任务设置
        scheduler.scheduleJob(jobDetail,trigger);

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

```
### 防止并发
使用@DisallowConcurrentExecution可以防止并发
```java
package cn.flowboot.quartz.job;

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
//@DisallowConcurrentExecution
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

```
编写例子测试，每五秒执行一次，定时任务睡眠7秒，在未添加注解的情况下下一个任务会在当前任务结束前执行，形成并发；在添加注解后，下一个任务会在当前任务执行完才执行

```java
package cn.flowboot.quartz.job;

import cn.flowboot.quartz.job.api.DisallowConcurrentExecutionJob;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class DisallowConcurrentExecutionJobTest extends TestCase {

    @Test
    public void testExecute() {
        //间隔五秒
        SchedulerTestUtils.simpleTest(DisallowConcurrentExecutionJob.class, 5);
    }
}

```

### 参数更新
可以修改任务参数，但需要配合@PersistJobDataAfterExecution注解
@PersistJobDataAfterExecution 在任务执行后更新数据
```java
package cn.flowboot.quartz.job;

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

```
测试

```java
package cn.flowboot.quartz.job;

import cn.flowboot.quartz.job.api.PersistJobDataAfterExecutionJob;
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
                .withIdentity("PersistJobDataAfterExecution", "group1")
                .usingJobData("count", 1)
                .build();

        SchedulerTestUtils.test(jobDetail, 10);
    }
}

```

### 多触发器和优先级
创建任务
```java
package cn.flowboot.quartz.job;

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

```

测试:其中定义了两个触发器，通过设置优先级来决定谁先执行，修改线程数，设置同时开始可以模拟相同情况下的优先级情况，数值越大优先级越高

```java
package cn.flowboot.quartz.job;

import cn.flowboot.quartz.job.api.TriggersJob;
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
                .withIdentity("jobDetail", "group1")
                .build();

        Date startTime = futureDate(10, DateBuilder.IntervalUnit.SECOND);
        //创建触发规则
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .usingJobData("msg", "hello trigger1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .withPriority(1)
                .build();
        //创建触发规则
        Trigger trigger2 = TriggerBuilder.newTrigger()
                .startAt(startTime)
                .usingJobData("msg", "hello trigger2")
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
        scheduler.scheduleJob(jobDetail, triggers, false);

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

```
scheduleJob 其中replace参数表示，如果已经添加了这些，再去添加一些相同的，导致key值不唯一，是否允许进行替换，如果设置true表示可以替换，如果设置false，然后添加了新的，就会抛出异常
```java
void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace);
```

### Cron 触发器
编写cron job
```java
package cn.flowboot.quartz.job;

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
public class CronJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("cron job");
    }
}

```
测试

```java
package cn.flowboot.quartz.job;

import cn.flowboot.quartz.job.api.CronJob;
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
                .withIdentity("jobDetail", "group1")
                .build();

        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? "))
                .build();
        SchedulerTestUtils.test(jobDetail, trigger);
    }
}
```


## 监听器
### TriggerListener

> 监听与触发器（trigger）相关的事件

相关事件：
- triggerFired 触发
- triggerComplete 完成
- vetoJobExecution 否决

监听器 MyTriggerListener
```java
package cn.flowboot.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

import java.util.Calendar;


/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/26
 */
public class MyTriggerListener extends TriggerListenerSupport {

    @Override
    public String getName() {
        return "myTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        super.triggerFired(trigger, context);
        System.out.println("【triggerFired|"+context.getFireInstanceId()+"】当前时间 "+context.getFireTime());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        super.triggerComplete(trigger, context, triggerInstructionCode);
        System.out.println("【triggerComplete|"+context.getFireInstanceId()+"】"+triggerInstructionCode.toString()+" 运行时间时间 "+context.getJobRunTime());

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        Calendar now = Calendar.getInstance();
        int second = now.get(Calendar.SECOND);
        if (second % 2 == 0){
            System.out.println("本次不执行");
            return true;
        }
        return false;
    }
}
```
测试
```java
package cn.flowboot.quartz.job.api;


import cn.flowboot.quartz.listener.MyTriggerListener;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class TriggerListenerTest {

    @Test
    public void execute() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //开启定时任务
        scheduler.start();

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("jobDetail","group1")
                .usingJobData("name","hello world")
                .build();
        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .build();
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener());
        //任务设置
        scheduler.scheduleJob(jobDetail,trigger);

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

```

## JobListener

相关事件：
- jobExecutionVetoed 忽略本次job
- jobToBeExecuted 即将执行
- jobWasExecuted 已经执行了

```java
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

```
测试
```java
package cn.flowboot.quartz.job.api;


import cn.flowboot.quartz.listener.MyJobListener;
import cn.flowboot.quartz.listener.MyTriggerListener;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class JobListenerTest {

    @Test
    public void execute() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //开启定时任务
        scheduler.start();

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("jobDetail","group1")
                .usingJobData("name","hello world")
                .build();
        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .build();
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener());
        scheduler.getListenerManager().addJobListener(new MyJobListener());
        //任务设置
        scheduler.scheduleJob(jobDetail,trigger);

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
```

### SchedulerListener
相关事件
- 添加/删除触发器
- Job暂停/恢复
- 调度器启动/关闭等

```java
package cn.flowboot.quartz.listener;

import org.quartz.*;
import org.quartz.listeners.SchedulerListenerSupport;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/26
 */
public class MySchedulerListener extends SchedulerListenerSupport {

    @Override
    public void jobAdded(JobDetail jobDetail) {
        super.jobAdded(jobDetail);
        System.out.println("【jobAdded|"+jobDetail.getKey()+"】");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        super.jobDeleted(jobKey);
        System.out.println("【jobDeleted|"+jobKey.getName()+"】");

    }

    @Override
    public void jobPaused(JobKey jobKey) {
        super.jobPaused(jobKey);
        System.out.println("【jobPaused|"+jobKey.getName()+"】");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        super.jobResumed(jobKey);
        System.out.println("【jobResumed|"+jobKey.getName()+"】");
    }

    @Override
    public void jobScheduled(Trigger trigger) {
        super.jobScheduled(trigger);
        System.out.println("【jobScheduled|"+trigger.getStartTime()+"】");
    }

    @Override
    public void jobsPaused(String jobGroup) {
        super.jobsPaused(jobGroup);
        System.out.println("【jobsPaused|"+jobGroup+"】");
    }

    @Override
    public void jobsResumed(String jobGroup) {
        super.jobsResumed(jobGroup);
        System.out.println("【jobsResumed|"+jobGroup+"】");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        super.jobUnscheduled(triggerKey);
        System.out.println("【jobUnscheduled|"+triggerKey.getName()+"】");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        super.schedulerError(msg, cause);
        System.out.println("【schedulerError|"+msg+"】");
    }

    @Override
    public void schedulerInStandbyMode() {
        super.schedulerInStandbyMode();
        System.out.println("【schedulerInStandbyMode】");
    }

    @Override
    public void schedulerShutdown() {
        super.schedulerShutdown();
        System.out.println("【schedulerShutdown】");
    }

    @Override
    public void schedulerShuttingdown() {
        super.schedulerShuttingdown();
        System.out.println("【schedulerShuttingdown】");
    }

    @Override
    public void schedulerStarted() {
        super.schedulerStarted();
        System.out.println("【schedulerStarted】");
    }

    @Override
    public void schedulerStarting() {
        super.schedulerStarting();
        System.out.println("【schedulerStarting】");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        super.triggerFinalized(trigger);
        System.out.println("【triggerFinalized|"+trigger.getEndTime()+"】");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        super.triggerPaused(triggerKey);
        System.out.println("【triggerPaused|"+triggerKey.getName()+"】");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        super.triggerResumed(triggerKey);
        System.out.println("【triggerResumed|"+triggerKey.getName()+"】");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        super.triggersPaused(triggerGroup);
        System.out.println("【triggersPaused|"+triggerGroup+"】");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        super.triggersResumed(triggerGroup);
        System.out.println("【triggersResumed|"+triggerGroup+"】");
    }

    @Override
    public void schedulingDataCleared() {
        super.schedulingDataCleared();
        System.out.println("【schedulingDataCleared】");
    }
}
```
测试,添加jvm设置关闭钩子
```java
package cn.flowboot.quartz.job.api;


import cn.flowboot.quartz.listener.MySchedulerListener;
import cn.flowboot.quartz.listener.MyTriggerListener;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <h1></h1>
 *
 * @version 1.0
 * @author: Vincent Vic
 * @since: 2022/01/24
 */
public class SchedulerListenerTest {

    @Test
    public void execute() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.getListenerManager().addSchedulerListener(new MySchedulerListener());
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    scheduler.shutdown();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        });
        //开启定时任务
        scheduler.start();

        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("jobDetail","group1")
                .usingJobData("name","hello world")
                .build();
        //创建触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever()
                )
                .build();
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener());
        //任务设置
        scheduler.scheduleJob(jobDetail,trigger);
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

```
