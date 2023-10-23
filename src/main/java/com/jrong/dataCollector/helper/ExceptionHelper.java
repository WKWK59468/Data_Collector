package com.jrong.dataCollector.helper;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ExceptionHelper {
    public static <T, E extends Exception> Consumer<T> lambdaWarpper(ThrowingConsumer<T> throwingConsumer, Class<E> clazz){
        return i -> {
            try {
                throwingConsumer.accept(i);
            }catch (Exception ex){
                try {
                    E cxCast = clazz.cast(ex);
                    System.err.println("Runtime Exception : " + cxCast.getMessage());
                }catch (ClassCastException e){
                    throw e;
                }
            }
        };
    }
}
