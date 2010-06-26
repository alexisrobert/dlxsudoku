package org.alexis.DLXSudoku;

public class Solver {
	public int test() {
		return 42;
	}
	
	public static void main(String[] args) {
		DancingHeader hdr = new DancingHeader();
		
		System.out.println("Step 1. Linking matrix nodes ...");
		System.out.println();
		
		/*DancingItem[][] init = new DancingItem[][] {
			{new DancingColumn("A"),new DancingColumn("B"),new DancingColumn("C"),new DancingColumn("D"),new DancingColumn("E"),new DancingColumn("F"),new DancingColumn("G")},
			{null,null,new DancingObject(1),null,new DancingObject(1),new DancingObject(1),null},
			{new DancingObject(1),null,null,new DancingObject(1),null,null,new DancingObject(2)},
			{null,new DancingObject(1),new DancingObject(1),null,null,new DancingObject(1),null},
			{new DancingObject(1),null,null,new DancingObject(1),null,null,null},
			{null,new DancingObject(1),null,null,null,null,new DancingObject(3)},
			{null,null,null,new DancingObject(1),new DancingObject(1),null,new DancingObject(4)}
		};
		
		/*ancingItem[][] init = new DancingItem[][] {
				{new DancingColumn("A"),new DancingColumn("B"),new DancingColumn("C"),new DancingColumn("D"),new DancingColumn("E"),new DancingColumn("F"),new DancingColumn("G")},
				{new DancingObject(1),null,null,new DancingObject(1),null,null,new DancingObject(1)},
				{new DancingObject(1),null,null,new DancingObject(1),null,null,null},
				{null,null,null,new DancingObject(1),new DancingObject(1),null,new DancingObject(1)},
				{null,null,new DancingObject(1),null,new DancingObject(1),new DancingObject(1),null},
				{null,new DancingObject(1),new DancingObject(1),null,null,new DancingObject(1),new DancingObject(1)},
				{null,new DancingObject(1),null,null,null,null,new DancingObject(1)}
		};*/
		
		/*Matrix.computeLinks(init);
		Matrix.linkNodesToColumn(init);
		Matrix.linkHeader(init, hdr);
		
		System.out.println(((DancingObject)init[2][0]).getColumn());
		System.out.println(hdr.left().right());
		
		System.out.println();
		System.out.println("Step 2. Computing column sizes ...");
		Matrix.computeSize(init);
		
		System.gc();
		
		System.out.println("Step 3. Solving matrix ...");
		
		Matrix.items = init;
		Matrix.printMatrix(init);
		
		DLX dlx = new DLX(hdr);
		dlx.solve(0);*/
		
		Sudoku sudoku = new Sudoku(9);
		hdr = sudoku.buildMatrix();
		
		/*int[][] grid = new int[][]{
				{ 2,1,6,0,0,0,3,0,0 },
				{ 0,0,0,6,3,0,0,0,0 },
				{ 0,0,0,0,0,0,7,9,0 },
				{ 7,4,0,0,5,0,6,0,0 },
				{ 0,0,0,0,6,0,0,0,0 },
				{ 0,0,1,0,8,0,0,2,4 },
				{ 0,7,5,0,0,0,0,0,0 },
				{ 0,0,0,0,9,2,0,0,0 },
				{ 0,0,3,0,0,0,9,6,8 }
		};*/
		
		int[][] grid = new int[][] {
				{1,0,0,0,0,0,0,0,2},
				{0,9,0,4,0,0,0,5,0},
				{0,0,6,0,0,0,7,0,0},
				{0,5,0,9,0,3,0,0,0},
				{0,0,0,0,7,0,0,0,0},
				{0,0,0,8,5,0,0,4,0},
				{7,0,0,0,0,0,6,0,0},
				{0,3,0,0,0,9,0,8,0},
				{0,0,2,0,0,0,0,0,1}
		};
		
		System.out.println("=> Inserting grid into matrix ...");
		sudoku.insertGrid(grid);
		
		/*
		sudoku.insertGrid(new int[][]{
				{ 0,2,1,0 },
				{ 1,0,0,0 },
				{ 0,0,0,3 },
				{ 0,3,4,0 }
		});
		/*sudoku.insertGrid(new int[][] {
				{ 1,0 },
				{ 0,0 }
		});*/
		
		System.out.println("=> Solve !");
		
		//sudoku.printMatrix();
		
		DLX dlx = new DLX(hdr,sudoku);
		dlx.grid = grid;
		dlx.solve(0);
		
		for (int[] is : dlx.grid) {
			for (int i : is) {
				System.out.print(i+" ");
			}
			System.out.println();
		}
	}
}
