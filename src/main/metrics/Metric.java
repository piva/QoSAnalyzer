package main.metrics;

import java.util.List;

import main.model.Edge;
import main.model.Vertex;

public abstract class Metric {

	/**
	 * This method is called once for each vertex, before running
	 * the reduction algorithm. It should return the initial value
	 * for the vertex
	 * @param v graph vertex
	 * @return the initial value associated with v
	 */
	public abstract Double prepare(Vertex v);
	
	public abstract double reduceSequence(Vertex src, Vertex dst);
	
	// FIXME: só pra testar
	public abstract double reduceSplitJoinGateway(Vertex split, Vertex join, List<Edge> neighbors);
	public abstract double reduceSplitGateway(Vertex split, List<Edge> neighbors);
	
	// Only split gateways
	public abstract double reduceParallelSplitGateway(Vertex split, List<Vertex> neighbors);
	public abstract double reduceExclusiveSplitGateway(Vertex split, List<Vertex> neighbors);
	public abstract double reduceInclusiveSplitGateway(Vertex split, List<Vertex> neighbors);
	
	// Split-Join gateways
	public abstract double reduceParallelSplitJoinGateway(Vertex split, Vertex join, List<Vertex> neighbors);
	public abstract double reduceExclusiveSplitJoinGateway(Vertex split, Vertex join, List<Vertex> neighbors);
	public abstract double reduceInclusiveSplitJoinGateway(Vertex split, Vertex join, List<Vertex> neighbors);
	

}

