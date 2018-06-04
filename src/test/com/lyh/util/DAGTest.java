package com.lyh.util;

import java.util.Stack;

public class DAGTest {
    public static int count1 = 0;
    public static Stack<Character> result1;

    public static void main(String[] args) {
        int[][] adjMatrix = {{0,0,1,0,0},{0,0,1,0,0},{0,0,0,1,1},{0,0,0,0,1},{0,0,0,0,0}};
        int[] value = new int[5];

        //方法1:基于减治法，寻找图中入度为0的顶点作为即将遍历的顶点，遍历完后，将此顶点从图中删除

        //方法2：基于深度优先排序

        dfs(adjMatrix, value);
        System.out.println();
        System.out.println("使用ＤＦＳ方法得到拓扑排序序列的逆序：");
        System.out.println(result1);
        System.out.println("使用ＤＦＳ方法得到拓扑排序序列：");
        while(!result1.empty())
            System.out.println(result1.pop()+"\t");

    }

    /*
    * adjMatrix是待遍历图的邻接矩阵
    * value是待遍历图顶点用于是否被遍历的判断依据，0代表未遍历，非0代表已被遍历
    */
    private static void dfs(int[][] adjMatrix, int[] value) {
        result1 = new Stack<Character>();
        for(int i = 0;i < value.length;i++){
            if(value[i] == 0)
                dfsVisit(adjMatrix,value,i);
        }

    }

    /*
   * adjMatrix是待遍历图的邻接矩阵
   * value是待遍历图顶点用于是否被遍历的判断依据，0代表未遍历，非0代表已被遍历
   * number是当前正在遍历的顶点在邻接矩阵中的数组下标编号
   */
    private static void dfsVisit(int[][] adjMatrix, int[] value, int number) {
        value[number] = ++count1;               //把++count1赋值给当前正在遍历顶点判断值数组元素，变为非0，代表已被遍历
        for(int i = 0;i < value.length;i++){
            if(adjMatrix[number][i] == 1 && value[i] == 0)         //当，当前顶点的相邻有相邻顶点可行走且其为被遍历
                dfsVisit(adjMatrix,value,i);   //执行递归，行走第i个顶点
        }
        char temp = (char) ('a' + number);
        result1.push(temp);
    }


}
