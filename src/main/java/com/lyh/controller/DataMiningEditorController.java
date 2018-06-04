package com.lyh.controller;


import com.alibaba.fastjson.JSONObject;
import com.lyh.model.DAGGraph;
import com.lyh.model.KahnTopo;
import com.lyh.model.Vertex;
import com.lyh.service.GBM;
import com.lyh.util.XMLParseUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hex.deeplearning.DeepLearning;
import water.bindings.H2oApi;
import water.bindings.pojos.ColSpecifierV3;
import water.bindings.pojos.DeepLearningModelV3;
import water.bindings.pojos.DeepLearningParametersV3;
import water.bindings.pojos.DeepLearningV3;
import water.bindings.pojos.FrameKeyV3;
import water.bindings.pojos.GBMModelV3;
import water.bindings.pojos.GBMParametersV3;
import water.bindings.pojos.GBMV3;
import water.bindings.pojos.ImportFilesV3;
import water.bindings.pojos.JobV3;
import water.bindings.pojos.ModelKeyV3;
import water.bindings.pojos.ModelMetricsListSchemaV3;
import water.bindings.pojos.ModelsV3;
import water.bindings.pojos.ParseSetupV3;
import water.bindings.pojos.ParseV3;
import water.bindings.pojos.RapidsSchemaV3;

