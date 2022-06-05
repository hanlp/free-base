package com.java.base;

/**
 * final 关键字
 */
public class FinalTest {

    //普通域
    private int a;
    //final域
    private final int b;


    private static FinalTest finalDemo;

    public FinalTest() {
        // 1. 写普通域
        a = 1;
        // 2. 写final域
        b = 2;
    }

    public static void writer() {
        finalDemo = new FinalTest();
    }

    public static void reader() {
        // 3.读对象引用
        FinalTest demo = finalDemo;
        //4.读普通域
        int a = demo.a;
        System.out.println("a=" + a);

        //5.读final域
        int b = demo.b;
        System.out.println("b=" + b);
    }

    public static void main(String[] args) {
//        writer();
//        reader();

        final byte a = 1;
        final byte b = 2;
        byte c = a + b;



    }

}
