package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(redirectToIndex())
                .orElse(new ModelAndView("/login.jsp"));
    }

    public Function<User, ModelAndView> redirectToIndex() {
        return user -> {
            log.info("logged in {}", user.getAccount());
            return new ModelAndView("redirect:/index.jsp");
        };
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView("redirect:index.jsp");
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return checkPassword(req, user);
                })
                .orElse(new ModelAndView("redirect:401.jsp"));
    }

    private ModelAndView checkPassword(HttpServletRequest request, User user) {
        if (!user.checkPassword(request.getParameter("password"))) {
            return new ModelAndView("redirect:401.jsp");
        }
        final HttpSession session = request.getSession();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return new ModelAndView("redirect:index.jsp");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView("redirect:/");
    }
}
