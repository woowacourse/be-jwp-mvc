package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class ManualHandlerMappingAdapter implements HandlerMapping {

    private final ManualHandlerMapping manualHandlerMapping;

    public ManualHandlerMappingAdapter(ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void initialize() {
        manualHandlerMapping.initialize();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return manualHandlerMapping.getHandler(request.getRequestURI());
    }
}
