package test.algorithm;

import static org.junit.Assert.*;
import main.algorithm.Reducer;
import main.model.Label;

import org.junit.Test;

public class ReducerTest {
	
	@Test
	public void testSimpleSequence(){
		int adj[][] = new int[2][2];
		double prob[][] = new double[2][2];
		double times[] = new double[2];
		Label labels[] = new Label[2];
		
		adj[0][0] = 0;
		adj[0][1] = 1;
		adj[1][0] = 0;
		adj[1][0] = 0;
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				prob[i][j] = 1.0;
			}
		}
		
		times[0] = 1.0;
		times[1] = 1.0;
		
		labels[0] = Label.START;
		labels[1] = Label.END;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(2.0, r.reduce(), 1e-9);
		
	}
	
	@Test
	public void testSimpleSequence2(){
		int adj[][] = new int[3][3];
		double prob[][] = new double[3][3];
		double times[] = new double[3];
		Label labels[] = new Label[3];
		
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
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 1.0;
		
		labels[0] = Label.START;
		labels[1] = Label.ACTIVITY;
		labels[2] = Label.END;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(3.0, r.reduce(), 1e-9);
		
	}
	
	@Test
	public void testSimpleFork(){
		int adj[][] = new int[6][6];
		double prob[][] = new double[6][6];
		double times[] = new double[6];
		Label labels[] = new Label[6];
		
		for(int i = 0; i < 6; i++){
			times[i] = 0.0;
			for(int j = 0; j < 6; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][5] = 1;
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				prob[i][j] = 1.0;
			}
		}
		
		labels[0] = Label.START;
		labels[1] = Label.FORK_SPLIT;
		labels[2] = Label.ACTIVITY;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.END;
		labels[5] = Label.END;
		
		times[0] = 0.0;
		times[1] = 1.0;
		times[2] = 1.0;
		times[3] = 5.0;
		times[4] = 0.0;
		times[5] = 0.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(6.0, r.reduce(), 1e-9);
		
	}
	
	@Test
	public void testSimpleFork2(){
		int adj[][] = new int[7][7];
		double prob[][] = new double[7][7];
		double times[] = new double[7];
		Label labels[] = new Label[7];
		
		for(int i = 0; i < 7; i++){
			times[i] = 0.0;
			for(int j = 0; j < 7; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[2][3] = 1;
		adj[2][4] = 1;
		adj[3][5] = 1;
		adj[4][6] = 1;
		
		for(int i = 0; i < 7; i++){
			for(int j = 0; j < 7; j++){
				prob[i][j] = 1.0;
			}
		}
		
		labels[0] = Label.START;
		labels[1] = Label.ACTIVITY;
		labels[2] = Label.FORK_SPLIT;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.ACTIVITY;
		labels[5] = Label.END;
		labels[6] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 1.0;
		times[3] = 1.0;
		times[4] = 3.0;
		times[5] = 5.0;
		times[6] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(9.0, r.reduce(), 1e-9);
	}
	
	@Test
	public void testSimpleForkJoin(){
		int adj[][] = new int[6][6];
		double prob[][] = new double[6][6];
		double times[] = new double[6];
		Label labels[] = new Label[6];
		
		for(int i = 0; i < 6; i++){
			times[i] = 0.0;
			for(int j = 0; j < 6; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				prob[i][j] = 1.0;
			}
		}
		
		labels[0] = Label.START;
		labels[1] = Label.FORK_SPLIT;
		labels[2] = Label.ACTIVITY;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.FORK_JOIN;
		labels[5] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 1.0;
		times[3] = 2.0;
		times[4] = 1.0;
		times[5] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(6.0, r.reduce(), 1e-9);
	}
	
	@Test
	public void testSimpleForkJoin2(){
		int adj[][] = new int[6][6];
		double prob[][] = new double[6][6];
		double times[] = new double[6];
		Label labels[] = new Label[6];
		
		for(int i = 0; i < 6; i++){
			times[i] = 0.0;
			for(int j = 0; j < 6; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				prob[i][j] = 1.0;
			}
		}
		
		labels[0] = Label.START;
		labels[1] = Label.FORK_SPLIT;
		labels[2] = Label.ACTIVITY;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.FORK_JOIN;
		labels[5] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 2.0;
		times[3] = 1.0;
		times[4] = 1.0;
		times[5] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(6.0, r.reduce(), 1e-9);
	}
	
	@Test
	public void testSimpleExclusiveSplitJoin(){
		int adj[][] = new int[6][6];
		double prob[][] = new double[6][6];
		double times[] = new double[6];
		Label labels[] = new Label[6];
		
		for(int i = 0; i < 6; i++){
			times[i] = 0.0;
			for(int j = 0; j < 6; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				prob[i][j] = 1.0;
			}
		}
		
		prob[1][2] = 0.75;
		prob[1][3] = 0.25;
		
		labels[0] = Label.START;
		labels[1] = Label.EXCLUSIVE_SPLIT;
		labels[2] = Label.ACTIVITY;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.EXCLUSIVE_JOIN;
		labels[5] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 1.0;
		times[3] = 2.0;
		times[4] = 1.0;
		times[5] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(5.25, r.reduce(), 1e-9);
	}
	
	@Test
	public void testSimpleExclusiveSplitJoin2(){
		int adj[][] = new int[6][6];
		double prob[][] = new double[6][6];
		double times[] = new double[6];
		Label labels[] = new Label[6];
		
		for(int i = 0; i < 6; i++){
			times[i] = 0.0;
			for(int j = 0; j < 6; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[2][4] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;

		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				prob[i][j] = 1.0;
			}
		}
		
		prob[1][2] = 0.75;
		prob[1][3] = 0.25;
		
		labels[0] = Label.START;
		labels[1] = Label.EXCLUSIVE_SPLIT;
		labels[2] = Label.ACTIVITY;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.EXCLUSIVE_JOIN;
		labels[5] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 2.0;
		times[3] = 1.0;
		times[4] = 1.0;
		times[5] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(5.75, r.reduce(), 1e-9);
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
		
		int adj[][] = new int[9][9];
		double prob[][] = new double[9][9];
		double times[] = new double[9];
		Label labels[] = new Label[9];
		
		for(int i = 0; i < 9; i++){
			times[i] = 0.0;
			for(int j = 0; j < 9; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;
		adj[4][6] = 1;
		adj[5][7] = 1;
		adj[6][7] = 1;
		adj[7][8] = 1;

		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				prob[i][j] = 1.0;
			}
		}
		
		prob[4][5] = 0.5;
		prob[4][6] = 0.5;
		
		labels[0] = Label.START;
		labels[1] = Label.FORK_SPLIT;
		labels[2] = Label.END;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.EXCLUSIVE_SPLIT;
		labels[5] = Label.ACTIVITY;
		labels[6] = Label.ACTIVITY;
		labels[7] = Label.EXCLUSIVE_JOIN;
		labels[8] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 3.0;
		times[3] = 1.0;
		times[4] = 0.0;
		times[5] = 2.0;
		times[6] = 1.0;
		times[7] = 0.0;
		times[8] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(5.5, r.reduce(), 1e-9);
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
		
		int adj[][] = new int[9][9];
		double prob[][] = new double[9][9];
		double times[] = new double[9];
		Label labels[] = new Label[9];
		
		for(int i = 0; i < 9; i++){
			times[i] = 0.0;
			for(int j = 0; j < 9; j++){
				adj[i][j] = 0;
			}
		}
		
		adj[0][1] = 1;
		adj[1][2] = 1;
		adj[1][3] = 1;
		adj[3][4] = 1;
		adj[4][5] = 1;
		adj[4][6] = 1;
		adj[5][7] = 1;
		adj[6][7] = 1;
		adj[7][8] = 1;

		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				prob[i][j] = 1.0;
			}
		}
		
		prob[4][5] = 0.5;
		prob[4][6] = 0.5;
		
		labels[0] = Label.START;
		labels[1] = Label.FORK_SPLIT;
		labels[2] = Label.END;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.EXCLUSIVE_SPLIT;
		labels[5] = Label.ACTIVITY;
		labels[6] = Label.ACTIVITY;
		labels[7] = Label.EXCLUSIVE_JOIN;
		labels[8] = Label.END;
		
		times[0] = 1.0;
		times[1] = 1.0;
		times[2] = 10.0;
		times[3] = 1.0;
		times[4] = 0.0;
		times[5] = 2.0;
		times[6] = 1.0;
		times[7] = 0.0;
		times[8] = 1.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(12, r.reduce(), 1e-9);
	}
	
	@Test
	public void testBusinessProcess(){
		//	Business Process Model And Notation P. 299
		
		final int nodes = 14;
		
		int adj[][] = new int[nodes][nodes];
		double prob[][] = new double[nodes][nodes];
		double times[] = new double[nodes];
		Label labels[] = new Label[nodes];
		
		for(int i = 0; i < nodes; i++){
			times[i] = 0.0;
			for(int j = 0; j < nodes; j++){
				adj[i][j] = 0;
			}
		}
		
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

		for(int i = 0; i < nodes; i++){
			for(int j = 0; j < nodes; j++){
				prob[i][j] = 1.0;
			}
		}
		
		prob[2][3] = 0.5;
		prob[2][10] = 0.5;
		
		labels[0] = Label.START;
		labels[1] = Label.ACTIVITY;
		labels[2] = Label.EXCLUSIVE_SPLIT;
		labels[3] = Label.ACTIVITY;
		labels[4] = Label.FORK_SPLIT;
		labels[5] = Label.ACTIVITY;
		labels[6] = Label.ACTIVITY;
		labels[7] = Label.ACTIVITY;
		labels[8] = Label.ACTIVITY;
		labels[9] = Label.FORK_JOIN;
		labels[10] = Label.ACTIVITY;
		labels[11] = Label.EXCLUSIVE_JOIN;
		labels[12] = Label.ACTIVITY;
		labels[13] = Label.END;
		
		times[0] = 0.0;
		times[1] = 1.0;
		times[2] = 0.0;
		times[3] = 1.0;
		times[4] = 0.0;
		times[5] = 1.0;
		times[6] = 1.0;
		times[7] = 1.0;
		times[8] = 1.0;
		times[9] = 0.0;
		times[10] = 2.0;
		times[11] = 0.0;
		times[12] = 1.0;
		times[13] = 0.0;
		
		Reducer r = new Reducer(adj, prob, times, labels);
		
		assertEquals(4.0, r.reduce(), 1e-9);
	}

}
