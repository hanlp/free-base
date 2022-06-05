package com.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * -Xms10M -Xmx10M  -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeepOOM {


    static class OOMObj {

    }

    public static void main(String[] args) {
        heapOOM();
    }

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     */
    private static void heapOOM() {
        List<OOMObj> list = new ArrayList<OOMObj>();
        while (true) {
            list.add(new OOMObj());
        }
    }

}
