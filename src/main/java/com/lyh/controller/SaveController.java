package com.lyh.controller;

import com.alibaba.fastjson.JSONObject;
import com.lyh.util.XMLParseUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        System.out.println(ajaxParam);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String graphXml = ajaxParamObj.getString("graphXml");
        System.out.println(graphXml);
//        Map<String, String> mapIn = XMLParseUtil.parseXml(graphXml);
//        JSONObject returnInfo = executeAlogs(mapIn);
//        return returnInfo;
        return null;
    }


}
