package com.designmodel.masterworker;

import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.TimeUnit;

/**
 * Master-Worker模式测试
 */
public class MasterWorkerTest {

    private static class SimpleTask extends Task<Integer> {
        @Override
        protected Integer doExecute() {
            PrintUtil.println("Task [{}] is done...", getId());
            return getId();
        }
    }

    public static void main(String[] args) {

        Master<SimpleTask, Integer> master = new Master<>(4);


        ThreadUtil.scheduleAtFixedRate(() -> master.checkDeadThread(), 3, TimeUnit.SECONDS);
        ThreadUtil.scheduleAtFixedRate(() -> master.submit(new SimpleTask()), 2, TimeUnit.SECONDS);
        ThreadUtil.scheduleAtFixedRate(master::printResult, 5, TimeUnit.SECONDS);
    }
}
