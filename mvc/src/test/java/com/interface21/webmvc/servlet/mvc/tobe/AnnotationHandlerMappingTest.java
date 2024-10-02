package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("GET 요청을 처리하는 핸들러 매핑 성공")
    @Test
    void get() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("POST 요청을 처리하는 핸들러 매핑 성공")
    @Test
    void post() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        //when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("method 설정이 없는 경우 모든 Http method를 지원")
    @ParameterizedTest
    @EnumSource(value = RequestMethod.class)
    void noMethod(RequestMethod requestMethod) throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/no-method-test");
        when(request.getMethod()).thenReturn(requestMethod.name());

        //when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.findHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("요청에 해당하는 HandlerExecution이 없을 때 null 반환")
    @Test
    void getHandle_notExist() {
        //given & when
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get");
        when(request.getMethod()).thenReturn("GET");

        //then
        assertThat(handlerMapping.findHandler(request)).isNull();
    }
}
