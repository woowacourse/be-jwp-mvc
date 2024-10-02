package com.interface21.web;

import com.interface21.core.util.ReflectionUtils;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

    private final List<WebApplicationInitializer> initializers;

    public SpringServletContainerInitializer() {
        initializers = new ArrayList<>();
    }

    SpringServletContainerInitializer(List<WebApplicationInitializer> initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
            throws ServletException {
        if (webAppInitializerClasses == null || webAppInitializerClasses.isEmpty()) {
            String packageName = getClass().getPackageName();
            String rootPackageName = packageName.split("\\.")[0];
            Reflections reflections = new Reflections(rootPackageName);
            var foundClasses = reflections.getSubTypesOf(WebApplicationInitializer.class);
            addClassToInitializers(new HashSet<>(foundClasses));
            startUp(servletContext);
            return;
        }

        addClassToInitializers(webAppInitializerClasses);

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        startUp(servletContext);
    }

    private void addClassToInitializers(Set<Class<?>> webAppInitializerClasses)
            throws ServletException {
        for (Class<?> waiClass : webAppInitializerClasses) {
            try {
                initializers.add((WebApplicationInitializer)
                        ReflectionUtils.accessibleConstructor(waiClass).newInstance());
            } catch (Throwable ex) {
                throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
            }
        }
    }

    private void startUp(ServletContext servletContext)
            throws ServletException {
        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
