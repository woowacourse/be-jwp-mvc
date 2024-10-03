package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingsTest {

    @Test
    @DisplayName("적절한 핸들러를 조회할 수 있다.")
    void getHandler() {
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        Object handler = new Object();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI())
                .thenReturn("/samples");
        when(request.getMethod())
                .thenReturn("GET");
        when(handlerMapping.getHandler(request))
                .thenReturn(handler);

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.appendHandlerMapping(handlerMapping);

        Object actual = handlerMappings.getHandler(request);
        assertThat(actual).isEqualTo(handler);
    }

    @Test
    @DisplayName("핸들러 매핑을 추가할 수 있다.")
    void append() {
        HandlerMappings handlerMappings = new HandlerMappings();
        HandlerMapping handlerMapping = mock(HandlerMapping.class);
        Object handler = new Object();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/samples");
        when(request.getMethod())
                .thenReturn("GET");
        when(handlerMapping.getHandler(request))
                .thenReturn(handler);

        handlerMappings.appendHandlerMapping(handlerMapping);

        Object actual = handlerMappings.getHandler(request);
        assertThat(actual).isEqualTo(handler);
    }

    @Test
    @DisplayName("대응되는 핸들러가 존재하지 않으면 예외를 발생한다.")
    void notFound() {
        HandlerMappings handlerMappings = new HandlerMappings();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI())
                .thenReturn("/samples");
        when(request.getMethod())
                .thenReturn("GET");

        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("핸들러가 존재하지 않습니다. /samples");
    }
}
