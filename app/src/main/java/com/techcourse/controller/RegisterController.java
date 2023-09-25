package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class RegisterController {

    private static final String INDEX_JSP = "/index.jsp";
    private static final String REGISTER_JSP = "/register.jsp";

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView display(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(JspView.of(REGISTER_JSP));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        final var user = new User(
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(JspView.redirect(INDEX_JSP));
    }
}
