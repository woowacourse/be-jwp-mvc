package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotFoundException;

public class HandlerMappingRegistry {

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest httpServletRequest) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(httpServletRequest))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findAny()
            .orElseThrow(HandlerNotFoundException::new);
    }
}
