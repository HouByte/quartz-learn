package cn.flowboot.quartz.job.api;

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
        SchedulerTestUtils.simpleTest(DisallowConcurrentExecutionJob.class,5);
    }
}
