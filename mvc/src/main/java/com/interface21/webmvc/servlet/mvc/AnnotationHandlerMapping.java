package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ClassScanner classScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.classScanner = new ClassScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        List<Method> methods = classScanner.findHandlingMethods();
        methods.forEach(this::addHandlerExecutions);

        handlerExecutions.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, handlerExecutions.get(path).getClass()));
    }

    private void addHandlerExecutions(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        Arrays.stream(requestMethods)
                .forEach(requestMethod -> addHandlerExecution(method, requestMapping.value(), requestMethod));
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void addHandlerExecution(Method method, String uri, RequestMethod requestMethod) {
        Object handlerInstance = createHandlerInstance(method.getDeclaringClass());
        HandlerKey handlerKey = HandlerKey.of(uri, requestMethod);
        validateHandlerKey(handlerKey);
        HandlerExecution handlerExecution = new HandlerExecution(method, handlerInstance);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Object createHandlerInstance(Class<?> clazz) {
        if (instances.containsKey(clazz)) {
            return instances.get(clazz);
        }
        try {
            instances.put(clazz, clazz.getConstructor().newInstance());
            return instances.get(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance for class: " + clazz.getName());
        }
    }

    private void validateHandlerKey(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("HandlerExecution mappings already exists with handler key: " + handlerKey.toString());
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.find(request.getMethod());
        HandlerKey handlerKey = HandlerKey.of(uri, method);

        return handlerExecutions.get(handlerKey);
    }
}
