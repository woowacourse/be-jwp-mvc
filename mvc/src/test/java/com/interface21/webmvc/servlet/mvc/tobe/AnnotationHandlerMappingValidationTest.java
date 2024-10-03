package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingValidationTest {

    @DisplayName("Controller에 매개 변수가 있는 생성자가 정의 되었을 때 매개 변수가 없는 생성자가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void exceptionWithNoArgConstructor() {
        // given
        AnnotationHandlerMapping testHandlerMapping = new AnnotationHandlerMapping(getClass());
        // when
        assertThatThrownBy(() -> testHandlerMapping.initialize())
                // then
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("NoArgConstructorController");
    }

    @DisplayName("AnnotationMapping을 지원하지 않은 객체 타입이 들어올 경우 예외가 발생한다.")
    @Test
    void exceptionWithUnsupportedType() throws Exception {
        assertAll(() -> assertThatThrownBy(() -> new AnnotationHandlerMapping(null))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new AnnotationHandlerMapping(new URL("http://TACAN.zzang")))
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @Controller
    static public class NoArgConstructorController {

        private String name;
        private String value;

        public NoArgConstructorController(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @RequestMapping(value = "/multi-constructor-test", method = RequestMethod.GET)
        public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
            final var modelAndView = new ModelAndView(new JspView(""));
            modelAndView.addObject("name", null);
            return modelAndView;
        }
    }
}
