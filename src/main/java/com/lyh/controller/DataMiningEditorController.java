package com.lyh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/dataMiningEditor.do")
public class DataMiningEditorController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        return new ModelAndView("/dataMiningEditor");
    }
}
