package com.lock;


import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer  implements Runnable {

    private AtomicInteger TURN = new AtomicInteger(0);
    private AtomicInteger NO = new AtomicInteger(1);
    private Callable action = null;

    private int gap = 200;

    private String name;

    public Producer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        name = "Producer-" + NO.getAndIncrement();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object call = action.call();
                if(call != null) {
                    PrintUtil.println("第{}轮生产：{}", TURN.get(), call);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TURN.incrementAndGet();
            ThreadUtil.sleep(gap);
        }
    }
}
