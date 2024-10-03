package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();

        initialize();
    }

    @Override
    public void initialize() {
        try {
            processControllers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void processControllers() throws Exception {
        for (Class<?> controller : findControllers()) {
            processController(controller);
        }
    }

    private Set<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void processController(Class<?> controller) throws Exception {
        Object runnerInstance = controller.getDeclaredConstructor().newInstance();

        for (Method handlerMethod : findHandlerMethods(controller)) {
            appendRequestMapping(handlerMethod, runnerInstance);
        }
    }

    private List<Method> findHandlerMethods(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void appendRequestMapping(Method handlerMethod, Object runnerInstance) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        if (isRequestMethodEmpty(requestMethods)) {
            putHandlerExecutions(handlerMethod, runnerInstance, requestMapping.value(), RequestMethod.values());
            return;
        }

        putHandlerExecutions(handlerMethod, runnerInstance, requestMapping.value(), requestMapping.method());
    }

    private boolean isRequestMethodEmpty(RequestMethod[] requestMethods) {
        return requestMethods.length == 0;
    }

    private void putHandlerExecutions(
            Method handlerMethod,
            Object runnerInstance,
            String path,
            RequestMethod[] requestMethods
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(runnerInstance, handlerMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
