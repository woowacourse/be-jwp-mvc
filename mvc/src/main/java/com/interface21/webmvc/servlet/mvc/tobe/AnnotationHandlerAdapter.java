package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter<HandlerExecution> {

    @Override
    public Class<HandlerExecution> getSupportedClass() {
        return HandlerExecution.class;
    }

    @Override
    public ModelAndView handle(Object handlerExecution, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        return ((HandlerExecution) handlerExecution).handle(request, response);
    }
}
