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
	
	public abstract Double reduceSequence(Vertex src, Vertex dst);
	
	// FIXME: só pra testar
	public abstract Double reduceSplitJoinGateway(Vertex split, Vertex join, List<Edge> neighbors);
	public abstract Double reduceSplitGateway(Vertex split, List<Edge> neighbors);
	
	/*public abstract Double reduceParallelSplitJoinGateway(Vertex split, Vertex join, List<Edge> neighbors);
	public abstract Double reduceParallelSplitGateway(Vertex split, List<Edge> neighbors);
	
	public abstract Double reduceExclusiveSplitJoinGateway(Vertex split, Vertex join, List<Edge> neighbors);
	public abstract Double reduceExclusiveSplitGateway(Vertex split, List<Edge> neighbors);
	
	public abstract Double reduceInclusiveSplitJoinGateway(Vertex split, Vertex join, List<Edge> neighbors);
	public abstract Double reduceInclusiveSplitGateway(Vertex split, List<Edge> neighbors);*/

}

