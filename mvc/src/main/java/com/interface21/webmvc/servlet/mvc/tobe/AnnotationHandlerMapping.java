package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final List<Class<?>> AVAILABLE_PACKAGE_TYPE = List.of(String.class, Class.class);
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        validatePackageTypes(basePackages);
        this.basePackages = basePackages;
        this.handlerExecutions = new HandlerExecutions();
    }

    private void validatePackageTypes(Object[] basePackages) {
        if (basePackages == null) {
            throw new IllegalArgumentException("basePackages는 null 값이 될 수 없습니다");
        }
        for (Object basePackage : basePackages) {
            validatePackageType(basePackage);
        }
    }

    private void validatePackageType(Object basePackage) {
        if (basePackage == null) {
            throw new IllegalArgumentException("basePackage는 null 값이 될 수 없습니다");
        }
        if (basePackage != null & !AVAILABLE_PACKAGE_TYPE.contains(basePackage.getClass())) {
            throw new IllegalArgumentException("지원하지 않은 패키지 타입입니다 : [%s]".formatted(basePackage.getClass().toString()));
        }
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllerClasses) {
            log.info("Initialized AnnotationHandlerMapping! - {}", controller);
            addAnnotatedHandlerExecutions(controller);
        }
    }

    private void addAnnotatedHandlerExecutions(Class<?> controller) {
        Object executionTarget = constructExecutionTarget(controller);
        List<Method> annotatedMethods = searchRequestMappingAnnotation(controller);
        annotatedMethods
                .forEach(method -> handlerExecutions.addHandlerExecution(executionTarget, method));
    }

    private Object constructExecutionTarget(Class<?> controller) {
        try {
            Constructor<?> firstConstructor = controller.getDeclaredConstructor();
            return firstConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "default constructor가 존재하지 않습니다 %s".formatted(controller.getCanonicalName()));
        } catch (Exception e) {
            throw new IllegalArgumentException("HandlerMapping을 초기화 하는데 실패했습니다.");
        }
    }

    private List<Method> searchRequestMappingAnnotation(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.findMethod(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
