package org.alexis.DLXSudoku;

import java.util.Vector;

public class DLX {
	DancingHeader hdr;
	Vector<DancingItem> rows = new Vector<DancingItem>();
	
	public DLX(DancingHeader hdr) {
		this.hdr = hdr;
	}
	
	public void printSolution() {
		for (DancingItem item : rows) {
			System.out.println(((DancingObject)item).getColumn());
		}
	}
	
	public void solve(int k) {
		if (hdr.right() == hdr) {
			System.out.println("SOLUTION at "+k);
			printSolution();
			return;
		}
		
		DancingColumn c = Matrix.getColumnHeuristic(hdr);
		System.out.println(String.format("=== Selecting %s (k=%d) ===",c,k));
		c.cover();
		
		if (c.down() == c) System.out.println("==> FALSE SOLUTION -> rolling back ...");
		
		for (DancingItem downit = c.down(); downit != c; downit = downit.down()) {
			rows.add(downit);
			
			// covering columns at the right
			for (DancingItem rightnode = downit.right(); rightnode != downit; 
					rightnode = rightnode.right()) {
				
				if (rightnode instanceof DancingObject)
					((DancingObject)rightnode).getColumn().cover();
			}
			
			solve(k+1);
			
			rows.remove(downit);
			c = ((DancingObject)downit).getColumn();
			
			// uncovering columns from the left
			for (DancingItem leftnode = downit.left(); leftnode != downit; 
					leftnode = leftnode.left()) {
				
				if (leftnode instanceof DancingObject)
					((DancingObject)leftnode).getColumn().rollback();
			}
		}
		
		c.rollback();
	}
}
