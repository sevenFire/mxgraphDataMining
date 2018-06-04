package com.lyh.util;

import com.alibaba.fastjson.JSONObject;
import com.lyh.constant.XMLConstants;
import com.lyh.model.BuildModel;
import com.lyh.model.GBMModel;
import com.lyh.model.ImportFile;
import com.lyh.model.Parse;
import com.lyh.model.ParseSetup;
import com.lyh.model.Predict;
import com.lyh.model.Split;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParseUtilTest {

    public static void main(String[] args) {
        String ajaxParam = "{\"executeObjectType\":\"transformation\",\"fileName\":null,\"graphXml\":\"<mxGraphModel grid=\\\"1\\\" gridSize=\\\"10\\\" guides=\\\"1\\\" tooltips=\\\"1\\\" connect=\\\"1\\\" arrows=\\\"1\\\" fold=\\\"1\\\" page=\\\"1\\\" pageScale=\\\"1\\\" pageWidth=\\\"1440\\\" pageHeight=\\\"612\\\" background=\\\"#ffffff\\\"><root><mxCell id=\\\"0\\\"/><mxCell id=\\\"1\\\" parent=\\\"0\\\"/><mxCell id=\\\"13\\\" value=\\\"\\\" style=\\\"rhombus;whiteSpace=wrap;html=1;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"170\\\" y=\\\"150\\\" width=\\\"80\\\" height=\\\"80\\\" as=\\\"geometry\\\"/></mxCell><mxCell id=\\\"14\\\" value=\\\"\\\" style=\\\"rounded=0;whiteSpace=wrap;html=1;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"320\\\" y=\\\"160\\\" width=\\\"120\\\" height=\\\"60\\\" as=\\\"geometry\\\"/></mxCell><mxCell id=\\\"15\\\" value=\\\"\\\" style=\\\"rounded=1;whiteSpace=wrap;html=1;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"510\\\" y=\\\"160\\\" width=\\\"120\\\" height=\\\"60\\\" as=\\\"geometry\\\"/></mxCell><mxCell id=\\\"16\\\" value=\\\"\\\" style=\\\"triangle;whiteSpace=wrap;html=1;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"700\\\" y=\\\"150\\\" width=\\\"60\\\" height=\\\"80\\\" as=\\\"geometry\\\"/></mxCell><mxCell id=\\\"17\\\" value=\\\"\\\" style=\\\"ellipse;whiteSpace=wrap;html=1;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"850\\\" y=\\\"150\\\" width=\\\"120\\\" height=\\\"80\\\" as=\\\"geometry\\\"/></mxCell><mxCell id=\\\"18\\\" value=\\\"\\\" style=\\\"ellipse;whiteSpace=wrap;html=1;aspect=fixed;\\\" vertex=\\\"1\\\" parent=\\\"1\\\"><mxGeometry x=\\\"1040\\\" y=\\\"160\\\" width=\\\"80\\\" height=\\\"80\\\" as=\\\"geometry\\\"/></mxCell></root></mxGraphModel>\"}";
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String graphXml = ajaxParamObj.getString("graphXml");
        System.out.println(graphXml);
//        parseXml(graphXml);
        parseXmlWithOrder(graphXml);
    }

    private static void parseXmlWithOrder(String xmldata) {
        Map<String, String> paraMap = new HashMap<String, String>();
        InputSource in = new InputSource(new StringReader(xmldata));
        in.setEncoding("UTF-8");

        SAXReader reader = new SAXReader();
        Document document;

        try {
            document = reader.read(in);
            Element graphModelElement = document.getRootElement(); // 获取根节点
            Element rootElement = graphModelElement.element("root");
            Iterator rootiter = rootElement.elementIterator("mxCell"); // 获取根节点下的子节点mxCell


            //构造DAGGraph
            while (rootiter.hasNext()) {
                Element mxCellElement = (Element) rootiter.next();
                String style = mxCellElement.attribute("style")==null ? "":mxCellElement.attribute("style").getText();

                if (StringUtils.equals(style, XMLConstants.ImportFileStyle)){
                    ImportFile importFile = new ImportFile();
                }else if (StringUtils.equals(style,XMLConstants.ParseSetupStyle)){
                    ParseSetup parseSetup = new ParseSetup();
                }else if(StringUtils.equals(style,XMLConstants.ParseStyle)){
                    Parse parse = new Parse();
                }else if(StringUtils.equals(style,XMLConstants.SplitStyle)){
                    Split split = new Split();
                }else if(StringUtils.equals(style,XMLConstants.BuildModelGBMStyle)){
                    GBMModel gbmModel = new GBMModel();
                }else if(StringUtils.equals(style,XMLConstants.PredictStyle)){
                    Predict predict = new Predict();
                }else if(StringUtils.equals(style,XMLConstants.ArrowStyle)){

                }


            }



        }catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    private static void parseXml(String xmldata) {
        Map<String, String> paraMap = new HashMap<String, String>();
        InputSource in = new InputSource(new StringReader(xmldata));
//        in.setEncoding("UTF-8");

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
                    System.out.println("ParseSetup");
                }else if(StringUtils.equals(style,"rounded=1;whiteSpace=wrap;html=1;")){
                    System.out.println("parse");
                }else if(StringUtils.equals(style,"triangle;whiteSpace=wrap;html=1;")){
                    System.out.println("split");
                }else if(StringUtils.equals(style,"ellipse;whiteSpace=wrap;html=1;")){
                    System.out.println("train");
                    paraMap.put("train","GBM");
                }else if(StringUtils.equals(style,"ellipse;whiteSpace=wrap;html=1;aspect=fixed;")){
                    System.out.println("predict");
                }


            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}
