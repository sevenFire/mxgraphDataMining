package com.lyh.service;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import water.bindings.H2oApi;
import water.bindings.pojos.ColSpecifierV3;
import water.bindings.pojos.GBMModelV3;
import water.bindings.pojos.GBMParametersV3;
import water.bindings.pojos.GBMV3;
import water.bindings.pojos.JobV3;
import water.bindings.pojos.ModelKeyV3;
import water.bindings.pojos.ModelMetricsListSchemaV3;
import water.bindings.pojos.ModelsV3;

public class GBM extends BasicFlow{
    public static final String DEFAULT_PATH = "hdfs://kbmst:9000/user/spark/datasets/arrhythmia.csv";

    public final void computeRemote() throws IOException {
        compute(DEFAULT_REMOTE_URL,DEFAULT_PATH);
    }

    public final void compute() throws IOException {
        compute(DEFAULT_URL,DEFAULT_PATH);
    }

    @Override
    ModelMetricsListSchemaV3 train(H2oApi h2o) throws IOException {
        GBMParametersV3 gbmParms = new GBMParametersV3();
        gbmParms.trainingFrame = H2oApi.stringToFrameKey("train");
        gbmParms.validationFrame = H2oApi.stringToFrameKey("test");

        ColSpecifierV3 responseColumn = new ColSpecifierV3();
        responseColumn.columnName = "C1";
        gbmParms.responseColumn = responseColumn;

        System.out.println("About to train GBM. . .");
        GBMV3 gbmBody = h2o.train_gbm(gbmParms);
        System.out.println("gbmBody: " + gbmBody);

        JobV3 job = h2o.waitForJobCompletion(gbmBody.job.key);
        System.out.println("GBM build done.");

        ModelKeyV3 model_key = (ModelKeyV3)job.dest;
        ModelsV3 models = h2o.model(model_key);
        System.out.println("models: " + models);
        GBMModelV3 model = (GBMModelV3)models.models[0];
        System.out.println("new GBM model: " + model);

        ModelMetricsListSchemaV3 predict_params = new ModelMetricsListSchemaV3();
        predict_params.model = model_key;
        predict_params.frame = gbmParms.trainingFrame;
        predict_params.predictionsFrame = H2oApi.stringToFrameKey("predictions");

        return predict_params;
    }

}