@Controller
@RequestMapping("/dataMiningEditor.do")
public class DataMiningEditorController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        return new ModelAndView("/dataMiningEditor");
    }

    @RequestMapping(params="method=saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveFile(String ajaxParam) throws IOException {
        JSONObject ajaxParamObj = null;
        System.out.println("save file done");

        JSONObject returnInfo = new JSONObject();
        returnInfo.put("ok","done");
        return returnInfo;
    }

    @RequestMapping(params="method=executeByEditor", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject executeByEditor(String ajaxParam) throws IOException {

        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String graphXml = ajaxParamObj.getString("graphXml");
        System.out.println(graphXml);

        DAGGraph dagGraph = XMLParseUtil.parseXmlToDAGGraph(graphXml);
        dagGraph.buildGraph();
        KahnTopo topo = new KahnTopo(dagGraph);
        topo.process();
        for(Vertex vertex : topo.getResult()){
            System.out.print(vertex.getVertexLabel()+ "-->");
        }

//        executeAlogs(mapIn);

        return null;
    }

    private void executeAlogs(Map<String, String> mapIn) {
        String trainAlg = mapIn.get("train")==null? "":mapIn.get("train");
        if (!StringUtils.equals("",trainAlg)){
            if (StringUtils.equals("GBM",trainAlg)){
                GBM gbm = new GBM();
                try {
                    gbm.computeRemote();
                }catch (IOException ex){
                    System.out.println("执行算法失败，原因是："+ ex.toString());
                }
            }else{
                System.out.println("暂不支持此算法");
            }
        }
    }

    private void DL() throws IOException {
        //H2O start
//        String url = "http://localhost:54321/";
        String url = "http://168.2.8.130:54321/";
        H2oApi h2o = new H2oApi(url);

        //STEP 0: init a session
        String sessionId = h2o.newSession().sessionKey;

        //STEP 1: import raw file
        String path = "hdfs://kbmst:9000/user/spark/datasets/iris.csv";
        ImportFilesV3 importBody = h2o.importFiles(path, null);
        System.out.println("import: " + importBody);

        //STEP 2: parse setup
        ParseSetupV3 parseSetupParams = new ParseSetupV3();
        parseSetupParams.sourceFrames = H2oApi.stringArrayToKeyArray(importBody.destinationFrames, FrameKeyV3.class);
        ParseSetupV3 parseSetupBody = h2o.guessParseSetup(parseSetupParams);
        System.out.println("parseSetupBody: " + parseSetupBody);

        //STEP 3: parse into columnar Frame
        ParseV3 parseParams = new ParseV3();
        H2oApi.copyFields(parseParams, parseSetupBody);
        parseParams.destinationFrame = H2oApi.stringToFrameKey("iris.hex");
        parseParams.blocking = true;
        ParseV3 parseBody = h2o.parse(parseParams);
        System.out.println("parseBody: " + parseBody);

        //STEP 4: Split into test and train datasets
        String tmpVec = "tmp_" + UUID.randomUUID().toString();
        String splitExpr =
                "(, " +
                        "  (tmp= " + tmpVec + " (h2o.runif iris.hex 906317))" +
                        "  (assign train " +
                        "    (rows iris.hex (<= " + tmpVec + " 0.75)))" +
                        "  (assign test " +
                        "    (rows iris.hex (> " + tmpVec + " 0.75)))" +
                        "  (rm " + tmpVec + "))";
        RapidsSchemaV3 rapidsParams = new RapidsSchemaV3();
        rapidsParams.sessionId = sessionId;
        rapidsParams.ast = splitExpr;
        h2o.rapidsExec(rapidsParams);

        // STEP 5: Train the model
        // (NOTE: step 4 is polling, which we don't require because we specified blocking for the parse above)
        DeepLearningParametersV3 dlParams = new DeepLearningParametersV3();
        dlParams.trainingFrame = H2oApi.stringToFrameKey("train");
        dlParams.validationFrame = H2oApi.stringToFrameKey("test");

        dlParams.hidden=new int[]{200,200};

        ColSpecifierV3 responseColumn = new ColSpecifierV3();
        responseColumn.columnName = "class";
        dlParams.responseColumn = responseColumn;

        System.out.println("About to train DL. . .");
        DeepLearningV3 dlBody = h2o.train_deeplearning(dlParams);
        System.out.println("dlBody: " + dlBody);

        // STEP 6: poll for completion
        JobV3 job = h2o.waitForJobCompletion(dlBody.job.key);
        System.out.println("DL build done.");

        // STEP 7: fetch the model
        ModelKeyV3 model_key = (ModelKeyV3)job.dest;
        ModelsV3 models = h2o.model(model_key);
        System.out.println("models: " + models);
        DeepLearningModelV3 model = (DeepLearningModelV3)models.models[0];
        System.out.println("new DL model: " + model);


        // STEP 8: predict!
        ModelMetricsListSchemaV3 predict_params = new ModelMetricsListSchemaV3();
        predict_params.model = model_key;
        predict_params.frame = dlParams.trainingFrame;
        predict_params.predictionsFrame = H2oApi.stringToFrameKey("predictions");

        ModelMetricsListSchemaV3 predictions = h2o.predict(predict_params);
        System.out.println("predictions: " + predictions);

        // STEP 9: end the session
        h2o.endSession();

    }

    private void GBM() throws IOException {
        //H2O start
        String url = "http://localhost:54321/";
        H2oApi h2o = new H2oApi(url);

        //STEP 0: init a session
        String sessionId = h2o.newSession().sessionKey;

        //STEP 1: import raw file
        String path = "hdfs://kbmst:9000/user/spark/datasets/arrhythmia.csv";
//        String path = "http://s3.amazonaws.com/h2o-public-test-data/smalldata/flow_examples/arrhythmia.csv.gz";
        ImportFilesV3 importBody = h2o.importFiles(path, null);

        System.out.println("import: " + importBody);

        //STEP 2: parse setup
        ParseSetupV3 parseSetupParams = new ParseSetupV3();
        parseSetupParams.sourceFrames = H2oApi.stringArrayToKeyArray(importBody.destinationFrames, FrameKeyV3.class);
        ParseSetupV3 parseSetupBody = h2o.guessParseSetup(parseSetupParams);
        System.out.println("parseSetupBody: " + parseSetupBody);

        //STEP 3: parse into columnar Frame
        ParseV3 parseParams = new ParseV3();
        H2oApi.copyFields(parseParams, parseSetupBody);
        parseParams.destinationFrame = H2oApi.stringToFrameKey("arrhythmia.hex");
        parseParams.blocking = true;
        ParseV3 parseBody = h2o.parse(parseParams);
        System.out.println("parseBody: " + parseBody);

        //STEP 4: Split into test and train datasets
        String tmpVec = "tmp_" + UUID.randomUUID().toString();
        String splitExpr =
                "(, " +
                        "  (tmp= " + tmpVec + " (h2o.runif arrhythmia.hex 906317))" +
                        "  (assign train " +
                        "    (rows arrhythmia.hex (<= " + tmpVec + " 0.75)))" +
                        "  (assign test " +
                        "    (rows arrhythmia.hex (> " + tmpVec + " 0.75)))" +
                        "  (rm " + tmpVec + "))";
        RapidsSchemaV3 rapidsParms = new RapidsSchemaV3();
        rapidsParms.sessionId = sessionId;
        rapidsParms.ast = splitExpr;
        h2o.rapidsExec(rapidsParms);

        // STEP 5: Train the model
        // (NOTE: step 4 is polling, which we don't require because we specified blocking for the parse above)
        GBMParametersV3 gbmParms = new GBMParametersV3();
        gbmParms.trainingFrame = H2oApi.stringToFrameKey("train");
        gbmParms.validationFrame = H2oApi.stringToFrameKey("test");

        ColSpecifierV3 responseColumn = new ColSpecifierV3();
        responseColumn.columnName = "C1";
        gbmParms.responseColumn = responseColumn;

        System.out.println("About to train GBM. . .");
        GBMV3 gbmBody = h2o.train_gbm(gbmParms);
        System.out.println("gbmBody: " + gbmBody);

        // STEP 6: poll for completion
        JobV3 job = h2o.waitForJobCompletion(gbmBody.job.key);
        System.out.println("GBM build done.");

        // STEP 7: fetch the model
        ModelKeyV3 model_key = (ModelKeyV3)job.dest;
        ModelsV3 models = h2o.model(model_key);
        System.out.println("models: " + models);
        GBMModelV3 model = (GBMModelV3)models.models[0];
        System.out.println("new GBM model: " + model);


        // STEP 8: predict!
        ModelMetricsListSchemaV3 predict_params = new ModelMetricsListSchemaV3();
        predict_params.model = model_key;
        predict_params.frame = gbmParms.trainingFrame;
        predict_params.predictionsFrame = H2oApi.stringToFrameKey("predictions");

        ModelMetricsListSchemaV3 predictions = h2o.predict(predict_params);
        System.out.println("predictions: " + predictions);

        // STEP 99: end the session
        h2o.endSession();
    }
}
