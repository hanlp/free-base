package com.jvm.memory;

import java.util.ArrayList;
import java.util.List;

public class JvmMemoryTest {

    public static void main(String[] args) {
        forCreateObj();
    }

    /**
     * java.lang.OutOfMemoryError: GC overhead limit exceeded
     */
    private static void forCreateObj() {

        List<List<String>> list = new ArrayList<List<String>>();
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
            ArrayList<String> strings = new ArrayList<String>();
            strings.add(i + "");
            list.add(strings);
        }

    }
}
