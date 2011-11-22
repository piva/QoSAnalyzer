package main.model;

public class Edge {
	public Vertex src, dst;
	public double prob; // Edge probability

	public Edge(Vertex src, Vertex dst){
		this.src = src;
		this.dst = dst;
	}

	public Edge(Vertex src, Vertex dst, double prob){
		this.src = src;
		this.dst = dst;
		this.prob = prob;
	}
}
