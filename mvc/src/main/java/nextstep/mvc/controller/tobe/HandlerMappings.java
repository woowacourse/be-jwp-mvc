package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.CannotFindHandlerException;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(CannotFindHandlerException::new);
    }
}
