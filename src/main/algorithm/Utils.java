package main.algorithm;

import main.model.Label;

public class Utils {

	public static void printGraph(int[][] adj, double[][] prob, double[] times,
			Label[] labels, int[] vertices) {
		System.out.println("Printing graph");
		int nodes = adj.length;
		
		for(int i = 0; i < nodes; i++) if(vertices[i] == 1){
			System.out.print(i + " " + labels[i] + ": ");
			for(int j = 0; j < nodes; j++){
				if(adj[i][j] == 1){
					System.out.print(" " + j);
				}
			}
			System.out.println();
		}
	}
}
