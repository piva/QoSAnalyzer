package main.algorithm;

import java.util.LinkedList;
import java.util.Stack;

import main.model.Label;

public class Reducer {
	
	private int adj[][];
	private double prob[][]; // Edge probability
	
	private Label labels[];
	private double times[];
	private int numVertices;
	private int vertices[];
	
	public Reducer(int adj[][], double prob[][], double times[], Label labels[]){
		this.adj = adj;
		this.prob = prob;
		this.labels = labels;
		this.times = times;
		
		numVertices = adj[0].length;
		vertices = new int[numVertices];
		for(int i = 0; i < numVertices; i++){
			vertices[i] = 1;
		}
	}
	
	private void topologicalSorting(){
		
		Stack<Integer> gateways = new Stack<Integer>();
		LinkedList<Integer> ts = new LinkedList<Integer>(); //TODO: queue
		int inDegree[] = new int[numVertices];
		
		// initialization
		for(int i = 0; i < numVertices; i++){
			inDegree[i] = 0;
		}
		
		// Count in degree
		for(int i = 0; i < numVertices; i++){
			for(int j = 0; j < numVertices; j++){
				if(adj[i][j] == 1){
					inDegree[j]++;
				}
			}
		}
		
		for(int i = 0; i < numVertices; i++){
			if(inDegree[i] == 0){
				//TODO: check if is a start vertex
				ts.push(i);
			}
		}
		
		while(ts.size() > 0){
			int vertex = ts.removeFirst();
			System.out.println("Removing vertex " + vertex);

			for(int i = 0; i < numVertices; i++){
				if(adj[vertex][i] == 1){
					inDegree[i]--;
					if(inDegree[i] == 0){
						ts.push(i);
					}
				}
			}
			
			if(labels[vertex].isSplitGateway()){
				System.out.println("Pushing split gateway " + vertex);
				gateways.push(vertex);
			} else if(labels[vertex].isJoinGateway()){
				System.out.println("Found join gateway " + vertex);
				
				if(gateways.empty()){
					throw new RuntimeException("Join gateway with no corresponding split gateway");
				}
				
				// TODO - Check if closing gateway corresponds to open (exclusive - exclusive, fork - fork, ...)
				reduceSplitJoinGateway(gateways.pop(), vertex);
			}

		}
		
		while(!gateways.empty()){
			reduceSplitGateway(gateways.pop());
		}
		
	}

	// Get the times corresponding to reducing all out
	// edges of the gateway
	private double getTimesOfSplitGateway(int split){
		// FIXME: Name of method. It has two responsabilites:
		// getting the time and reducing the edges
		
		double time = 0.0;
		
		for(int i = 0; i < numVertices; i++){
			if(vertices[i] == 1 && adj[split][i] == 1){
				adj[split][i] = 0;
				vertices[i] = 0;
				
				if(labels[split] == Label.FORK_SPLIT){
					time = Math.max(time, times[i]);
				} else if (labels[split] == Label.INCLUSIVE_SPLIT
						|| labels[split] == Label.EXCLUSIVE_SPLIT) {
					time += prob[split][i] * times[i]; 
				}
			}
		}
		
		return time;
	}
	
	private void reduceSplitGateway(int split) {
		System.out.println("reduceSplitGateway " + split);
		
		double time = getTimesOfSplitGateway(split);
		
		int inVertex = getOnlyInVertex(split);
		
		times[inVertex] += time + times[split];
		
		vertices[split] = 0;
		
		adj[inVertex][split] = 0;
		
		labels[inVertex] = Label.END;
	}

	private void reduceSplitJoinGateway(int split, int join) {
		System.out.println("reduceSplitJoinGateway " + split + " " + join);
		
		double time = getTimesOfSplitGateway(split);
		
		int inVertex = getOnlyInVertex(split);
		int outVertex = getOnlyOutVertex(join);
		
		System.out.println("In " + inVertex + " out " + outVertex);
		
		times[split] += time + times[join];
		vertices[join] = 0;
		adj[join][outVertex] = 0;
		adj[split][outVertex] = 1;
		labels[split] = Label.ACTIVITY;
		
		if(isSequence(split, outVertex)){
			split = reduceSequence(new Edge(split, outVertex));
		}
		
		if(isSequence(inVertex, split)){
			split = reduceSequence(new Edge(inVertex, split));
		}
	}
	
	private boolean isSequence(int v1, int v2)
	{
		return vertices[v1] != 0 && vertices[v2] != 0
					&& !labels[v1].isGateway() && !labels[v2].isGateway();
	}

	// returns the resulting vertex
	private int reduceSequence(Edge e){
		
		System.out.println("Reducing sequence " + e.src + " " + e.dst);
		
		// Fix edge and metrics: a->b->c becomes (a+b)->c
			
		adj[e.src][e.dst] = 0;
		vertices[e.dst] = 0; 
		
		int nextVertex;
		
		try {
			nextVertex = getOnlyOutVertex(e.dst); 
			adj[e.src][nextVertex] = 1;
		} catch(RuntimeException exception){
			labels[e.src] = Label.END;
		}

		times[e.src] += times[e.dst];
		
		return e.src;
	}
	
	private int getOnlyInVertex(int vertex) {
		System.out.println("getOnlyInVertex vertex " + vertex);
		int inVertex = -1;
		for(int i = 0; i < numVertices; i++){
			if(vertices[i] == 1 && adj[i][vertex] == 1){
				System.out.println("found vertex " + i);
				if(inVertex != -1){
					throw new RuntimeException("Vertex has more than one in edge");
				}
				inVertex = i;
			}
		}
		
		if(inVertex == -1){
			throw new RuntimeException("Vertex has no in vertices");
		}
		
		return inVertex;
	}
	
	private int getOnlyOutVertex(int vertex) {
		int outVertex = -1;
		
		for(int i = 0; i < numVertices; i++){
			if(adj[vertex][i] == 1){
				if(outVertex != -1){
					throw new RuntimeException("Vertex has more than one out edge");
				}
				outVertex = i;
			}
		}
		
		if(outVertex == -1){
			throw new RuntimeException("Vertex has no out vertices");
		}
		
		return outVertex;
	}

	private Edge findSequence() {
		
		for(int i = 0; i < numVertices; i++) if(vertices[i] != 0 && !labels[i].isGateway()){
			for(int j = 0; j < numVertices; j++) if(vertices[j] != 0 && !labels[j].isGateway()){
				if(adj[i][j] == 1){
					return new Edge(i, j); 
				}
			}
		}
		
		return null;
	}
	
	public double reduce(){
		
		Edge e;
		
		System.out.println("Reducing sequences");
		
		while((e = findSequence())!= null){
			reduceSequence(e);
		}
		
		Utils.printGraph(adj, prob, times, labels, vertices);
		
		System.out.println("Running topological sort");

		topologicalSorting();
		
		for(int i = 0; i < numVertices; i++){
			if(vertices[i] != 0){
				return times[i];
			}
		}
		return -1.0;
	}

}

class Edge {
	public int src, dst;

	Edge(int src, int dst){
		this.src = src;
		this.dst = dst;
	}
	
}