package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() throws Exception {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("RequestMapping에 아무런 메서드가 지정되지 않으면 모든 메서드가 맵핑된다.")
    @Test
    void testNoMethod() throws NoSuchFieldException, IllegalAccessException {
        // given
        Set<HandlerKey> handlerKeys = new HashSet<>();
        for (RequestMethod method : RequestMethod.values()) {
            handlerKeys.add(new HandlerKey("/no-method-test", method));
        }

        // when
        Field field = handlerMapping.getClass().getDeclaredField("handlerExecutions");
        field.setAccessible(true);

        // then
        Map<HandlerKey, HandlerExecution> handlerExecutions = (Map<HandlerKey, HandlerExecution>) field.get(handlerMapping);
       Assertions.assertThat(handlerExecutions.keySet()).containsAnyElementsOf(handlerKeys);
    }
}
