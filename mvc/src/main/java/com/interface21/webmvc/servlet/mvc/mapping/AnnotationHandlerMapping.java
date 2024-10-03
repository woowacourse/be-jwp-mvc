package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.exception.HandlerInitializationException;
import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    @Override
    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::addHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }

    private void addHandlers(final Class<?> controllerType, final Object instance) {
        try {
            Arrays.stream(controllerType.getMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandler(method, instance));
        } catch (Exception e) {
            throw new HandlerInitializationException("핸들러 초기화에서 예외가 발생했습니다.", e);
        }
    }

    private void addHandler(final Method method, final Object controllerInstance) {
        HandlerExecution handlerExecution = createHandlerExecution(method, controllerInstance);
        createHandlerKeys(method).forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        return Arrays.stream(getMappingMethods(requestMapping))
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toList();
    }

    private RequestMethod[] getMappingMethods(RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    private HandlerExecution createHandlerExecution(final Method method, final Object controller) {
        return new HandlerExecution(method, controller);
    }
}
