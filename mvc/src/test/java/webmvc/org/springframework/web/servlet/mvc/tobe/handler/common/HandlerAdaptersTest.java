package webmvc.org.springframework.web.servlet.mvc.tobe.handler.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;

    @BeforeEach
    void setup() {
        handlerAdapters = new HandlerAdapters();
    }

    @Test
    @DisplayName("핸들러를 찾는다")
    void find_handler() throws Exception {
        // given
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapters.addHandlerAdapter(handlerAdapter);

        Method executionMethod = TestController.class
                .getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);

        HandlerExecution handlerExecution = new HandlerExecution(executionMethod);

        // when
        HandlerAdapter result = handlerAdapters.getHandlerAdapter(handlerExecution);

        // then
        assertThat(result).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
