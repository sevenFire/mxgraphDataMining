package com.lyh.controller;

import com.alibaba.fastjson.JSONObject;

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
//        JSONObject mapIn = parseXml(graphXml);
//        JSONObject returnInfo = executeAlogs(mapIn);
//        return returnInfo;
        return null;
    }

    /**
     * 执行对应的算法
     * @param mapIn
     * @return
     */
    private JSONObject executeAlogs(JSONObject mapIn) {
        return null;
    }

    /**
     * 将前台传来的graphXml解析
     * @return
     */
    private JSONObject parseXml(String xmldata){
        int updateResult = 1;
        Map<String, String> paraMap = new HashMap<String, String>();
        InputSource in = new InputSource(new StringReader(xmldata));
        in.setEncoding("UTF-8");

        SAXReader reader = new SAXReader();
        Document document;

        try {
            document = reader.read(in);
            Element rootElement = document.getRootElement(); // 获取根节点
            Element rootjd = rootElement.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // 获取根节点下的子节点mxCell

            while (rootiter.hasNext()) {
                Element recordEle = (Element) rootiter.next();
            }


            } catch (Exception e) {
            e.printStackTrace();

        } finally {


        }
        return null;
    }

}
