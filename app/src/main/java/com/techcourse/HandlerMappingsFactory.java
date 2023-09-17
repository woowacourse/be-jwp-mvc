package com.techcourse;

import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class HandlerMappingsFactory {
    private HandlerMappingsFactory() {
    }

    public static List<HandlerMapping> createHandlerMappings(final Object... basePackagePath) {
        return List.of(new AnnotationHandlerMapping(basePackagePath), new ManualHandlerMapping());
    }
}
