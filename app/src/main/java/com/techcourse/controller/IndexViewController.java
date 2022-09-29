package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class IndexViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexView(final HttpServletRequest request, final HttpServletResponse response) {
        return "index.jsp";
    }
}
