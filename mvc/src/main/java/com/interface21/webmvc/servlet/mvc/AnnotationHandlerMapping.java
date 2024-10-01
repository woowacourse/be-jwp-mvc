package com.interface21.webmvc.servlet.mvc;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            initializeByController(controller);
        }
    }

    private void initializeByController(Class<?> controller)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method[] methods = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toArray(Method[]::new);
        Object baseInstance = controller.getDeclaredConstructor()
                .newInstance();

        for (Method method : methods) {
            initializeByMethod(method, baseInstance);
        }
    }

    private void initializeByMethod(Method method, Object baseInstance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] mappingMethods = getMappingMethods(requestMapping);

        for (RequestMethod requestMethod : mappingMethods) {
            log.info("Path : {}, Method: {}", requestMapping.value(), requestMethod);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution execution = new HandlerExecution(baseInstance, method);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    private RequestMethod[] getMappingMethods(RequestMapping requestMapping) {
        RequestMethod[] mappingMethods = requestMapping.method();
        if (mappingMethods.length == 0) {
            return RequestMethod.values();
        }
        return mappingMethods;
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), request.getMethod());

        return handlerExecutions.get(handlerKey);
    }
}
