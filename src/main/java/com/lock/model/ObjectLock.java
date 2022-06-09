package com.lock.model;


import com.util.PrintUtil;
import com.util.ThreadUtil;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

public class ObjectLock {
    /**
     * 整型字段占用4字节
     */
    private Integer amount = 0;

//    private String name;
//    private int age;
//    private double a;
//    private boolean b;
//    private byte aByte;
//    private short aShort;
//    private char aChar;
//
//    private long aLong;
//    private float aFloat;


    public void inc() {
        synchronized (this) {
            amount++;
        }
    }

    /**
     * 输出十六进制、小端模式的hashCode
     *
     * @return
     */
    public String hexHash() {
        int hashCode = this.hashCode();
//        byte[] hashCode_LE = ByteUtils.int2Bytes_LE(hashCode);
        return hashCode + "";
    }

    public void printSelf() {
        String printable = ClassLayout.parseInstance(this).toPrintable();
        PrintUtil.println("lock={}", printable);
    }

    /**
     * 偏向锁： 101
     * # Running 64-bit HotSpot VM.
     * # Using compressed oop with 0-bit shift.
     * # Using compressed klass with 3-bit shift.
     * # Objects are 8 bytes aligned.
     * # Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
     * # Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
     *
     * 抢占锁前，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           05 c1 00 20 (00000101 11000001 00000000 00100000) (536920325)
     *      12     4   java.lang.Integer ObjectLock.amount                         0
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * 占有锁，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           05 00 37 1a (00000101 00000000 00110111 00011010) (439812101)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           05 c1 00 20 (00000101 11000001 00000000 00100000) (536920325)
     *      12     4   java.lang.Integer ObjectLock.amount                         1
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * 释放锁后，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           05 00 37 1a (00000101 00000000 00110111 00011010) (439812101)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           05 c1 00 20 (00000101 11000001 00000000 00100000) (536920325)
     *      12     4   java.lang.Integer ObjectLock.amount                         1
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     */
    private static void showBiasedLock() throws InterruptedException {
        PrintUtil.println(VM.current().details());
        ThreadUtil.sleep(5000);
        ObjectLock lock = new ObjectLock();

        PrintUtil.println("抢占锁前，lock状态：");
        lock.printSelf();
        ThreadUtil.sleep(5000);
        int ct = 1;
        CountDownLatch latch = new CountDownLatch(ct);
        Runnable runnable = () -> {
            for (int i = 0; i < ct; i++) {
                synchronized (lock) {
                    lock.inc();
                    if (i == ct / 2) {
                        PrintUtil.println("占有锁，lock状态：");
                        lock.printSelf();
                    }
                }
                ThreadUtil.sleep(5000);
                latch.countDown();
            }
        };
        new Thread(runnable, "biased-demo-thread").start();
        latch.await();
        ThreadUtil.sleep(5000);
        PrintUtil.println("释放锁后，lock状态：");
        lock.printSelf();

    }

