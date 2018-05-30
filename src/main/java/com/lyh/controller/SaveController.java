package com.lyh.controller;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/element.do")
public class SaveController {

    @RequestMapping(value = "/")
    public ModelAndView save(HttpServletRequest request,
                             HttpServletResponse response) {
        ModelAndView model = new ModelAndView("/page/save");
        return model;
    }

    @RequestMapping(params="method=save", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveFile(String ajaxParam) throws IOException {
        JSONObject ajaxParamObj = null;
        System.out.println("save file done");

        JSONObject returnInfo = new JSONObject();
        returnInfo.put("ok","done");
        return returnInfo;
    }

}
