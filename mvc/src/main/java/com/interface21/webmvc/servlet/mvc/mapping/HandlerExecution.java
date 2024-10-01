package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("Handling request: {} {}", request.getMethod(), request.getRequestURI());
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("핸들러 메서드 호출 중 오류가 발생했습니다.", e);
        }
    }
}
