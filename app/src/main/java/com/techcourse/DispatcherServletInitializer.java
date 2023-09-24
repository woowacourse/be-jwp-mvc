package com.techcourse;

import jakarta.servlet.ServletContext;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.WebApplicationInitializer;
import webmvc.org.springframework.web.servlet.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    public static final String BASE_PACKAGE = "com.techcourse.controller";


    @Override
    public void onStartup(final ServletContext servletContext) {
        final HandlerAdapters handlerAdapters = new HandlerAdapters(
                Set.of(new AnnotationHandlerAdapter()));
        final HandlerMappings handlerMappings = new HandlerMappings(
                Set.of(new AnnotationHandlerMapping(BASE_PACKAGE)));

        final var dispatcherServlet = new DispatcherServlet(handlerAdapters, handlerMappings);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
