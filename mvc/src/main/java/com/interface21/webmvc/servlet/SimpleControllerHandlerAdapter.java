package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final var controller = (Controller) handler;
        final var viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }
}
