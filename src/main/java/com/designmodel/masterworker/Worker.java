package com.designmodel.masterworker;


import com.util.PrintUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 任务执行
 */
public class Worker<T extends Task, R> {

    private LinkedBlockingQueue<T> taskQueue = new LinkedBlockingQueue();

    private Thread thread = null;

    private int workerId;

    private static AtomicInteger no = new AtomicInteger();

    public Worker() {
        this.workerId = no.incrementAndGet();
        thread = new Thread(this::doRun);
        thread.start();
    }

    private void doRun() {
        for (;;) {
            try {
                T task = this.taskQueue.take();
                task.setWorkerId(workerId);
                task.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void submit(T task, Consumer<Task<R>> action) {
        task.setResultAction(action);
        try {
            this.taskQueue.put(task);
            PrintUtil.println("向Worker[{}]提交任务[{}]...", this.workerId, task.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
