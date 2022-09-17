package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            extractHandlerAdapter(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void extractHandlerAdapter(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            Object handler = getHandler(request);
            execute(request, response, handlerAdapter, handler);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }

    private void execute(final HttpServletRequest request, final HttpServletResponse response,
                           final HandlerAdapter handlerAdapter, final Object handler) throws Exception {
        if (handlerAdapter.supports(handler)) {
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(request, response, modelAndView);
        }
    }

    private void render(final HttpServletRequest request, final HttpServletResponse response,
                        final ModelAndView modelAndView) {
        try {
            modelAndView.render(request, response);
        } catch (Exception e) {
            log.info("fail render");
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
