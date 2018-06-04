package com.lyh.util;

import com.lyh.constant.XMLConstants;
import com.lyh.model.DAGGraph;
import com.lyh.model.Edge;
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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLParseUtil {


    public static DAGGraph parseXmlToDAGGraph(String xmldata) {
        Map<String, String> paramMap = new HashMap<>();
        InputSource in = new InputSource(new StringReader(xmldata));
        in.setEncoding("UTF-8");

        SAXReader reader = new SAXReader();
        Document document;
        DAGGraph dagGraph = new DAGGraph();

        try {
            document = reader.read(in);
            Element graphModelElement = document.getRootElement(); // 获取根节点
            Element rootElement = graphModelElement.element("root");
            Iterator rootiter = rootElement.elementIterator("mxCell"); // 获取根节点下的子节点mxCell

            while (rootiter.hasNext()) {
                Element mxCellElement = (Element) rootiter.next();
                String style = mxCellElement.attribute("style")==null ?
                        "":mxCellElement.attribute("style").getText();

                if (StringUtils.isEmpty(style)){
                    continue;
                }else if(StringUtils.contains(style,XMLConstants.ArrowStyle)){//若是连线 //todo 这里用的contains
                    String source = mxCellElement.attribute(XMLConstants.ARROW_SOURCE)==null ?
                            "":mxCellElement.attribute(XMLConstants.ARROW_SOURCE).getText();
                    String target = mxCellElement.attribute(XMLConstants.ARROW_TARGET)==null ?
                            "":mxCellElement.attribute(XMLConstants.ARROW_TARGET).getText();

                    if (!StringUtils.isEmpty(source) && !StringUtils.isEmpty(target)){
                        dagGraph.addEdge(new Edge(source,target));
                    }
                }else {//若不是连线
                    String id = mxCellElement.attribute(XMLConstants.ID)==null ?
                            "":mxCellElement.attribute(XMLConstants.ID).getText();
                    if (StringUtils.isEmpty(id)){
                        throw new DocumentException("模型的id未提供");
                    }

                    if (StringUtils.equals(style, XMLConstants.ImportFileStyle)){
                        ImportFile importFile = new ImportFile();
                        //todo 填写属性
                        dagGraph.addVertex(id,importFile);
                    }else if (StringUtils.equals(style,XMLConstants.ParseSetupStyle)){
                        ParseSetup parseSetup = new ParseSetup();
                        dagGraph.addVertex(id,parseSetup);
                    }else if(StringUtils.equals(style,XMLConstants.ParseStyle)){
                        Parse parse = new Parse();
                        dagGraph.addVertex(id,parse);
                    }else if(StringUtils.equals(style,XMLConstants.SplitStyle)){
                        Split split = new Split();
                        dagGraph.addVertex(id,split);
                    }else if(StringUtils.equals(style,XMLConstants.BuildModelGBMStyle)){
                        GBMModel gbmModel = new GBMModel();
                        dagGraph.addVertex(id,gbmModel);
                    }else if(StringUtils.equals(style,XMLConstants.PredictStyle)){
                        Predict predict = new Predict();
                        dagGraph.addVertex(id,predict);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            return dagGraph;
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
