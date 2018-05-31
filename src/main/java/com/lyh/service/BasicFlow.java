package com.lyh.service;

import java.io.IOException;
import java.util.UUID;

import water.bindings.H2oApi;
import water.bindings.pojos.FrameKeyV3;
import water.bindings.pojos.ImportFilesV3;
import water.bindings.pojos.ModelMetricsListSchemaV3;
import water.bindings.pojos.ParseSetupV3;
import water.bindings.pojos.ParseV3;
import water.bindings.pojos.RapidsSchemaV3;

public abstract class BasicFlow extends AlgorithmFlowTemplate{


    @Override
    ImportFilesV3 importFile(H2oApi h2o,String path) throws IOException {
        ImportFilesV3 importBody = h2o.importFiles(path, null);
        System.out.println("import: " + importBody);
        return importBody;
    }

    @Override
    ParseSetupV3 parseSetup(H2oApi h2o, ImportFilesV3 importBody) throws IOException {
        ParseSetupV3 parseSetupParams = new ParseSetupV3();
        parseSetupParams.sourceFrames = H2oApi.stringArrayToKeyArray(importBody.destinationFrames, FrameKeyV3.class);
        ParseSetupV3 parseSetupBody = h2o.guessParseSetup(parseSetupParams);
        System.out.println("parseSetupBody: " + parseSetupBody);
        return parseSetupBody;
    }

    @Override
    ParseV3 parse(H2oApi h2o, ParseSetupV3 parseSetupBody, String fileName) throws IOException {
        ParseV3 parseParams = new ParseV3();
        H2oApi.copyFields(parseParams, parseSetupBody);
        parseParams.destinationFrame = H2oApi.stringToFrameKey(fileName+".hex");
        parseParams.blocking = true;
        ParseV3 parseBody = h2o.parse(parseParams);
        System.out.println("parseBody: " + parseBody);
        return parseBody;
    }

    @Override
    void split(H2oApi h2o, String sessionId,String fileName) throws IOException {

        String tmpVec = "tmp_" + UUID.randomUUID().toString();
//        String splitExpr =
//                "(, " +
//                        "  (tmp= " + tmpVec + " (h2o.runif iris.hex 906317))" +
//                        "  (assign train " +
//                        "    (rows iris.hex (<= " + tmpVec + " 0.75)))" +
//                        "  (assign test " +
//                        "    (rows iris.hex (> " + tmpVec + " 0.75)))" +
//                        "  (rm " + tmpVec + "))";
        String splitExpr =
                "(, " +
                        "  (tmp= " + tmpVec + " (h2o.runif "+ fileName+".hex 906317))" +
                        "  (assign train " +
                        "    (rows "+fileName+".hex (<= " + tmpVec + " 0.75)))" +
                        "  (assign test " +
                        "    (rows "+fileName+".hex (> " + tmpVec + " 0.75)))" +
                        "  (rm " + tmpVec + "))";
        RapidsSchemaV3 rapidsParams = new RapidsSchemaV3();
        rapidsParams.sessionId = sessionId;
        rapidsParams.ast = splitExpr;
        h2o.rapidsExec(rapidsParams);
    }



    @Override
    ModelMetricsListSchemaV3 predict(H2oApi h2o,ModelMetricsListSchemaV3 predict_params) throws IOException {
        ModelMetricsListSchemaV3 predictions = h2o.predict(predict_params);
        System.out.println("predictions: " + predictions);

        return predict_params;
    }

}
