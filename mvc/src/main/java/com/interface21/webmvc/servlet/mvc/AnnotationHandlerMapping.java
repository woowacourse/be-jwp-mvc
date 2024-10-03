package com.interface21.webmvc.servlet.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<RequestMapping> REQUEST_MAPPING = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    private void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.getControllers().forEach(this::mapRequestMappingToHandler);
    }

    private void mapRequestMappingToHandler(Class<?> controller) {
        List<Method> requestMappings = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING))
                .toList();

        Object controllerInstance = ClassInstantiator.Instantiate(controller);
        requestMappings.forEach(method -> registerHandler(controllerInstance, method));
    }

    private void registerHandler(Object controllerInstance, Method method) {
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(method.getAnnotation(REQUEST_MAPPING));
        List<HandlerKey> handlerKeys = requestMappingInfo.getHandlerKeys();

        handlerKeys.forEach(key -> {
            HandlerExecution execution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(key, execution);
        });
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return handlerExecutions.containsKey(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return Optional.ofNullable(handlerExecutions.get(handlerKey))
                .orElseThrow(() -> new IllegalArgumentException("Annotation Mapping에 해당 요청을 처리할 수 있는 핸들러가 없습니다."));
    }
}
