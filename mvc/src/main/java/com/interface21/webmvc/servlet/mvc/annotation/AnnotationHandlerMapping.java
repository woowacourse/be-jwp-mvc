package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (var controller : ControllerScanner.getControllerClass(basePackage)) {
            registerController(controller);
        }
    }

    private void registerController(Class<?> controllerClass) {
        List<Method> handlerMethods = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
        for (Method handlerMethod : handlerMethods) {
            RequestMapping requestMappingAnnotation = handlerMethod.getAnnotation(RequestMapping.class);
            String requestUrl = requestMappingAnnotation.value();
            bindControllerToRequest(controllerClass, handlerMethod, requestUrl,
                    getRequestMethods(requestMappingAnnotation));
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMappingAnnotation) {
        if (requestMappingAnnotation.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMappingAnnotation.method();
    }

    private void bindControllerToRequest(Class<?> controllerClass, Method method, String requestUrl,
                                         RequestMethod... requestMethods
    ) {
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(requestUrl, requestMethod),
                    new HandlerExecution(controllerClass, method));
            log.info("map method {}() to {} {}", method.getName(), requestMethod.name(), requestUrl);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
