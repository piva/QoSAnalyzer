package main.metrics;

import java.util.List;

import main.model.Edge;
import main.model.Vertex;

public class PerimeterMetric extends Metric {

	@Override
	public Double prepare(Vertex v) {
		return v.getTime();
	}

	@Override
	public Double reduceSequence(Vertex src, Vertex dst) {
		System.out.println("Reducing sequence " + src.getId() + " " + dst.getId() + " " + src.getTime() + " " + dst.getTime());
		return src.getTime() + dst.getTime();
	}

	@Override
	public Double reduceSplitJoinGateway(Vertex split, Vertex join,
			List<Edge> neighbors) {
		double time = split.getTime() + join.getTime();
		for(Edge e : neighbors){
			time += e.dst.getTime();
		}
		return time;
	}

	@Override
	public Double reduceSplitGateway(Vertex split, List<Edge> neighbors) {
		double time = split.getTime();
		for(Edge e : neighbors){
			System.out.println("Adding neighbor " + e.dst.getId() + " " + e.dst.getTime());
			time += e.dst.getTime();
		}
		System.out.println("Split time " + time);
		return time;
	}
/*
	@Override
	public ouble reduceParallelSplitGateway(Vertex split,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double reduceExclusiveSplitGateway(Vertex split,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double reduceInclusiveSplitGateway(Vertex split,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double reduceParallelSplitJoinGateway(Vertex split, Vertex join,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double reduceExclusiveSplitJoinGateway(Vertex split, Vertex join,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double reduceInclusiveSplitJoinGateway(Vertex split, Vertex join,
			List<Vertex> neighbors) {
		// TODO Auto-generated method stub
		return 0;
	}
*/
}
