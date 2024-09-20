package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import samples.ExampleController;

public class AnnotatedControllerTest {

    @DisplayName("컨트롤러에 대한 모든 핸들러를 생성한다.")
    @Test
    public void getRequestMappingMethods() throws Exception {
        ExampleController instance = new ExampleController();
        AnnotatedController annotatedController = AnnotatedController.from(ExampleController.class);

        List<Handler> handlers = annotatedController.createHandlers();

        assertThat(handlers)
                .containsExactlyInAnyOrder(
                        createHandler("/get-test", RequestMethod.DELETE, instance, "method1"),
                        createHandler("/get-test", RequestMethod.GET, instance, "method1"),
                        createHandler("/test", RequestMethod.POST, instance, "method2"),
                        createHandler("/all", RequestMethod.GET, instance, "method4"),
                        createHandler("/all", RequestMethod.HEAD, instance, "method4"),
                        createHandler("/all", RequestMethod.POST, instance, "method4"),
                        createHandler("/all", RequestMethod.PUT, instance, "method4"),
                        createHandler("/all", RequestMethod.PATCH, instance, "method4"),
                        createHandler("/all", RequestMethod.DELETE, instance, "method4"),
                        createHandler("/all", RequestMethod.OPTIONS, instance, "method4"),
                        createHandler("/all", RequestMethod.TRACE, instance, "method4")
                );
    }

    private Handler createHandler(String url, RequestMethod requestMethod, Object controller, String methodName)
            throws Exception {
        Method method = controller.getClass()
                .getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        return new Handler(
                new HandlerKey(url, requestMethod),
                new HandlerExecution(controller, method)
        );
    }
}