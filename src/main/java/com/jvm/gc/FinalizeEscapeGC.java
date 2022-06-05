package com.jvm.gc;

/**
 * 演示兩點
 * 1、對象在GC時自我拯救
 * 2、這種拯救機會只有一次，因為finalize方法只能被系統調用一次
 */
public class FinalizeEscapeGC {

    private static FinalizeEscapeGC SAVE_HOOK = null;


    private void isAlive() {
        System.out.println("i am alive....");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": execute finalize method done...");
        FinalizeEscapeGC.SAVE_HOOK  = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("i am dead....");
        }



        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("i am dead....");
        }




    }
}
