package main.model;

import java.util.List;

public interface Graph {

	public void addVertex(Vertex v);
	public void addEdge(Edge e);
	
	public Vertex getVertex(int v);
	public boolean areNeighbors(int v0, int v1);
	
	public int vertexInDegree(int v);
	public int vertexOutDegree(int v);
	
	List<Edge> outEdges(int v);
	List<Edge> inEdges(int v);

}
