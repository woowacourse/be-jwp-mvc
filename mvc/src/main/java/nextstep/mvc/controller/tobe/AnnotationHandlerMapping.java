package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        loadHandlerExecutions();
    }

    private void loadHandlerExecutions() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            requestMapping(controller, methods);
        }
    }

    private void requestMapping(Class<?> controller, Set<Method> methods) {
        for (Method method : methods) {
            putHandlerExecution(controller, method);
        }
    }

    private void putHandlerExecution(Class<?> controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, controller);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.from(request.getRequestURI(), request.getMethod());
        return handlerExecutions.get(handlerKey);
    }
}
