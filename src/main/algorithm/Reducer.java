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
			
			if(labels[vertex].isSplitGateway()){
				gateways.push(vertex);
			} else if(labels[vertex].isJoinGateway()){
				
				if(gateways.empty()){
					throw new RuntimeException("Join gateway with no corresponding split gateway");
				}
				
				// TODO - Check if closing gateway corresponds to open (exclusive - exclusive, fork - fork, ...)
				reduceSplitJoinGateway(gateways.pop(), vertex);
			}

			for(int i = 0; i < numVertices; i++){
				if(adj[vertex][i] == 1){
					inDegree[i]--;
					if(inDegree[i] == 0){
						ts.push(i);
					}
				}
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
		
		times[inVertex] += time + times[split] + times[join];
		
		vertices[split] = vertices[join] = 0;
		
		adj[inVertex][split] = 0;
		adj[join][outVertex] = 0;
		
		adj[inVertex][outVertex] = 1;
		
		reduceSequence(new Edge(inVertex, outVertex));
		
	}

	private void reduceSequence(Edge e){
		
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
		
	}
	
//	private void reduceForkSplit(int forkVertex){
//		
//		
//		// Reduce all branches out of the fork
//		double maxTime = 0.0;
//		for(int i = 0; i < numVertices; i++){
//			if(vertices[i] == 1 && adj[forkVertex][i] == 1){
//				if(labels[i] != Label.END){
//					throw new RuntimeException("Fork has not been fully reduced to END vertices");
//				}
//				maxTime = Math.max(maxTime, times[i]);
//				vertices[i] = 0;
//				adj[forkVertex][i] = 0;
//			}
//		}
//		
//		labels[forkVertex] = Label.END;
//		times[forkVertex] += maxTime;
//
//		// reduce fork vertex a->forkVertex to (a+forkVertex)
//		int inVertex = getOnlyInVertex(forkVertex);
//		
//		reduceSequence(new Edge(inVertex, forkVertex));
//		
//	}

	private int getOnlyInVertex(int vertex) {
		System.out.println("vertex " + vertex);
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
	
//	private int findForkSplitVertex(){
//		for(int i = 0; i < numVertices; i++){
//			if(vertices[i] == 1 && labels[i] == Label.FORK_SPLIT){
//				return i;
//			}
//		}
//		return -1;
//	}
	
	public double reduce(){
		
		Edge e;
		
		while((e = findSequence())!= null){
			reduceSequence(e);
		}

		topologicalSorting();
//		int v;
//		while((v = findForkSplitVertex()) >= 0){
//			reduceForkSplit(v);
//		}
		
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