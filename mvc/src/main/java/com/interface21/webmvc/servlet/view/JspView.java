package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);
    private static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }
        setModelAttribute(model, request);
        request.getRequestDispatcher(viewName).forward(request, response);
    }

    private void setModelAttribute(final Map<String, ?> model, final HttpServletRequest request) {
        model.keySet()
                .forEach(key -> {
                    log.debug("attribute name : {}, value : {}", key, model.get(key));
                    request.setAttribute(key, model.get(key));
                });
    }
}
