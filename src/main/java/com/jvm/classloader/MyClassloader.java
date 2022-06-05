package com.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

public class MyClassloader extends ClassLoader {


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        String className = name.substring(name.lastIndexOf(".") + 1) + ".class";
        InputStream is = getClass().getResourceAsStream(className);
        if (is == null) {
            return super.loadClass(name);
        }

        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException("类未找到", e);
        }

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("findClass name=" + name);
        return super.findClass(name);
    }

}
