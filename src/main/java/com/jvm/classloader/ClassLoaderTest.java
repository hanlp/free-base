package com.jvm.classloader;

import sun.misc.Launcher;

import java.io.File;
import java.util.List;

public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
//        printClassloader();
//        printLauncherPath();
//        printExtLoaderPath();
//        printAppLoaderPath();
//        ClassLoaderTest classLoaderTest = classForName();
//        classLoaderTest.innerTest();

//        ClassLoaderTest classLoaderTest2 = classForClassLoader();
//        classLoaderTest2.innerTest();



//        loadClassProcess();

        myClassloader();

    }

    private static void myClassloader() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassloader myClassloader = new MyClassloader();
        Class<?> aClass = myClassloader.loadClass(ClassLoaderTest.class.getName());
        Object myObject = aClass.newInstance();
        System.out.println("myObject.classloader：\n" + myObject.getClass().getClassLoader());
        boolean  bo = myObject instanceof com.jvm.classloader.ClassLoaderTest;
        System.out.println("ClassLoaderTest.classloader：\n" + ClassLoaderTest.class.getClassLoader());
        System.out.println(bo);
    }

    private static void loadClassProcess() throws Exception {
        String className = Demo.class.getName();
        System.out.println(className);
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
//        classLoader.loadClass(className);

//        Class.forName(className);
        Class<?> demoClass = Class.forName(className, false, classLoader);
        Object o = demoClass.newInstance();
    }

    private static ClassLoaderTest classForClassLoader() throws Exception {
        return (ClassLoaderTest)Thread.currentThread().getContextClassLoader().loadClass(ClassLoaderTest.class.getName()).newInstance();
    }

    private static ClassLoaderTest classForName() throws Exception {
        Class<?> aClass = Class.forName(ClassLoaderTest.class.getName());

        return (ClassLoaderTest)aClass.newInstance();
    }

    private static void printAppLoaderPath() {
        String property = System.getProperty("java.class.path");
        String str = replaceBr(property);
        System.out.println(str);
    }

    private static void printExtLoaderPath() {
        String property = System.getProperty("java.ext.dirs");
        String str = replaceBr(property);
        System.out.println(str);
    }

    private static String replaceBr(String str) {
        return str.replace(";", "\n");
    }

    private static void printLauncherPath() {
        String property = System.getProperty("sun.boot.class.path");
        String str = replaceBr(property);
        System.out.println(str);
    }

    /**
     * sun.misc.Launcher$AppClassLoader@14dad5dc
     * sun.misc.Launcher$ExtClassLoader@28d93b30
     * null
     */
    private static void printClassloader() {
        ClassLoader contextClassLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(contextClassLoader.toString());

        System.out.println(contextClassLoader.getParent().toString());
        System.out.println(contextClassLoader.getParent().getParent());

    }

    private void innerTest() {
        System.out.println("inner test...");
    }

    public static class Demo {

        static {
            System.out.println("demo static...");
        }

        public Demo() {
            System.out.println("demo init...");
        }
    }

    public static class SubMainDemo {

        public static void main(String[] args) {
            System.out.println("subMainDemo main...");

        }
        static {
            System.out.println("subMainDemo static...");
        }


        public SubMainDemo() {
            System.out.println("SubMainDemo init...");
        }
    }

}

