package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = convertToJson(model);
        response.getWriter().write(json);
    }

    private String convertToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model == null) {
            throw new IllegalArgumentException("model은 널일 수 없습니다.");
        }
        if (model.isEmpty()) {
            return "{}";
        }
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
