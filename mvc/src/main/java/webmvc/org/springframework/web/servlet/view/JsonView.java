package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsonValue;
        if (model.size() == 1) {
            jsonValue = makeResponseWhenModelHasOneData(model);
        } else {
            jsonValue = objectMapper.writeValueAsString(model);
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(jsonValue);
    }

    private String makeResponseWhenModelHasOneData(Map<String, ?> model) throws JsonProcessingException {
        String key = model.keySet().iterator().next();
        Object value = model.get(key);
        return objectMapper.writeValueAsString(value);
    }
}
