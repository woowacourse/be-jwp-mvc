package com.techcourse.servlet;

import com.techcourse.servlet.adaptor.HandlerAdaptorScanner;
import com.techcourse.servlet.handler.ManualHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private HandlerAdaptorScanner handlerAdaptorScanner;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping = new AnnotationHandlerMapping("com");
        handlerAdaptorScanner = new HandlerAdaptorScanner();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = manualHandlerMapping.getHandler(request);
            final var handlerAdaptor = handlerAdaptorScanner.getHandlerAdaptor(controller);
            final var viewName = handlerAdaptor.handle(controller, request, response);
            move(viewName, request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        if (view.isRedirectCommand()) {
            response.sendRedirect(view.getRedirectFilePath());
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(view.getViewName());
        requestDispatcher.forward(request, response);
    }
}
