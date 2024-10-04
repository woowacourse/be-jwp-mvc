package com.techcourse.controller;

import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(3,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        final String viewName = "redirect:/index.jsp";
        final View view = new JspView(viewName);
        return new ModelAndView(view)
                .addObject("user", user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        final User user = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .orElseThrow(() -> new NoSuchElementException("account가 존재하지 않습니다."));

        final String viewName = "/register.jsp";
        final View view = new JspView(viewName);
        return new ModelAndView(view)
                .addObject("user", user);
    }
}
