package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            for (Object targetPackage : basePackage) {
                handleMappingSinglePackage(targetPackage);
            }
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            log.error("AnnotationHandlerMapping Failed!");
        }
    }

    private void handleMappingSinglePackage(Object targetPackage) throws Exception {
        final Reflections reflections = new Reflections(targetPackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            handleMappingSingleController(controller);
        }
    }

    private void handleMappingSingleController(Class<?> controller) throws Exception {
        final Object controllerInstance = controller.getConstructor().newInstance();
        final Method[] methods = controller.getMethods();
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (!Objects.isNull(requestMapping)) {
                registerHandler(controllerInstance, method, requestMapping);
            }
        }
    }

    private void registerHandler(Object controllerInstance, Method method, RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method, controllerInstance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
