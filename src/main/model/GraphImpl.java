package main.model;

import java.util.ArrayList;
import java.util.List;

public class GraphImpl implements Graph {

	private int numVertices;
	private Vertex vertices [];
	private Edge adjacencyMatrix [][];
	private int inDegree[];
	private int outDegree[];
	
	public GraphImpl(int numVertices){
		this.numVertices = numVertices;
		
		vertices = new Vertex[numVertices];
		inDegree = new int[numVertices];
		outDegree = new int[numVertices];
		adjacencyMatrix = new Edge[numVertices][numVertices];
		
		for(int i = 0; i < numVertices; i++){
			vertices[i] = null;
			inDegree[i] = 0;
			outDegree[i] = 0;
			for(int j = 0; j < numVertices; j++){
				adjacencyMatrix[i][j] = null;
			}
		}
	}
	
	@Override
	public void addVertex(Vertex v) {
		vertices[v.getId()] = v;
	}

	@Override
	public void addEdge(Edge e) {
		adjacencyMatrix[e.getSrc().getId()][e.getDst().getId()] = e;
	}

	@Override
	public boolean areNeighbors(int v0, int v1) {
		return adjacencyMatrix[v0][v1] != null;
	}

	@Override
	public Vertex getVertex(int v) {
		return vertices[v];
	}

	@Override
	public List<Edge> outEdges(int v) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < numVertices; i++){
			if(i != v){
				if(adjacencyMatrix[v][i] != null){
					edges.add(adjacencyMatrix[v][i]);
				}
			}
		}
		return edges;
	}

	@Override
	public List<Edge> inEdges(int v) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < numVertices; i++){
			if(i != v){
				if(adjacencyMatrix[i][v] != null){
					edges.add(adjacencyMatrix[i][v]);
				}
			}
		}
		return edges;
	}

	@Override
	public int vertexInDegree(int v) {
		return 0;
	}

	@Override
	public int vertexOutDegree(int v) {
		return 0;
	}

}
