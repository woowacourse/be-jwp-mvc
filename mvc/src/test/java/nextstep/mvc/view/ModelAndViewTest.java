package nextstep.mvc.view;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ModelAndViewTest {

    @Test
    void attributes_를_추가할_수_있다() {
        ModelAndView modelAndView = new ModelAndView(new JspView("/"));
        modelAndView.addObject("id", "1")
                .addObject("name", "hoho")
                .addObject("age", "22");

        Map<String, Object> model = modelAndView.getModel();

        assertThat(model).contains(
                entry("id", "1"),
                entry("name", "hoho"),
                entry("age", "22"));
    }

    @Test
    void 생성한_view_의_render_메서드가_호출된다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JspView view = mock(JspView.class);

        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.render(request, response);

        verify(view).render(Collections.emptyMap(), request, response);
    }
}
