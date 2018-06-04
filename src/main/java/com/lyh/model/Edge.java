
package com.lyh.model;


/**
 * 边
 */
public class Edge {
    private String startVertexId;
    private String endVertexId;


    public Edge(String startVertexId, String endVertexId) {
        this.startVertexId = startVertexId;
        this.endVertexId = endVertexId;
    }

    public String getStartVertexId() {
        return startVertexId;
    }

    public void setStartVertexId(String startVertexId) {
        this.startVertexId = startVertexId;
    }

    public String getEndVertexId() {
        return endVertexId;
    }

    public void setEndVertexId(String endVertexId) {
        this.endVertexId = endVertexId;
    }
}
