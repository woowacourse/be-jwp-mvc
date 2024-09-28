package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        handlerAdapter.handle(handler, request, response);
    }
}
