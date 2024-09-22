package samples;

import com.interface21.context.stereotype.Component;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TestFailHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return null;
    }
}
