package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.disapatchersevlet.HandlerAdapter;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupporting(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return  handlerExecution.handle(request, response);
    }
}
