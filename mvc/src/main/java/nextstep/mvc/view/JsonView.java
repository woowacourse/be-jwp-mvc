package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int SINGLE_DATE = 1;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        if (model.size() == SINGLE_DATE) {
            writer.write(OBJECT_MAPPER.writeValueAsString(getSingleModel(model)));
            return;
        }
        writer.write(OBJECT_MAPPER.writeValueAsString(model));
    }

    private Object getSingleModel(final Map<String, ?> model) {
        return model.values().stream()
                .findFirst()
                .orElseThrow();
    }
}
