package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Entry<Class<?>, Object> controllerObject : controllers.entrySet()) {
            final Set<Method> methods = getHandlerMethods(controllerObject.getKey());
            final Map<Method, Set<HandlerKey>> handlerKeysPerMethod = getHandlerKeysPerMethod(methods);
            mapHandlerExecutions(controllerObject.getValue(), handlerKeysPerMethod);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }

    private Set<Method> getHandlerMethods(final Class<?> controllerClass) {
        return new Reflections(controllerClass, Scanners.MethodsAnnotated)
                .getMethodsAnnotatedWith(RequestMapping.class);
    }

    private Map<Method, Set<HandlerKey>> getHandlerKeysPerMethod(final Set<Method> methods) {
        return methods.stream()
                .collect(Collectors.toMap(
                        method -> method,
                        HandlerKey::allFrom
                ));
    }

    private void mapHandlerExecutions(final Object controller,
                                      final Map<Method, Set<HandlerKey>> handlerKeysPerMethod) {
        for (Entry<Method, Set<HandlerKey>> handlerKeys : handlerKeysPerMethod.entrySet()) {
            final Method method = handlerKeys.getKey();
            final Set<HandlerKey> keys = handlerKeys.getValue();
            keys.forEach(
                    handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method))
            );
        }
    }
}
