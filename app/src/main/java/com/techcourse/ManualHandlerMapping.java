package com.techcourse;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class ManualHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<String, Object> handlers;

    public ManualHandlerMapping() {
        this.handlers = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized ManualHandlerMapping!");

//        handlers.put("/", new ForwardController("/index.jsp"));
//        handlers.put("/login", new LegacyLoginController());
//        handlers.put("/login/view", new LegacyLoginViewController());
//        handlers.put("/logout", new LegacyLogoutController());
//        handlers.put("/register/view", new LegacyRegisterViewController());
//        handlers.put("/register", new LegacyRegisterController());

        handlers.forEach((key, value) -> log.info("HandlerKey : {}, handler : {}", key, value));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);

        return handlers.get(requestURI);
    }
}
