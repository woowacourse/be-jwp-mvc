package com.techcourse.controller;

import com.interface21.web.bind.annotation.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginPage(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return redirectToIndex();
                })
                .orElse(getLoginPage());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return redirectToIndex();
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(redirectTo401());
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return redirectToIndex();
        }
        return redirectTo401();
    }

    private ModelAndView redirectToIndex() {
        return getModelAndViewByViewName("redirect:/index.jsp");
    }

    private ModelAndView redirectTo401() {
        return getModelAndViewByViewName("redirect:/401.jsp");
    }

    private ModelAndView getLoginPage() {
        return getModelAndViewByViewName("/login.jsp");
    }

    private ModelAndView getModelAndViewByViewName(String viewName) {
        View view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
