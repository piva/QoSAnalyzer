package main.model;

public class Edge {
	private Vertex src;
	private Vertex dst;
	
	private double probability;

	public Edge(Vertex src, Vertex dst, double probability) {
		super();
		this.src = src;
		this.dst = dst;
		this.probability = probability;
	}

	public Vertex getSrc() {
		return src;
	}

	public Vertex getDst() {
		return dst;
	}

	public double getProbability() {
		return probability;
	}

}