    /**
     * 轻量级锁： 101 到 000
     抢占锁前，lock状态：
     lock=com.lock.model.ObjectLock object internals:
     OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     0     4                     (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
     4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     12     4   java.lang.Integer ObjectLock.amount                         0
     Instance size: 16 bytes
     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

     biased-demo-thread-1:占有锁，lock状态：
     lock=com.lock.model.ObjectLock object internals:
     OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     0     4                     (object header)                           05 c8 3c 1a (00000101 11001000 00111100 00011010) (440190981)
     4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     12     4   java.lang.Integer ObjectLock.amount                         1
     Instance size: 16 bytes
     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

     biased-demo-thread-2:占有锁，lock状态：
     lock=com.lock.model.ObjectLock object internals:
     OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     0     4                     (object header)                           38 ef 8f 1b (00111000 11101111 10001111 00011011) (462417720)
     4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     12     4   java.lang.Integer ObjectLock.amount                         2
     Instance size: 16 bytes
     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

     biased-demo-thread-1:占有锁，lock状态：
     lock=com.lock.model.ObjectLock object internals:
     OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     0     4                     (object header)                           18 ee 7f 1b (00011000 11101110 01111111 00011011) (461368856)
     4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     12     4   java.lang.Integer ObjectLock.amount                         3
     Instance size: 16 bytes
     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    private static void showLightweightLock() throws InterruptedException {
        PrintUtil.println(VM.current().details());
        ThreadUtil.sleep(5000);
        ObjectLock lock = new ObjectLock();

        PrintUtil.println("抢占锁前，lock状态：");
        lock.printSelf();
        ThreadUtil.sleep(5000);
        int ct = 2;
        int turn = 5;
        CountDownLatch latch = new CountDownLatch(ct);
        Runnable runnable = () -> {
            for (int i = 0; i < turn; i++) {
                synchronized (lock) {
                    lock.inc();
                    PrintUtil.println("{}:占有锁，lock状态：", Thread.currentThread().getName());
                    lock.printSelf();
                }
                ThreadUtil.sleep(5000);
            }
            latch.countDown();
        };
        new Thread(runnable, "lightweight-thread-1").start();
        ThreadUtil.sleep(1000);
        new Thread(runnable, "lightweight-thread-2").start();
        latch.await();
        ThreadUtil.sleep(5000);
        PrintUtil.println("释放锁后，lock状态：");
        lock.printSelf();
    }

    /**
     * 重量级锁： 101 到 000 到 010
     * # Running 64-bit HotSpot VM.
     * # Using compressed oop with 0-bit shift.
     * # Using compressed klass with 3-bit shift.
     * # Objects are 8 bytes aligned.
     * # Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
     * # Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
     *
     * 抢占锁前，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     *      12     4   java.lang.Integer ObjectLock.amount                         0
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * heavyweight-thread-1:占有锁，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           05 70 41 1a (00000101 01110000 01000001 00011010) (440496133)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     *      12     4   java.lang.Integer ObjectLock.amount                         1
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * heavyweight-thread-2:占有锁，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           b8 f3 92 1b (10111000 11110011 10010010 00011011) (462615480)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     *      12     4   java.lang.Integer ObjectLock.amount                         2
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * heavyweight-thread-2:占有锁，lock状态：
     * lock=com.lock.model.ObjectLock object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           6a ba 08 03 (01101010 10111010 00001000 00000011) (50903658)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           54 c3 00 20 (01010100 11000011 00000000 00100000) (536920916)
     *      12     4   java.lang.Integer ObjectLock.amount                         3
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     */
    private static void showHeavyweightLock() throws InterruptedException {
        PrintUtil.println(VM.current().details());
        ThreadUtil.sleep(5000);
        ObjectLock lock = new ObjectLock();

        PrintUtil.println("抢占锁前，lock状态：");
        lock.printSelf();
        ThreadUtil.sleep(5000);
        int ct = 2;
        int turn = 100;
        CountDownLatch latch = new CountDownLatch(ct);
        Runnable runnable = () -> {
            for (int i = 0; i < turn; i++) {
                synchronized (lock) {
                    lock.inc();
                    PrintUtil.println("{}:占有锁，lock状态：", Thread.currentThread().getName());
                    lock.printSelf();
                }
                if ("heavyweight-thread-1".equals(Thread.currentThread().getName())) {
                    while (true) {
                        ThreadUtil.sleep(2);
                    }
                } else {
                    ThreadUtil.sleep(1);
                }
            }
            latch.countDown();
        };
        new Thread(runnable, "heavyweight-thread-1").start();
        ThreadUtil.sleep(1000);
        new Thread(runnable, "heavyweight-thread-2").start();
        ThreadUtil.sleep(10);
        new Thread(runnable, "heavyweight-thread-3").start();
        latch.await();
        ThreadUtil.sleep(5000);
        PrintUtil.println("释放锁后，lock状态：");
        lock.printSelf();
    }



    public static void main(String[] args) throws Exception {
//        showBiasedLock();
//        showLightweightLock();
        showHeavyweightLock();
    }
}
