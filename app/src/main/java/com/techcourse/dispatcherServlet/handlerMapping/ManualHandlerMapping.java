package com.techcourse.dispatcherServlet.handlerMapping;

import com.interface21.webmvc.servlet.mvc.tobe.handlerMapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    public void initialize() {
//        controllers.put("/", new ForwardController("/index.jsp"));
//        controllers.put("/login", new LoginRelacyController());
//        controllers.put("/login/view", new LoginViewController());
//        controllers.put("/logout", new LogoutRegacyController());
//        controllers.put("/register/view", new RegisterViewController());
//        controllers.put("/register", new RegisterController());

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public boolean isSupported(HttpServletRequest request) {
        return controllers.containsKey(request.getRequestURI());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        return controllers.get(request.getRequestURI());
    }
}
