package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            final String redirectViewName = viewName.substring(JspView.REDIRECT_PREFIX.length());
            return new ModelAndView(new JspView(redirectViewName));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
