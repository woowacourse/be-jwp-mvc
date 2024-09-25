package com.techcourse;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.techcourse.handler.ManualHandlerMappingAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingAdapter handlerMappingAdapter;

    @Override
    public void init() {
        handlerMappingAdapter = new ManualHandlerMappingAdapter();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final ModelAndView modelAndView = handlerMappingAdapter.adapt(request, response);
            final View view = modelAndView.getView();
            view.render(Map.of(), request, response);
        } catch (final Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
