package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView showIndexView(final HttpServletRequest req, final HttpServletResponse res) {
        return ModelAndView.fromJsp("/index.jsp");
    }
}