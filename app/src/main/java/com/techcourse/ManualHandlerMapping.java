package com.techcourse;

import com.techcourse.controllerv1.LoginControllerV1;
import com.techcourse.controllerv1.LoginViewControllerV1;
import com.techcourse.controllerv1.LogoutControllerV1;
import com.techcourse.controllerv1.RegisterControllerV1;
import com.techcourse.controllerv1.RegisterViewControllerV1;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<HandlerKey, Controller> CONTROLLERS = new HashMap<>();

    public ManualHandlerMapping() {
        initialize();
    }

    @Override
    public void initialize() {
        CONTROLLERS.put(new HandlerKey("/", GET), new ForwardController("/index.jsp"));
        CONTROLLERS.put(new HandlerKey("/login", POST), new LoginControllerV1());
        CONTROLLERS.put(new HandlerKey("/login/view", GET), new LoginViewControllerV1());
        CONTROLLERS.put(new HandlerKey("/logout", GET), new LogoutControllerV1());
        CONTROLLERS.put(new HandlerKey("/register/view", GET), new RegisterViewControllerV1());
        CONTROLLERS.put(new HandlerKey("/register", POST), new RegisterControllerV1());

        log.info("Initialized Handler Mapping!");
        CONTROLLERS.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, CONTROLLERS.get(path).getClass()));
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        return CONTROLLERS.containsKey(getHandlerKey(request));
    }

    @Override
    public Object getHandlerExecution(final HttpServletRequest request) {
        final HandlerKey handlerKey = getHandlerKey(request);

        return CONTROLLERS.get(handlerKey);
    }
}
