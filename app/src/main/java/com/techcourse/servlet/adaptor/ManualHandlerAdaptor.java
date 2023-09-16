package com.techcourse.servlet.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.MyController;

public class ManualHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof MyController;
    }

    @Override
    public ModelAndView handle(final Object controller, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final MyController interfaceMyController = (MyController) controller;
        return interfaceMyController.execute(request, response);
    }
}
