package com.spring.customtag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        super.registerBeanDefinitionParser("user", new MyBeanDefinitionParser());
    }
}
