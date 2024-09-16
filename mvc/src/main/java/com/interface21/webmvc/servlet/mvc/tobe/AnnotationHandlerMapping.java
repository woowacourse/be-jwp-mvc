package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.exception.HandlerMappingInitializeException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = createReflections();
        reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .forEach(this::addHandlerExecution);
    }

    private Reflections createReflections() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        for (String packageName : basePackage) {
            configurationBuilder.setUrls(ClasspathHelper.forPackage(packageName))
                    .filterInputsBy(new FilterBuilder().includePackage(packageName));
        }
        return new Reflections(basePackage);
    }

    private void addHandlerExecution(Class clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(clazz, method));
    }

    private void addHandlerExecution(Class clazz, Method method) {
        try {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String paths = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            Object instance = clazz.getDeclaredConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            Arrays.stream(requestMethods)
                    .map(requestMethod -> new HandlerKey(paths, requestMethod))
                    .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        } catch (Exception e) {
            throw new HandlerMappingInitializeException(e);
        }
    }

    @Nullable
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), request.getMethod());
        return handlerExecutions.get(handlerKey);
    }
}
