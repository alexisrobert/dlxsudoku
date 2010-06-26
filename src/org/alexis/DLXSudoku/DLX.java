package org.alexis.DLXSudoku;

import java.util.Vector;

public class DLX {
	DancingHeader hdr;
	Vector<DancingItem> rows = new Vector<DancingItem>();
	Sudoku sdk;
	public int[][] grid;
	
	public DLX(DancingHeader hdr, Sudoku sdk) {
		this.hdr = hdr;
		this.sdk = sdk;
	}
	
	public void printSolution() {
		DancingItem it = null;
		for (DancingItem item : rows) {
			it = item;
			do {
				//System.out.println(((DancingObject)it).getColumn());
				System.out.println(((DancingObject)it).getRow());
				it = it.right();
			} while (it != item);
		}
	}
	
	public int[][] fillSolutions(int[][] grid) {
		DancingItem it = null;
		String tmp = null;
		for (DancingItem item : rows) {
			it = item;
			do {
				tmp = ((DancingObject)it).getRow();
				
				int row = Integer.valueOf((String) tmp.subSequence(1, tmp.indexOf("C")))-1;
				int col = Integer.valueOf((String) tmp.subSequence(tmp.indexOf("C")+1, tmp.indexOf("#")))-1;
				int num = Integer.valueOf((String) tmp.subSequence(tmp.indexOf("#")+1, tmp.length()));
				
				grid[row][col] = num;
				it = it.right();
			} while (it != item);
		}
		
		return grid;
	}
	
	public void solve(int k) {
		if (hdr.right().equals(hdr)) {
			System.out.println("SOLUTION at "+k);
			printSolution();
			this.grid = fillSolutions(this.grid);
			//System.exit(0);
			return;
		}
		
		DancingColumn c = Matrix.getColumnHeuristic(hdr);
		
		if (c.getSize() == 0) System.out.println("==> FALSE SOLUTION -> rolling back ...");
		
		System.out.println(String.format("=== Selecting %s (k=%d) ===",c,k));
		
		//sdk.printMatrix();
		
		c.cover();
		
		DancingItem downit = c.down();
		
		while (downit != c) {
			rows.add(downit);
			
			// covering columns at the right
			for (DancingItem rightnode = downit.right(); 
				rightnode != downit; rightnode = rightnode.right()) {
				
				if (rightnode instanceof DancingObject)
					((DancingObject)rightnode).getColumn().cover();
			}
			
			solve(k+1);
			
			downit = rows.get(rows.size()-1);
			c = ((DancingObject)downit).getColumn();
			rows.remove(downit);
			
			// uncovering columns from the left
			for (DancingItem leftnode = downit.left();
				leftnode != downit; leftnode = leftnode.left()) {
				
				if (leftnode instanceof DancingObject)
					((DancingObject)leftnode).getColumn().rollback();
				
			}
			
			downit = downit.down();
		}
		
		c.rollback();
	}
}
