package main.algorithm;

import main.model.Vertex;

public class Utils {

	public static void printGraph(int[][] adj, int nodes, double[][] prob, int[] active, Vertex[] vertices) {
		System.out.println("Printing graph");
		
		for(int i = 0; i < nodes; i++) if(active[i] == 1){
			System.out.print(i + " " + vertices[i].getTime() + " " + vertices[i].getLabel() + ": ");
			for(int j = 0; j < nodes; j++){
				if(adj[i][j] == 1){
					System.out.print(" " + j);
				}
			}
			System.out.println();
		}
	}
}
