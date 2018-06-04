package com.lyh.model;



import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KahnTopo {
    private List<Vertex> result; // 用来存储结果集

    private Queue<Vertex> setOfZeroIndegree; // 用来存储入度为0的顶点
    private DAGGraph dagGraph;

    public KahnTopo(DAGGraph dagGraph) {
        this.dagGraph = dagGraph;
        this.result = new ArrayList<>();
        this.setOfZeroIndegree = new LinkedList<>();
        //对入度为0的集合进行初始化
        for (Vertex vertex : this.dagGraph.vertexSet) {
            if (vertex.getInDegree() == 0) {
                this.setOfZeroIndegree.add(vertex);
            }
        }
    }

    /**
     * 拓扑排序
     */
    public void process() {
        //没有起点或者没有边
        if (CollectionUtils.isEmpty(setOfZeroIndegree) || CollectionUtils.isEmpty(dagGraph.adjaVertex.keySet())) {
            throw new IllegalArgumentException("请至少包含一条流程");
        } else if (setOfZeroIndegree.size() != 1) {
            throw new IllegalArgumentException("请只包含一条流程，图中不能包含多个起点");
        }
        //todo
//        else if (!(setOfZeroIndegree.peek() instanceof Start)) {
//            throw new IllegalArgumentException("请以start节点开始");
//        }

        while (!setOfZeroIndegree.isEmpty()) {
            Vertex vertexOne = setOfZeroIndegree.poll();
            //移除该点，放入结果集中
            dagGraph.vertexSet.remove(vertexOne);
            result.add(vertexOne);

            // 遍历由vertexOne引出的所有出边
            if(!CollectionUtils.isEmpty(dagGraph.adjaVertex.get(vertexOne))){
                for (Vertex nextVertexOne : dagGraph.adjaVertex.get(vertexOne)) {
                    // 将该出边从图中移除，通过减少边的数量来表示。即将下一个节点的入度减1。
                    nextVertexOne.decreaseInDegree();
                    // 如果入度为0，那么加入入度为0的集合
                    if (0 == nextVertexOne.getInDegree()) {
                        setOfZeroIndegree.add(nextVertexOne);
                    }
                }
                //移除该点的所有出边
                dagGraph.adjaVertex.remove(vertexOne);
            }
        }

        // 如果此时图中还存在边，那么说明图中含有环路
        if (!dagGraph.vertexSet.isEmpty()) {
            throw new IllegalArgumentException("请不要在流程图中包含环路");
        }

    }

    //结果集
    public Iterable<Vertex> getResult() {
        return result;
    }

    public Queue<Vertex> getSetOfZeroIndegree() {
        return setOfZeroIndegree;
    }

    public void setSetOfZeroIndegree(Queue<Vertex> setOfZeroIndegree) {
        this.setOfZeroIndegree = setOfZeroIndegree;
    }
}

