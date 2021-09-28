package nextstep.mvc.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(View view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public void addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> model() {
        return Collections.unmodifiableMap(model);
    }

    public View view() {
        return view;
    }
}
