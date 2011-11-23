package main.metrics;

import java.util.List;

import main.model.Edge;
import main.model.Label;
import main.model.Vertex;

public class ResponseTimeMetric extends Metric {

	@Override
	public Double prepare(Vertex v) {
		return v.getTime();
	}

	@Override
	public Double reduceSequence(Vertex src, Vertex dst) {
		return src.getTime() + dst.getTime();
	}
	
	@Override
	public Double reduceSplitJoinGateway(Vertex split, Vertex join,
			List<Edge> neighbors) {
		return reduceSplitGateway(split, neighbors) + join.getTime();
	}

	@Override
	public Double reduceSplitGateway(Vertex split, List<Edge> neighbors) {
		double time = 0.0;
		
		for(Edge e : neighbors){
			if(split.getLabel() == Label.FORK_SPLIT){
				time = Math.max(time, e.dst.getTime());
			} else if (split.getLabel() == Label.INCLUSIVE_SPLIT
					|| split.getLabel() == Label.EXCLUSIVE_SPLIT) {
				time += e.prob * e.dst.getTime(); 
			}
			
		}
		return split.getTime() + time;
	}
/*
	@Override
	public double reduceParallelSplitGateway(Vertex split,
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
