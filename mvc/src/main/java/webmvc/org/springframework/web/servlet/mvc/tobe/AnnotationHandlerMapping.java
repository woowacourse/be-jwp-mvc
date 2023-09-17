package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    private final AnnotationScanner annotationScanner;
    private final HandlerKeyGenerator handlerKeyGenerator;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this(Map.of(), new AnnotationScanner(basePackage), new HandlerKeyGenerator());
    }

    public AnnotationHandlerMapping(
            Map<HandlerKey, HandlerExecution> handlerExecutions,
            AnnotationScanner annotationScanner,
            HandlerKeyGenerator handlerKeyGenerator
    ) {
        this.handlerExecutions.putAll(handlerExecutions);
        this.annotationScanner = annotationScanner;
        this.handlerKeyGenerator = handlerKeyGenerator;
    }

    public void initialize() {
        final Map<Class<?>, ControllerInstance> controllers = annotationScanner.scanControllers();
        final Set<Method> methods = annotationScanner.scanHttpMappingMethods(controllers.keySet());

        for (final Method method : methods) {
            final ControllerInstance controller = controllers.get(method.getDeclaringClass());
            final HandlerExecution handlerExecution = new HandlerExecution(controller.getInstance(), method);
            final List<HandlerKey> handlerKeys = handlerKeyGenerator.generate(controller.getUriPrefix(), method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
