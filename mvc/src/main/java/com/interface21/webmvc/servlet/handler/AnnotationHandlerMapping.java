package com.interface21.webmvc.servlet.handler;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String DELIMITER_PATH = "/";
    private static final String REGEX_MANY_PATH_DELIMITER = "/{2,}";
    private static final String DEFAULT_PATH = "";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).forEach(this::assignHandlerByClass);

        handlerExecutions.forEach((key, execution) -> log.info("Key : {}, Execution : {}", key, execution));
    }

    private void assignHandlerByClass(Class<?> clazz) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        String path = extractPath(annotation);

        for (Method method : clazz.getMethods()) {
            assignHandlerByMethod(clazz, method, path);
        }
    }

    private String extractPath(RequestMapping annotation) {
        return Optional.ofNullable(annotation)
                .map(RequestMapping::value)
                .orElse(DEFAULT_PATH);
    }

    private void assignHandlerByMethod(Class<?> clazz, Method method, String basePath) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            return;
        }
        String path = generateEndpoint(basePath, extractPath(annotation));

        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            validateDuplicated(handlerKey);
            HandlerExecution handlerExecution = findHandlerExecution(clazz, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private String generateEndpoint(String basePath, String subPath) {
        if (subPath.isBlank()) {
            return String.join(DELIMITER_PATH, DELIMITER_PATH, basePath)
                    .replaceAll(REGEX_MANY_PATH_DELIMITER, DELIMITER_PATH);
        }
        return String.join(DELIMITER_PATH, DELIMITER_PATH, basePath, subPath)
                .replaceAll(REGEX_MANY_PATH_DELIMITER, DELIMITER_PATH);
    }

    private void validateDuplicated(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("HandlerKey exists: " + handlerKey.toString());
        }
    }

    private HandlerExecution findHandlerExecution(Class<?> clazz, Method method) {
        try {
            return new HandlerExecution(method, clazz);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Public constructor not found: {}" + clazz);
        } catch (Exception e) {
            log.error("Failed to find HandlerExecution for class {}", clazz, e);
        }
        return null;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            return null;
        }

        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
