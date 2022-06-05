package com.jvm.classloader;

public class ClassLoadTest {


    public static void main(String[] args) {
        System.out.println(Son.strSon);
    }



    public static class Supper {
        static {
            System.out.println("Supper...");
        }
    }

    public static class Father extends Supper {
        public final static String strFather = "Father.str...";
        static {
            System.out.println("Father....");
        }
    }

    public static class Son extends Father {
        public static String strSon = "Son.str...";

        static {
            System.out.println("Son...");
        }
    }
}
