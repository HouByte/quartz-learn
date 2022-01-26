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
