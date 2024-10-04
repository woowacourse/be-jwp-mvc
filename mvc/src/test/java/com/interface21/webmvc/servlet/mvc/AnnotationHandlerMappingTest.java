package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private ConcurrentHashMap<HandlerKey, AnnotationHandler> handlers;
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlers = new ConcurrentHashMap<>();
        handlerMapping = new AnnotationHandlerMapping(handlers, "samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("RequestMapping 어노테이션에 http method가 없는 경우 모든 http method에 대해 핸들러를 등록한다.")
    void initializeWithNoHttpMethod() {
        List<HandlerKey> expected = Arrays.stream(RequestMethod.getRequestMethods())
                .map(requestMethod -> new HandlerKey("/no-method-test", requestMethod))
                .toList();

        assertThat(handlers.keySet()).containsAll(expected);
    }

    @Test
    @DisplayName("요청을 처리할 수 있는지 확인한다.")
    void canHandle() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        boolean actual = handlerMapping.canHandle(request);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("등록되지 않은 url에 대한 요청을 처리할 수 없다.")
    void canNotHandle() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-found");
        when(request.getMethod()).thenReturn("GET");

        boolean actual = handlerMapping.canHandle(request);

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("중복된 url과 http method를 등록할 수 없다.")
    void initializeWithDuplicateUrlAndMethod() {
        handlerMapping = new AnnotationHandlerMapping(handlers, "com.interface21.webmvc.servlet.mvc");
        assertThatThrownBy(() -> handlerMapping.initialize())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 url과 http method 입니다.");
    }

    @Controller
    public static class TestController {

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView duplicatedUrlAndHttpMethod(final HttpServletRequest request,
                                                       final HttpServletResponse response) {
            return null;
        }

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        public ModelAndView duplicatedUrlAndHttpMethod2(final HttpServletRequest request,
                                                        final HttpServletResponse response) {
            return null;
        }
    }
}
