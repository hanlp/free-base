package com.designmodel.masterworker;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.util.PrintUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务分发
 * @param <T>
 * @param <R>
 */
public class Master<T extends Task, R> {

    private Map<String, Worker<T, R>> workerMap = Maps.newHashMap();

    private LinkedBlockingQueue<T> taskQueue = new LinkedBlockingQueue();

    // TODO  HashMap 每次循环读结果3轮，线程不执行，换ConcurrentMap未复现，具体问题未查
//    private Map<String, R> resultMap = Maps.newHashMap();
    protected Map<String, R> resultMap = Maps.newConcurrentMap();

    private Thread thread;


    public Master(int workerCt) {
        PrintUtil.println("Master初始化 开始...");
        for (int i = 0; i < workerCt; i++) {
            Worker<T, R> worker = new Worker<>();
            workerMap.put("worker-" + i, worker);
        }
        thread = new Thread(() -> this.execute());
        thread.start();
        PrintUtil.println("Master初始化 结束...");
    }

    private void execute() {
        for (;;) {
            for (Map.Entry<String, Worker<T, R>> workerEntry : workerMap.entrySet()) {
                try {
                    T task = this.taskQueue.take();
                    Worker<T, R> worker = workerEntry.getValue();
                    worker.submit(task, this::resultCallback);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resultCallback(Object o) {
        try {
            Task<R> task = (Task<R>) o;
            String taskName = "Worker:" + task.getWorkerId() + "-Task:" + task.getId();
            R result = task.getResult();
            resultMap.put(taskName, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit(T task) {
        try {
            this.taskQueue.put(task);
            PrintUtil.println("向Master提交任务[{}]...", task.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printResult() {
        PrintUtil.println("--------------Master result-----------size={}", resultMap.size());
//        PrintUtil.println("Master.ResultMap={}", JSON.toJSONString(resultMap));

         for (Map.Entry<String, R> entry : resultMap.entrySet()) {
            PrintUtil.println("TaskName[{}]：Result[{}]", entry.getKey(), entry.getValue());
        }
    }

    private static ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

    public void checkDeadThread() {
        long[] deadlockedThreads = mxBean.findDeadlockedThreads();
        if (deadlockedThreads == null) {
            PrintUtil.println("Master檢查死鎖, 沒有死鎖....");
            return;
        }
        PrintUtil.println("Master檢查死鎖....");
        for (long pid : deadlockedThreads) {
            ThreadInfo threadInfo = mxBean.getThreadInfo(pid, Integer.MAX_VALUE);
            PrintUtil.println("================死鎖綫程================");
            PrintUtil.println(threadInfo);
        }
    }
}
