package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingCreationException;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.reflections.Reflections;

public class HandlerMappingRegistry {

    private static final String BASE_PACKAGE = "com.interface21.webmvc.servlet.mvc.tobe.mapping";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public HandlerMappingRegistry() {
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        Reflections reflections = new Reflections(BASE_PACKAGE);
        reflections.getSubTypesOf(HandlerMapping.class)
                .stream()
                .map(this::createHandlerMappingInstance)
                .forEach(handlerMapping -> {
                    handlerMapping.initialize();
                    handlerMappings.add(handlerMapping);
                });
    }

    private HandlerMapping createHandlerMappingInstance(Class<? extends HandlerMapping> handlerMappingClass) {
        try {
            return createWithConstructor(handlerMappingClass);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new HandlerMappingCreationException(e.getMessage(), e);
        }
    }

    private HandlerMapping createWithConstructor(Class<? extends HandlerMapping> handlerMappingClass)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return handlerMappingClass.getDeclaredConstructor().newInstance();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(HandlerMappingNotFoundException::new);
    }
}
