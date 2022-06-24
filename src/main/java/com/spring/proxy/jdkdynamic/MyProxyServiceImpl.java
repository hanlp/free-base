package com.spring.proxy.jdkdynamic;

import com.util.PrintUtil;

public class MyProxyServiceImpl implements MyProxyService {

    @Override
    public void say() {
        PrintUtil.println("say bye...");
    }
}
