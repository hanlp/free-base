package com.designmodel.future;


import com.util.PrintUtil;
import com.util.ThreadUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        testCompletableFuture();
    }

    private static void testCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync(CompletableFutureDemo::rpc1);
        CompletableFuture<Object> future2 = CompletableFuture.supplyAsync(CompletableFutureDemo::rpc2);
        CompletableFuture<Object> future3 = future2.thenCombine(future1, (o, o2) -> o + " _ " + o2);
        Object result = future3.get();
        PrintUtil.println("最后执行结果：{}", result);
    }

    private static String rpc1() {
        ThreadUtil.sleep(2000);
        PrintUtil.println("模拟调用：1");
        return "result.from.server.1";
    }

    private static String rpc2() {
        ThreadUtil.sleep(5000);
        PrintUtil.println("模拟调用：2");
        return "result.from.server.2";
    }
}
