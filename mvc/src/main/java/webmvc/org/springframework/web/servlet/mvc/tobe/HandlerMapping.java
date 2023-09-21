package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(final HttpServletRequest request);

    void initialize();
}
