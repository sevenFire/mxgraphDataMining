package com.lyh.model;

public class BuildModel extends Vertex {
    private String algorithm;

    public BuildModel(String vertexLabel) {
        super(vertexLabel);
    }

    public BuildModel() {
        super("build model");
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
