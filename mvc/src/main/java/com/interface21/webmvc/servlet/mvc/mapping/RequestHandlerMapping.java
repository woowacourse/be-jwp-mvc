package com.interface21.webmvc.servlet.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestHandlerMapping {

    void initialize();

    Object getHandler(final HttpServletRequest request);
}
