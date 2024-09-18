package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    private HandlerExecution(Method method) {
        this.method = method;
    }

    public static HandlerExecution from(final Method method) {
        return new HandlerExecution(method);
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object instance = method.getDeclaringClass()
                .getDeclaredConstructor()
                .newInstance();

        return (ModelAndView) method.invoke(instance, request, response);
    }
}
