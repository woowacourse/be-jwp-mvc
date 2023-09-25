package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String viewLogin(final HttpServletRequest request,
                                  final HttpServletResponse response) throws Exception {
        final Optional<User> maybeUser = UserSession.getUserFrom(request.getSession());
        if (maybeUser.isPresent()) {
            log.info("user account={}", maybeUser.get().getAccount());
            return "redirect:/";
        }
        return "/login.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(final HttpServletRequest request,
                                    final HttpServletResponse response) throws Exception {
        if (UserSession.isLoggedIn(request.getSession())) {
            return  "redirect:/";
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String login(final HttpServletRequest request,
                         final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/";
        }
        return "redirect:/401.jsp";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}
