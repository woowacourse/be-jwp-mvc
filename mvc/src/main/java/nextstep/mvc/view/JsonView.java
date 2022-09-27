package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            Object value = model.values().stream().findFirst().orElseThrow();
            writer.write(OBJECT_MAPPER.writeValueAsString(value));
            return;
        }
        writer.write(OBJECT_MAPPER.writeValueAsString(model));
    }
}
