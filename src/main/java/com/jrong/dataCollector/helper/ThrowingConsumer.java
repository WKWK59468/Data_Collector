package com.jrong.dataCollector.helper;

public interface ThrowingConsumer<T> {
    void accept(T t) throws Exception;
}
