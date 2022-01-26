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
