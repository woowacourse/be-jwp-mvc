package com.techcourse;

import com.interface21.context.stereotype.Controller;
import com.interface21.util.FileUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ControllerKey;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.LegacyRequestHandlerImpl;
import com.interface21.webmvc.servlet.RequestHandler;
import com.interface21.webmvc.servlet.RequestHandlerImpl;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private final Map<ControllerKey, RequestHandler> controllers = new HashMap<>();

    @Override
    public void initialize(String basePackage) throws ReflectiveOperationException, FileNotFoundException {
        log.info("Initializing Handler Mapping!");
        registerControllers(basePackage);
        registerLegacyControllers();
    }

    private void registerControllers(String basePackage) throws ReflectiveOperationException, FileNotFoundException {
        List<Class<?>> controllerClasses = getControllerClasses(basePackage);
        for (Class<?> controllerClass : controllerClasses) {
            initializeController(controllerClass);
        }
    }

    private List<Class<?>> getControllerClasses(String basePackage)
            throws ReflectiveOperationException, FileNotFoundException {
        return getClassesInPackage(basePackage).stream()
                .filter(clazz -> classHasAnnotation(clazz, Controller.class) && !clazz.isAnnotation())
                .toList();
    }

    private void initializeController(Class<?> controllerClass) {
        try {
            Controller classMapping = controllerClass.getAnnotation(Controller.class);
            String baseUri = classMapping.value();
            List<Method> methods = getMethodsHaveAnnotation(controllerClass, RequestMapping.class);

            for (Method method : methods) {
                registerControllerMethod(baseUri, controllerClass, method);
            }
        } catch (Exception e) {
            log.error("Failed to initialize controller: {}", controllerClass.getName(), e);
        }
    }

    private void registerControllerMethod(String baseUri, Class<?> controllerClass, Method method)
            throws ReflectiveOperationException {
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        String fullUri = baseUri + methodMapping.value();
        for (RequestMethod requestMethod : methodMapping.method()) {
            controllers.put(new ControllerKey(fullUri, requestMethod), getRequestHandler(controllerClass, method));
        }
    }

    private void registerLegacyControllers() {
        controllers.put(new ControllerKey("/", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new ForwardController("/index.jsp")));
        controllers.put(new ControllerKey("/login", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LoginController()));
        controllers.put(new ControllerKey("/login/view", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LoginViewController()));
        controllers.put(new ControllerKey("/logout", RequestMethod.GET),
                new LegacyRequestHandlerImpl(new LogoutController()));
    }

    private RequestHandler getRequestHandler(Class<?> beanClass, Method method) throws ReflectiveOperationException {
        Object bean = beanClass.getDeclaredConstructor().newInstance();
        return new RequestHandlerImpl(bean, method);
    }

    private List<Method> getMethodsHaveAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    private List<Class<?>> getClassesInPackage(String packageName)
            throws ReflectiveOperationException, FileNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        URL packageURL = FileUtils.getPackageURL(packageName);
        FileUtils.processDirectory(FileUtils.getPackageDirectory(packageURL), packageName, classes);
        return classes;
    }

    private boolean classHasAnnotation(Class<?> clazz, Class<? extends Annotation> targetAnnotation) {
        List<? extends Class<? extends Annotation>> annotations = getClassesHasCustomAnnotation(clazz);
        if (annotations.contains(targetAnnotation)) {
            return true;
        }

        return annotations.stream()
                .anyMatch(annotation -> classHasAnnotation(annotation, targetAnnotation));
    }

    private List<? extends Class<? extends Annotation>> getClassesHasCustomAnnotation(Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .filter(this::isCustomAnnotation)
                .collect(Collectors.toList());
    }

    private boolean isCustomAnnotation(Class<? extends Annotation> annotationType) {
        return !annotationType.getPackage().getName().equals("java.lang.annotation");
    }

    @Override
    public RequestHandler getHandler(final String requestMethod, final String requestURI) {
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(new ControllerKey(requestURI, RequestMethod.of(requestMethod)));
    }
}
