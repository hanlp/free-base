package com.spring.aop;

import com.util.PrintUtil;

public class AopTestBean {

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
    public void doExe() {
        PrintUtil.println("AopTestBean.doExe...");
    }
}
