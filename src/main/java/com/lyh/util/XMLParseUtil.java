package com.lyh.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLParseUtil {


    public static Map<String, String> parseXml(String xmldata) {
        Map<String, String> paramMap = new HashMap<>();
        InputSource in = new InputSource(new StringReader(xmldata));
        in.setEncoding("UTF-8");

        SAXReader reader = new SAXReader();
        Document document;

        try {
            document = reader.read(in);
            Element graphModelElement = document.getRootElement(); // 获取根节点
            Element rootElement = graphModelElement.element("root");
            Iterator rootiter = rootElement.elementIterator("mxCell"); // 获取根节点下的子节点mxCell

            while (rootiter.hasNext()) {
                Element mxCellElement = (Element) rootiter.next();
                String style = mxCellElement.attribute("style")==null ? "":mxCellElement.attribute("style").getText();

                if (StringUtils.equals(style,"rhombus;whiteSpace=wrap;html=1;")){
                    System.out.println("import");
                }else if (StringUtils.equals(style,"rounded=0;whiteSpace=wrap;html=1;")){
                    System.out.println("parseSetup");
                }else if(StringUtils.equals(style,"rounded=1;whiteSpace=wrap;html=1;")){
                    System.out.println("parse");
                }else if(StringUtils.equals(style,"triangle;whiteSpace=wrap;html=1;")){
                    System.out.println("split");
                }else if(StringUtils.equals(style,"ellipse;whiteSpace=wrap;html=1;")){
                    System.out.println("train");
                    paramMap.put("train","GBM");
                }else if(StringUtils.equals(style,"ellipse;whiteSpace=wrap;html=1;aspect=fixed;")){
                    System.out.println("predict");
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            return paramMap;
        }
    }

    /**
     * 将字符串写入xml文件中
     * @param xmldata
     * @return
     */
    public static Map<String, String> toXmlFile(String xmldata) {
        return null;
    }
}
