package com.lyh.util;

import com.lyh.model.DAGGraph;
import com.lyh.model.KahnTopo;
import com.lyh.model.Vertex;

public class KahnTopoTest {
    public static void main(String[] args) {
        Vertex a = new Vertex("a");
        Vertex b = new Vertex("b");
        Vertex c = new Vertex("c");
        Vertex d = new Vertex("d");
        Vertex e = new Vertex("e");
        Vertex f = new Vertex("f");

        DAGGraph dagGraph = new DAGGraph();
        dagGraph.addVertex(a,c);
        dagGraph.addVertex(c,d);
        dagGraph.addVertex(d,b);
        dagGraph.addVertex(b,f);
        dagGraph.addVertex(f,e);

        KahnTopo topo = new KahnTopo(dagGraph);
        topo.process();
        for(Vertex vertex : topo.getResult()){
            System.out.print(vertex.getVertexLabel()+ "-->");
        }

    }
}
