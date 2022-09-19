package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class TestRedirectManualController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "redirect:/index.jsp";
    }
}
