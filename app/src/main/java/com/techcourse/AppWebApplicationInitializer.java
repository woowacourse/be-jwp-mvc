package com.techcourse;

import jakarta.servlet.ServletContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppWebApplicationInitializer implements WebApplicationInitializer {

    private static final String BASE_PACKAGE_PATH = "com.techcourse";
    private static final Logger log = LoggerFactory.getLogger(AppWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE_PATH));
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
