package com.interface21.webmvc.servlet.mvc.tobe.handlerMapping;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        try {
            initializeMappingInformation(reflections);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeMappingInformation(Reflections reflections)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            Method[] declaredMethods = controllerClass.getDeclaredMethods();
            initializeRequestMappingMethod(controllerClass, declaredMethods);
        }
    }

    private void initializeRequestMappingMethod(Class<?> controllerClass, Method[] declaredMethods)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Method declaredMethod : declaredMethods) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            putMappingHandlerExecution(controllerClass, declaredMethod, annotation);
        }
    }

    private void putMappingHandlerExecution(Class<?> controllerClass, Method declaredMethod, RequestMapping annotation)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (annotation != null) {
            List<RequestMethod> annotationMethodTypes = findSupportedRequestMethods(annotation);
            Object controllerClassInstance = controllerClass.getDeclaredConstructor().newInstance();
            for (RequestMethod annotationMethodType : annotationMethodTypes) {
                HandlerExecution handlerExecution = new HandlerExecution(controllerClassInstance, declaredMethod);
                HandlerKey handlerKey = new HandlerKey(annotation.value(), annotationMethodType);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private List<RequestMethod> findSupportedRequestMethods(RequestMapping annotation) {
        RequestMethod[] methodTypes = annotation.method();
        if (methodTypes.length == 0) {
            methodTypes = RequestMethod.values();
        }
        return Arrays.stream(methodTypes).toList();
    }

    @Override
    public boolean isSupported(HttpServletRequest request) {
        return handlerExecutions.containsKey(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("등록되지않은 요청URI와 MethodType입니다.");
        }
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
