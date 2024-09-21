package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
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

        final var handlerExecution = handlerMapping.getHandler(request);
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

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("매핑 method가 없다면 모든 method에 대해 매핑한다.")
    @ValueSource(strings = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"})
    @ParameterizedTest
    void givenMethodAbsent_whenMapping_thenAllMethod(String requestMethod) throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/without-method");
        when(request.getMethod()).thenReturn(requestMethod);

        final HandlerExecution handlerExecution = handlerMapping.getHandler(request);

        assertThat(handlerExecution.handle(request, response)).isNotNull();
    }

    @Nested
    @Controller
    class DuplicateTest {

        @RequestMapping(value = "/duplicate-test", method = RequestMethod.GET)
        public ModelAndView helloNakNak(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        @RequestMapping(value = "/duplicate-test", method = RequestMethod.GET)
        public ModelAndView hiNakNak(final HttpServletRequest request, final HttpServletResponse response) {
            return new ModelAndView(new JspView(""));
        }

        @DisplayName("HandlerKey가 중복인 경우 예외가 발생한다.")
        @Test
        void givenDuplicatedHandlerKey_whenMappingHandler_thenThrowException() {
            handlerMapping = new AnnotationHandlerMapping("com.interface21.webmvc.servlet.mvc.tobe");

            assertThatThrownBy(() -> handlerMapping.initialize())
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
