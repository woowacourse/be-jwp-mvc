package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof nextstep.mvc.controller.asis.Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final String viewName = ((nextstep.mvc.controller.asis.Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    @Override
    public void render(final ModelAndView modelAndView,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        // TODO refactor this
        final String viewName = ((JspView) modelAndView.getView()).getViewName();
        render(viewName, request, response);
    }

    private void render(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
