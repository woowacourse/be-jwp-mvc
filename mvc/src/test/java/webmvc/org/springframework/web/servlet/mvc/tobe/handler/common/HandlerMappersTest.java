package webmvc.org.springframework.web.servlet.mvc.tobe.handler.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMapper;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerMappers;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerMappersTest {

    private HandlerMappers handlerMappers;

    @BeforeEach
    void setup() {
        handlerMappers = new HandlerMappers();
    }

    @DisplayName("현재 추가된 Mapper들을 초기화 시킨다.")
    @Test
    void init_added_mappers() {
        // given
        HandlerMapper handlerMapper = mock(AnnotationHandlerMapping.class);
        handlerMappers.addHandlerMapping(handlerMapper);

        // when
        handlerMappers.init();

        // then
        verify(handlerMapper, times(1)).initialize();
    }

    @DisplayName("Handler를 찾아온다")
    @Test
    void find_handler() throws NoSuchMethodException {
        // given
        HttpServletRequest req = mock(HttpServletRequest.class);
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappers.addHandlerMapping(handlerMapping);
        handlerMappers.init();

        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn("/post-test");

        Method expected = TestController.class.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);

        // when
        HandlerExecution result = (HandlerExecution) handlerMappers.getHandler(req);

        // then
        assertThat(result).extracting("method").isEqualTo(expected);
    }
}
