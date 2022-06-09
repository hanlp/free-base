package com.lock;

public interface DataBuffer<T> {

    T add(T t) throws Exception;
    
    T fetchOne() throws Exception;
}
