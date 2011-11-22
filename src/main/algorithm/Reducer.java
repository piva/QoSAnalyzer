package main.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import main.metrics.Metric;
import main.model.Edge;
import main.model.Label;
import main.model.Vertex;

public class Reducer {
	
	List<String> metricNames;
	List<Metric> metricObjects;
	
	List< List<Vertex> > metricVertices; // Copy of the vertices list for each metric
	List< List<Double> > metricValues; // value associated with each metric
	
	private int adj[][];
	private double prob[][]; // Edge probability
	
	private int numVertices;
	private int active[];
	private Vertex vertices[];
	
	private int remainingVertex;
	
	public Reducer(int adj[][], int numVertices, double prob[][], Vertex vertices[]){
		remainingVertex = -1;
		
		metricNames = new ArrayList<String>();
		metricObjects = new ArrayList<Metric>();
		metricValues = new ArrayList<List<Double>>();
		metricVertices = new ArrayList<List<Vertex>>();
		
		this.adj = adj;
		this.prob = prob;
		this.vertices = vertices;
		
		this.numVertices = numVertices;
		active = new int[numVertices];
		for(int i = 0; i < numVertices; i++){
			active[i] = 1;
		}
	}
	
	public void registerMetric(String name, Metric metric){
		metricNames.add(name);
		metricObjects.add(metric);
		
		List<Vertex> initialVertices = new ArrayList<Vertex>();
		List<Double> initialValues = new ArrayList<Double>();
		for(int i = 0; i < numVertices; i++){
			// create a vertex copy so it doesn't use the same
			initialVertices.add(new Vertex(vertices[i]));
			initialValues.add(metric.prepare(vertices[i]));
		}
		
		metricVertices.add(initialVertices);
		metricValues.add(initialValues);
	}
	
	public Double getMetricResult(String name){
		if(remainingVertex == -1){
			throw new RuntimeException("Something went wrong...");
		}
		
		for(int i = 0; i < metricNames.size(); i++){
			if(metricNames.get(i).equals(name)){
				return metricValues.get(i).get(remainingVertex);
			}
		}
		
		throw new RuntimeException("Something went wrong...");
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
			Utils.printGraph(adj, numVertices, prob, active, vertices);

			for(int i = 0; i < numVertices; i++){
				if(adj[vertex][i] == 1){
					inDegree[i]--;
					if(inDegree[i] == 0){
						ts.push(i);
					}
				}
			}
			
			if(vertices[vertex].getLabel().isSplitGateway()){
				System.out.println("Pushing split gateway " + vertex);
				gateways.push(vertex);
			} else if(vertices[vertex].getLabel().isJoinGateway()){
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
			if(active[i] == 1 && adj[split][i] == 1){
				adj[split][i] = 0;
				active[i] = 0;
				
				if(vertices[split].getLabel() == Label.FORK_SPLIT){
					time = Math.max(time, vertices[i].getTime());
				} else if (vertices[split].getLabel() == Label.INCLUSIVE_SPLIT
						|| vertices[split].getLabel() == Label.EXCLUSIVE_SPLIT) {
					time += prob[split][i] * vertices[i].getTime(); 
				}
			}
		}
		System.out.println("time " + time);
		
		return time;
	}
	
	private void reduceSplitGateway(int split) {
		System.out.println("reduceSplitGateway " + split);
		
		List<Edge> outEdges = new ArrayList<Edge>();
		
		for(int i = 0; i < numVertices; i++)
			if(active[i] == 1 && adj[split][i] == 1)
				outEdges.add(new Edge(vertices[split], vertices[i], prob[split][i]));
		
		for(int i = 0; i < metricObjects.size(); i++){
			metricValues.get(i).set(
					split,
					metricObjects.get(i).reduceSplitGateway(
							vertices[split], outEdges));
		}
		
		vertices[split].setTime(vertices[split].getTime() + getTimesOfSplitGateway(split));
		vertices[split].setLabel(Label.END);
		
		reduceSequence(vertices[getOnlyInVertex(split)], vertices[split]);
	}

	private void reduceSplitJoinGateway(int split, int join) {
		System.out.println("reduceSplitJoinGateway " + split + " " + join);
		
		List<Edge> outEdges = new ArrayList<Edge>();
		
		for(int i = 0; i < numVertices; i++)
			if(active[i] == 1 && adj[split][i] == 1)
				outEdges.add(new Edge(vertices[split], vertices[i], prob[split][i]));
		
		for(int i = 0; i < metricObjects.size(); i++){
			metricValues.get(i).set(
					split,
					metricObjects.get(i).reduceSplitJoinGateway(
							vertices[split], vertices[join], outEdges));
		}
		
		double time = getTimesOfSplitGateway(split);
		
		int inVertex = getOnlyInVertex(split);
		int outVertex = getOnlyOutVertex(join);
		
		System.out.println("In " + inVertex + " out " + outVertex);

		vertices[split].setTime(time + vertices[split].getTime() + vertices[join].getTime());
		active[join] = 0;
		adj[join][outVertex] = 0;
		adj[split][outVertex] = 1;
		vertices[split].setLabel(Label.ACTIVITY);
		
		if(isSequence(split, outVertex)){
			split = reduceSequence(vertices[split], vertices[outVertex]);
		}
		
		if(isSequence(inVertex, split)){
			split = reduceSequence(vertices[inVertex], vertices[split]);
		}
	}
	
	private boolean isSequence(int v1, int v2)
	{
		return active[v1] != 0 && active[v2] != 0
					&& !vertices[v1].getLabel().isGateway() && !vertices[v2].getLabel().isGateway();
	}

	// returns the resulting vertex
	private int reduceSequence(Vertex src, Vertex dst){
		System.out.println("Reducing sequence " + src.getId() + " " + dst.getId());

		// Update metrics
		for(int i = 0; i < metricObjects.size(); i++){
			metricValues.get(i).set(src.getId(), metricObjects.get(i).reduceSequence(src, dst));
		}
		
		// Fix edge and metrics: a->b->c becomes (a+b)->c
			
		adj[src.getId()][dst.getId()] = 0;
		active[dst.getId()] = 0; 
		
		int nextVertex;
		
		try {
			nextVertex = getOnlyOutVertex(dst.getId()); 
			adj[src.getId()][nextVertex] = 1;
		} catch(RuntimeException exception){
			vertices[src.getId()].setLabel(Label.END);
		}

		vertices[src.getId()].setTime(vertices[src.getId()].getTime() + vertices[dst.getId()].getTime());
		
		return src.getId();
	}
	
	private int reduceSequence(Edge e){
		return reduceSequence(e.src, e.dst);
	}
	
	private int getOnlyInVertex(int vertex) {
		System.out.println("getOnlyInVertex vertex " + vertex);
		int inVertex = -1;
		for(int i = 0; i < numVertices; i++){
			if(active[i] == 1 && adj[i][vertex] == 1){
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
		for(int i = 0; i < numVertices; i++) if(active[i] != 0 && !vertices[i].getLabel().isGateway()){
			for(int j = 0; j < numVertices; j++) if(active[j] != 0 && !vertices[j].getLabel().isGateway()){
				if(adj[i][j] == 1){
					return new Edge(vertices[i], vertices[j]); 
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
		
		Utils.printGraph(adj, numVertices, prob, active, vertices);
		
		System.out.println("Running topological sort");

		topologicalSorting();
		
		for(int i = 0; i < numVertices; i++){
			if(active[i] != 0){
				remainingVertex = i;
				return vertices[i].getTime();
			}
		}
		return -1.0;
	}

}
