package com.interface21.webmvc.servlet.view.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        if (model.isEmpty()) {
            return;
        }
        
        try {
            PrintWriter writer = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            String responseJson = convertToJson(model);

            writer.write(responseJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertToJson(Map<String, ?> model) throws IOException {
        if (model.size() == 1) {
            Iterator<?> modelIterator = model.values().iterator();
            StringBuilder responseJson = new StringBuilder();
            while(modelIterator.hasNext()) {
                responseJson.append(modelIterator.next());
            }

            int jsonStartIndex = responseJson.indexOf("{");
            return objectMapper.writeValueAsString(responseJson.substring(jsonStartIndex));
        }
        return objectMapper.writeValueAsString(model);
    }
}
