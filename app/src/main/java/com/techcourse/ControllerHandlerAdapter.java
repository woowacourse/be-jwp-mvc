package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean supports(final Object handler) {
		return handler instanceof Controller;
	}

	@Override
	public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) throws Exception {
		Controller controller = (Controller)handler;
		String view = controller.execute(request, response);
		return new ModelAndView(new JspView(view));
	}
}
