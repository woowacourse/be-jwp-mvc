package com.techcourse.servlet.handler.mapper;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.manual.LoginController;
import com.techcourse.controller.manual.LoginViewController;
import com.techcourse.controller.manual.LogoutController;
import com.techcourse.controller.manual.RegisterController;
import com.techcourse.controller.manual.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request);
        if (!hasHandler(request)) {
            throw new IllegalArgumentException("잘못된 핸들러 요청입니다.");
        }
        return controllers.get(request.getRequestURI());
    }
}
