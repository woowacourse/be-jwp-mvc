package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.techcourse.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public ManualHandlerMapping() {
        initialize();
    }

    private void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }

    @Override
    public Controller getHandler(final HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        log.debug("Request URI : {}", requestURI);
        return controllers.computeIfAbsent(requestURI, key -> {
            throw new IllegalStateException("ManualHandlerMapping 에 요청을 처리할 수 있는 핸들러가 없습니다. 핸들러를 등록해주세요.");
        });
    }
}
