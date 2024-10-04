package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return ModelAndView.createJspView("redirect:/index.jsp");
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return redirectOnPasswordMatch(req, user);
                })
                .orElse(ModelAndView.createJspView("redirect:/401.jsp"));
    }

    private ModelAndView redirectOnPasswordMatch(final HttpServletRequest req, final User user) {
        if (user.checkPassword(req.getParameter("password"))) {
            final var session = req.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return ModelAndView.createJspView("redirect:/index.jsp");
        }
        return ModelAndView.createJspView("redirect:/401.jsp");
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return ModelAndView.createJspView("redirect:/index.jsp");
                })
                .orElse(ModelAndView.createJspView("/login.jsp"));
    }
}
