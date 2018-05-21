package com.lyh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/home.do")
    public ModelAndView home(HttpServletRequest request,
                             HttpServletResponse response) {
        ModelAndView model = new ModelAndView("home");
        return model;
    }

    @RequestMapping(value = "/test.do")
    public ModelAndView test(HttpServletRequest request,
                             HttpServletResponse response) {
        ModelAndView model = new ModelAndView("/pages/test");
        return model;
    }

    @RequestMapping(value = "/index.do")
    public ModelAndView index(HttpServletRequest request,
                             HttpServletResponse response) {
        ModelAndView model = new ModelAndView("/index");
        return model;
    }
}
