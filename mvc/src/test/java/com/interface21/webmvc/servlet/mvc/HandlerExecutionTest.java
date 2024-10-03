package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestAnnotationController;

class HandlerExecutionTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Class<?> testController;

    @BeforeEach
    void setUp() throws ClassNotFoundException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        testController = Class.forName("samples.TestAnnotationController");
    }

    @Test
    @DisplayName("핸들러 요청 처리 성공")
    void handle() throws Exception {
        // given
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = new HandlerExecution(
                new TestAnnotationController(),
                testController.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class)
        );

        // when
        final var modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
