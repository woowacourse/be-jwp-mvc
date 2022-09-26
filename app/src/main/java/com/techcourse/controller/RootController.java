package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RootController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
