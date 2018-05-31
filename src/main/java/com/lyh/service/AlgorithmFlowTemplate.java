package com.lyh.service;

import java.io.File;
import java.io.IOException;

import water.bindings.H2oApi;
import water.bindings.pojos.ImportFilesV3;
import water.bindings.pojos.ModelMetricsListSchemaV3;
import water.bindings.pojos.ParseSetupV3;
import water.bindings.pojos.ParseV3;

public abstract class AlgorithmFlowTemplate {
    public static final String DEFAULT_URL = "http://localhost:54321/";
    public static final String DEFAULT_REMOTE_URL = "http://168.2.8.130:54321/";

    abstract ImportFilesV3 importFile(H2oApi h2o, String path) throws IOException;
    abstract ParseSetupV3 parseSetup(H2oApi h2o, ImportFilesV3 importBody) throws IOException;
    abstract ParseV3 parse(H2oApi h2o, ParseSetupV3 parseSetupBody, String fileName) throws IOException;
    abstract void split(H2oApi h2o, String id, String sessionId) throws IOException;
    abstract ModelMetricsListSchemaV3 train(H2oApi h2o) throws IOException;
    abstract ModelMetricsListSchemaV3 predict(H2oApi h2o,ModelMetricsListSchemaV3 predict_params) throws IOException;

    public final void compute(String url,String path) throws IOException {
        //0 init a h2o session
        H2oApi h2o = new H2oApi(url);
        System.out.println("url:"+url);
        String sessionId = h2o.newSession().sessionKey;

        //1 import file
        System.out.println("path: "+path);
        ImportFilesV3 importBody = importFile(h2o,path);

        File file =new File(path.trim());
        String fileName = file.getName();
        System.out.println("fileName: "+fileName);

        // 2 parse file
        ParseSetupV3 parseSetupBody = parseSetup(h2o,importBody);
        ParseV3 parseBody = parse(h2o,parseSetupBody,fileName);

        //3 split data into train and test
        split(h2o,sessionId,fileName);

        //4 train with the algorithm
        ModelMetricsListSchemaV3 predict_params = train(h2o);

        //5 predict
        ModelMetricsListSchemaV3 predictions = predict(h2o,predict_params);

        //6 stop h2o session
        h2o.endSession();

    }



}
