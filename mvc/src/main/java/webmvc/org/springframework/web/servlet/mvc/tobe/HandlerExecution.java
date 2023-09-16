package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;

    public HandlerExecution( final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object handler = method.getDeclaringClass().getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
