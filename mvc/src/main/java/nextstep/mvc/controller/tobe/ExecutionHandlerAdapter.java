package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class ExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerExecution handlerObject = (HandlerExecution) handler;
        return handlerObject.handle(request, response);
    }
}
