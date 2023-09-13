package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        final Class<?> controller = method.getDeclaringClass();
        final Constructor<?> constructor = controller.getConstructor();
        final Object controllerInstance = constructor.newInstance();

        return (ModelAndView) method.invoke(controllerInstance, request, response);
    }
}
