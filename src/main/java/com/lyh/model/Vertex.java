package com.lyh.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 顶点
 */
public class Vertex {
    private String vertexLabel;// 顶点标识
    private int inDegree;// 该顶点的入度

    public Vertex(String vertexLabel) {
        this.vertexLabel = vertexLabel;
        inDegree = 0;
    }

    public void increaseInDegree() {
        inDegree++;
    }

    public void decreaseInDegree() {
        inDegree--;
    }

    public int getInDegree() {
        return inDegree;
    }

    public String getVertexLabel() {
        return vertexLabel;
    }

    public void setVertexLabel(String vertexLabel) {
        this.vertexLabel = vertexLabel;
    }
}
