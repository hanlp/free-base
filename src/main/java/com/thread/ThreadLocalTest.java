package com.thread;

import com.google.common.collect.Maps;
import com.util.ThreadUtil;

import java.util.Map;

public class ThreadLocalTest {

    private static final ThreadLocal<Map<String, Long>> record_thread_local = ThreadLocal.withInitial(ThreadLocalTest::recordInit);

    private static Map<String, Long> recordInit() {
        Map<String, Long> map = Maps.newHashMap();
        map.put("start", System.currentTimeMillis());
        map.put("last", System.currentTimeMillis());
        return map;
    }

    private static void start() {
        record_thread_local.get();
    }

    private static void record(String method) {
        Map<String, Long> map = record_thread_local.get();
        Long last = map.get("last");
        map.put(method, System.currentTimeMillis() - last);
        map.put("last", System.currentTimeMillis());
        record_thread_local.set(map);
    }

    private static void release() {
        record_thread_local.remove();
    }


    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                InnerClassTest test = new InnerClassTest();
                ThreadLocalTest.start();
                test.serviceMethod();
                ThreadUtil.sleep(500);
                ThreadLocalTest.record("serviceMethod:cost ");

                ThreadLocalTest.printRecord();
                ThreadLocalTest.release();
            }
        });
        thread.start();
    }

    public static class InnerClassTest {

        private void serviceMethod() {
            doDao();
            doRpc();
        }

        private void doDao() {
            ThreadUtil.sleep(2000);
            ThreadLocalTest.record("doDao:cost ");

        }

        private void doRpc() {
            ThreadUtil.sleep(2500);
            ThreadLocalTest.record("doRpc:cost ");

        }
    }

    private static void printRecord() {
        Map<String, Long> map = record_thread_local.get();
        for (String key : map.keySet()) {
            System.out.println(key + "   " + map.get(key));
        }
    }
}
