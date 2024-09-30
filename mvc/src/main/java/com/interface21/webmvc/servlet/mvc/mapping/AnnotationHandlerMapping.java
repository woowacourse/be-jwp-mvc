package com.interface21.webmvc.servlet.mvc.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerKey;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    public static final String DEFAULT_BASE_PACKAGE = "com";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    private boolean initialized = false;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    protected AnnotationHandlerMapping() {
        this.basePackage = new Object[]{DEFAULT_BASE_PACKAGE};
    }

    public void initialize() {
        if (initialized) {
            log.warn("AnnotationHandlerMapping이 이미 초기화되었습니다.");
            return;
        }
        log.info("AnnotationHandlerMapping을 초기화했습니다.");

        Set<Class<?>> controllerClasses = findControllerClasses();
        for (Class<?> controllerClass : controllerClasses) {
            registerHandlerMethods(controllerClass);
        }

        initialized = true;
    }

    private Set<Class<?>> findControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerHandlerMethods(Class<?> controllerClass) {
        List<Method> newHandlerExecutions = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        Object controllerInstance = createControllerInstance(controllerClass);
        for (Method method : newHandlerExecutions) {
            List<HandlerKey> handlerKeys = createHandlerKeys(method);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.putIfAbsent(handlerKey, handlerExecution));
        }
    }

    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(
                    "컨트롤러 인스턴스화 실패: " + controllerClass.getName(), e);
        }
    }

    private List<HandlerKey> createHandlerKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
