package com.interface21;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.HandlerMappingRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;
    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(
                new AnnotationHandlerMapping(basePackage));
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}