package com.interface21.webmvc.servlet.mvc.handlerMapping;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public HandlerMappingRegistryTest() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.annotationHandlerMapping = new AnnotationHandlerMapping("samples");
    }

    @BeforeEach
    void setUp() {
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @DisplayName("요청에 맞는 어노테이션 기반 핸들러를 반환한다")
    @Test
    void getAnnotationHandler() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object actual = handlerMappingRegistry.getHandler(request);
        Object expected = annotationHandlerMapping.getHandler(request);

        assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("요청에 맞는 핸들러를 찾을 수 없으면 예외로 처리한다.")
    @Test
    void findNothing() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/find-nothing");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청에 맞는 Handler를 찾을 수 없습니다.");
    }
}
