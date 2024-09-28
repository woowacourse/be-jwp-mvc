package com.interface21.webmvc.servlet.mvc.handler.mapping;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMappingsTest {

    private AnnotationHandlerMapping handlerMapping;
    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.success");
        handlerMapping.init();
        handlerMappings = new HandlerMappings(List.of(handlerMapping));
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 핸들러가 존재하지 않는 경우 예외가 발생한다.")
    void getHandlerAbsenceExceptionTest() {
        final var request = new MockHttpServletRequest("GET", "/absence-test");
        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("GET /absence-test를 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }

}
