package com.interface21.webmvc.servlet.mvc.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
