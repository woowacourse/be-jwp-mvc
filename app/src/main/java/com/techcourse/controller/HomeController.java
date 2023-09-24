package com.techcourse.controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class HomeController {

    public static final String REDIRECT_HOME_URL = "redirect:/";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView homeRedirect(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView(REDIRECT_HOME_URL));
    }

}
