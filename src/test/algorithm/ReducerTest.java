package test.algorithm;

import static org.junit.Assert.assertEquals;
import main.algorithm.Reducer;
import main.metrics.PerimeterMetric;
import main.metrics.ResponseTimeMetric;
import main.model.Label;
import main.model.Vertex;

import org.junit.Before;
import org.junit.Test;

public class ReducerTest {

	final int maxNodes = 100;
	
	private int adj[][];
	private double prob[][];
	private Vertex vertices[];
	
	@Before
	public void setup(){
		adj = new int[maxNodes][maxNodes];
		prob = new double[maxNodes][maxNodes];
		vertices = new Vertex[maxNodes];
		
		for(int i = 0; i < maxNodes; i++){
			vertices[i] = new Vertex(i, 0.0, 1.0);
			for(int j = 0; j < maxNodes; j++){
				adj[i][j] = 0;
				prob[i][j] = 1.0;
			}
		}
	}
	
	@Test
	public void testSimpleSequence(){
		final int nodes = 2;
		
		adj[0][0] = 0;
		adj[0][1] = 1;
		adj[1][0] = 0;
		adj[1][1] = 0;
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				prob[i][j] = 1.0;
			}
		}
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.END);
		
		System.out.println(vertices[0].getTime() + " " + vertices[1].getTime());
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		PerimeterMetric pm = new PerimeterMetric();
		String pName = "Perimeter";
		r.registerMetric(pName, pm);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		System.out.println(vertices[0].getTime() + " " + vertices[1].getTime());
		
		assertEquals(vertices[0].getTime() + vertices[1].getTime(), result, 1e-9);
		assertEquals(r.getMetricResult(metricName), result, 1e-9);
		assertEquals(vertices[0].getTime() + vertices[1].getTime(), r.getMetricResult(pName), 1e-9);
	}
	
	@Test
	public void testSimpleSequence2(){
		final int nodes = 3;
		
		adj[0][0] = 0;
		adj[0][1] = 1;
		adj[0][2] = 0;
		adj[1][0] = 0;
		adj[1][1] = 0;
		adj[1][2] = 1;
		adj[2][0] = 0;
		adj[2][1] = 0;
		adj[2][2] = 0;
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				prob[i][j] = 1.0;
			}
		}
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(1.0);
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.ACTIVITY);
		vertices[2].setLabel(Label.END);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		PerimeterMetric pm = new PerimeterMetric();
		String pName = "Perimeter";
		r.registerMetric(pName, pm);
		
		Double result = r.reduce();
		
		assertEquals(3.0, result, 1e-9);
		assertEquals(r.getMetricResult(metricName), result, 1e-9);
		assertEquals(3.0, r.getMetricResult(pName), 1e-9);
	}
	
	@Test
	public void testSimpleFork(){
		final int nodes = 6;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][5] = 1;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.FORK_SPLIT);
		vertices[2].setLabel(Label.ACTIVITY);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.END);
		vertices[5].setLabel(Label.END);
		
		vertices[0].setTime(0.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(1.0);
		vertices[3].setTime(5.0);
		vertices[4].setTime(0.0);
		vertices[5].setTime(0.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		PerimeterMetric pm = new PerimeterMetric();
		String pName = "Perimeter";
		r.registerMetric(pName, pm);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		//r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		//assertEquals(6.0, result, 1e-9);
		//assertEquals(r.getMetricResult(metricName), result, 1e-9);
		assertEquals(7.0, r.getMetricResult(pName), 1e-9);
		
	}
	
	@Test
	public void testSimpleFork2(){
		final int nodes = 7;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[2][3] = 1;
		adj[2][4] = 1;
		adj[3][5] = 1;
		adj[4][6] = 1;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.ACTIVITY);
		vertices[2].setLabel(Label.FORK_SPLIT);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.ACTIVITY);
		vertices[5].setLabel(Label.END);
		vertices[6].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(1.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(3.0);
		vertices[5].setTime(5.0);
		vertices[6].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(9.0, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}
	
	@Test
	public void testSimpleForkJoin(){
		final int nodes = 6;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.FORK_SPLIT);
		vertices[2].setLabel(Label.ACTIVITY);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.FORK_JOIN);
		vertices[5].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(1.0);
		vertices[3].setTime(2.0);
		vertices[4].setTime(1.0);
		vertices[5].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(6.0, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}
	
	@Test
	public void testSimpleForkJoin2(){
		final int nodes = 6;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.FORK_SPLIT);
		vertices[2].setLabel(Label.ACTIVITY);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.FORK_JOIN);
		vertices[5].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(2.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(1.0);
		vertices[5].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(6.0, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}
	
	@Test
	public void testSimpleExclusiveSplitJoin(){
		final int nodes = 6;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		prob[1][2] = 0.75;
		prob[1][3] = 0.25;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.EXCLUSIVE_SPLIT);
		vertices[2].setLabel(Label.ACTIVITY);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.EXCLUSIVE_JOIN);
		vertices[5].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(1.0);
		vertices[3].setTime(2.0);
		vertices[4].setTime(1.0);
		vertices[5].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(5.25, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}
	
	@Test
	public void testSimpleExclusiveSplitJoin2(){
		final int nodes = 6;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		prob[1][2] = 0.75;
		prob[1][3] = 0.25;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.EXCLUSIVE_SPLIT);
		vertices[2].setLabel(Label.ACTIVITY);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.EXCLUSIVE_JOIN);
		vertices[5].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(2.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(1.0);
		vertices[5].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(5.75, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}

	@Test
	public void testForkWithExclusiveSplitJoin(){
		//              -> A -
		//              |     V 
		//      -> A -> X     X -> E
		//      |       |     ^
		//      |       -> A -
		// S -> F
		//      |
		//      -> E 
		final int nodes = 9;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;
		adj[4][6] = 1;
		adj[5][7] = 1;
		adj[6][7] = 1;
		adj[7][8] = 1;

		prob[4][5] = 0.5;
		prob[4][6] = 0.5;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.FORK_SPLIT);
		vertices[2].setLabel(Label.END);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.EXCLUSIVE_SPLIT);
		vertices[5].setLabel(Label.ACTIVITY);
		vertices[6].setLabel(Label.ACTIVITY);
		vertices[7].setLabel(Label.EXCLUSIVE_JOIN);
		vertices[8].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(3.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(0.0);
		vertices[5].setTime(2.0);
		vertices[6].setTime(1.0);
		vertices[7].setTime(0.0);
		vertices[8].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(5.5, result, 1e-9);
		assertEquals(r.getMetricResult("ResponseTime"), result, 1e-9);
	}
	
	@Test
	public void testForkWithExclusiveSplitJoin2(){
		//              -> A -
		//              |     V 
		//      -> A -> X     X -> E
		//      |       |     ^
		//      |       -> A -
		// S -> F
		//      |
		//      -> E 
		final int nodes = 9;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;
		adj[4][6] = 1;
		adj[5][7] = 1;
		adj[6][7] = 1;
		adj[7][8] = 1;

		prob[4][5] = 0.5;
		prob[4][6] = 0.5;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.FORK_SPLIT);
		vertices[2].setLabel(Label.END);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.EXCLUSIVE_SPLIT);
		vertices[5].setLabel(Label.ACTIVITY);
		vertices[6].setLabel(Label.ACTIVITY);
		vertices[7].setLabel(Label.EXCLUSIVE_JOIN);
		vertices[8].setLabel(Label.END);
		
		vertices[0].setTime(1.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(10.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(0.0);
		vertices[5].setTime(2.0);
		vertices[6].setTime(1.0);
		vertices[7].setTime(0.0);
		vertices[8].setTime(1.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		PerimeterMetric pm = new PerimeterMetric();
		String pName = "Perimeter";
		r.registerMetric(pName, pm);
		
		Double result = r.reduce();
		
		assertEquals(12, result, 1e-9);
		assertEquals(r.getMetricResult(metricName), result, 1e-9);
		assertEquals(17.0, r.getMetricResult(pName), 1e-9);
	}
	
	@Test
	public void testBusinessProcess(){
		//	Business Process Model And Notation P. 299
		
		final int nodes = 14;
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[2][3] = 1;
		adj[2][10] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;
		adj[5][9] = 1;
		adj[4][6] = 1;
		adj[6][7] = 1;
		adj[7][8] = 1;
		adj[8][9] = 1;
		adj[9][11] = 1;
		adj[10][11] = 1;
		adj[11][12] = 1;
		adj[12][13] = 1;
		
		prob[2][3] = 0.5;
		prob[2][10] = 0.5;
		
		vertices[0].setLabel(Label.START);
		vertices[1].setLabel(Label.ACTIVITY);
		vertices[2].setLabel(Label.EXCLUSIVE_SPLIT);
		vertices[3].setLabel(Label.ACTIVITY);
		vertices[4].setLabel(Label.FORK_SPLIT);
		vertices[5].setLabel(Label.ACTIVITY);
		vertices[6].setLabel(Label.ACTIVITY);
		vertices[7].setLabel(Label.ACTIVITY);
		vertices[8].setLabel(Label.ACTIVITY);
		vertices[9].setLabel(Label.FORK_JOIN);
		vertices[10].setLabel(Label.ACTIVITY);
		vertices[11].setLabel(Label.EXCLUSIVE_JOIN);
		vertices[12].setLabel(Label.ACTIVITY);
		vertices[13].setLabel(Label.END);
		
		vertices[0].setTime(0.0);
		vertices[1].setTime(1.0);
		vertices[2].setTime(0.0);
		vertices[3].setTime(1.0);
		vertices[4].setTime(0.0);
		vertices[5].setTime(1.0);
		vertices[6].setTime(1.0);
		vertices[7].setTime(1.0);
		vertices[8].setTime(1.0);
		vertices[9].setTime(0.0);
		vertices[10].setTime(2.0);
		vertices[11].setTime(0.0);
		vertices[12].setTime(1.0);
		vertices[13].setTime(0.0);
		
		Reducer r = new Reducer(adj, nodes, prob, vertices);
		
		PerimeterMetric pm = new PerimeterMetric();
		String pName = "Perimeter";
		r.registerMetric(pName, pm);
		
		ResponseTimeMetric rtm = new ResponseTimeMetric();
		String metricName = "ResponseTime";
		r.registerMetric(metricName, rtm);
		
		Double result = r.reduce();
		
		assertEquals(5.0, result, 1e-9);
		assertEquals(r.getMetricResult(metricName), result, 1e-9);
		assertEquals(9.0, r.getMetricResult(pName), 1e-9);
	}
}
