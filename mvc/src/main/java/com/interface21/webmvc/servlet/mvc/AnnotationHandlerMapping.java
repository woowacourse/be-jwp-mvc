package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.containsKey(key);
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach((k, v) -> log.info("Found Controller: {}, {}", k, v));
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
        requestMappingMethods.forEach(method -> addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class)));
        handlerExecutions.forEach((k, v) -> log.info("HandlerExecution: {}, {}", k, v));
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        return classes.stream().flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class)))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        String url = requestMapping.value();
        List<RequestMethod> methods = getMethods(requestMapping);
        List<HandlerKey> handlerKeys = mapHandlerKeys(url, methods);
        log.info("HandlerKeys: {}", handlerKeys);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(
                handlerKey, new HandlerExecution(controllers.get(method.getDeclaringClass()), method)));
    }

    private List<RequestMethod> getMethods(RequestMapping requestMapping) {
        List<RequestMethod> uniqueRequestMethods = Arrays.stream(requestMapping.method()).distinct().toList();
        if (uniqueRequestMethods.isEmpty()) {
            return Arrays.stream(RequestMethod.values()).toList();
        }
        return uniqueRequestMethods;
    }

    private List<HandlerKey> mapHandlerKeys(String url, List<RequestMethod> methods) {
        return methods.stream().map(method -> new HandlerKey(url, method)).toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
