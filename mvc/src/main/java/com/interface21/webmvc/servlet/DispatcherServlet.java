package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.ModelAndViewHandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.ViewNameHandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;
    private final HandlerMappings handlerMappings = new HandlerMappings();
    private final HandlerAdapters handlerAdapters = new HandlerAdapters();

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        HandlerAdapter modelAndViewHandlerExecutionAdapter = new ModelAndViewHandlerExecutionAdapter();
        HandlerAdapter viewNameHandlerExecutionAdapter = new ViewNameHandlerExecutionAdapter();

        handlerMappings.appendHandlerMapping(annotationHandlerMapping);
        handlerAdapters.appendHandlerAdapter(modelAndViewHandlerExecutionAdapter);
        handlerAdapters.appendHandlerAdapter(viewNameHandlerExecutionAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView mv = handlerAdapter.invoke(handler, request, response);
            renderView(request, response, mv);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void renderView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        View view = mv.getView();
        view.render(mv.getModel(), request, response);
    }
}
