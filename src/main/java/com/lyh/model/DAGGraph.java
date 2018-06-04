package com.lyh.model;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用来实现拓扑排序的有向无环图
 */
public class DAGGraph {
    // 边的集合
    public Set<Edge> edgeSet = new HashSet<>();
    // id->vertex的map
    public Map<String,Vertex> vertexMap = new HashMap<>();

    // 图中节点的集合
    public Set<Vertex> vertexSet = new HashSet<>();
    // 相邻的节点，纪录边
    public Map<Vertex, Set<Vertex>> adjaVertex = new HashMap<>();

    public boolean addEdge(Edge edge) {
        this.edgeSet.add(edge);
        return true;
    }

    public boolean addVertex(String id,Vertex vertex) {
        this.vertexMap.put(id,vertex);
        return true;
    }


    public boolean buildGraph() {
        for (Edge edgeOne : edgeSet) {
            Vertex startVertex = vertexMap.get(edgeOne.getStartVertexId());
            Vertex endVertex = vertexMap.get(edgeOne.getEndVertexId());
            addVertex(startVertex,endVertex);
        }
        return true;
    }



    // 将节点加入图中  
    public boolean addVertex(Vertex start, Vertex end) {
        //如果顶点不存在
        if (!vertexSet.contains(start)) {
            vertexSet.add(start);
        }
        if (!vertexSet.contains(end)) {
            vertexSet.add(end);
        }

        //如果边已经存在(start有邻边集且邻边集中包含end)
        if (adjaVertex.containsKey(start) && adjaVertex.get(start).contains(end)) {
            return false;
        }

        //如果start节点没有邻边集
        if (!adjaVertex.containsKey(start)) {
            Set<Vertex> vertexSetTmp = new HashSet<>();
            vertexSetTmp.add(end);
            adjaVertex.put(start,vertexSetTmp);
        }else{
            adjaVertex.get(start).add(end);
        }

        //为end增加入度
        end.increaseInDegree();
        return true;
    }


}
