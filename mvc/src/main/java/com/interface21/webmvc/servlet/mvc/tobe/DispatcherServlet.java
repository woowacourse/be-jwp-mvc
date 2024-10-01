package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdaptorRegistry handlerAdaptorRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdaptorRegistry = new HandlerAdaptorRegistry();
    }

    @Override
    public void init() {
        initializeHandlerMappings();
        initializeHandlerAdaptors();
    }

    private void initializeHandlerMappings() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.add(annotationHandlerMapping);
    }

    private void initializeHandlerAdaptors() {
        handlerAdaptorRegistry.add(new AnnotationHandlerAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappingRegistry.findHandler(request);
            HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.findHandlerAdaptor(handler);
            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);

            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalStateException("해당 model과 view로 렌더링을 할 수 없습니다. " + e.getCause().getMessage());
        }
    }
}
