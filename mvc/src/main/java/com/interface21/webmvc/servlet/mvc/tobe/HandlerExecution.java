package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    private HandlerExecution(final Method method, final Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public static HandlerExecution from(final Method method) {
        try {
            final Object instance = method.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();
            return new HandlerExecution(method, instance);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
