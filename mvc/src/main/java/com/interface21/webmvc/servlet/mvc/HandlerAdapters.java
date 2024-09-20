package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        List<HandlerAdapter> objects = reflections.getSubTypesOf(HandlerAdapter.class).stream()
                .map(this::createHandlerAdapter)
                .toList();
        handlerAdapters.addAll(objects);
    }

    private HandlerAdapter createHandlerAdapter(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return (HandlerAdapter) instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ModelAndView getMAV(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerAdapter handlerAdapter1 = handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow();
        return handlerAdapter1.handle(request, response, handler);
    }
}
