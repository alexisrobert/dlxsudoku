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
		
		DancingItem downit = c.down();
		DancingItem lrit = null;
		
		while (downit != c) {
			rows.add(downit);
			
			// covering columns at the right
			lrit = downit.right();
			System.out.println(downit.up().down());
			while (lrit != downit) {
				if (lrit instanceof DancingObject)
					((DancingObject)lrit).getColumn().cover();
				lrit = lrit.right();
			}
			
			solve(k+1);
			
			downit = rows.get(k);
			c = ((DancingObject)downit).getColumn();
			
			// uncovering columns from the left
			lrit = downit.left();
			while (lrit != downit) {
				if (lrit instanceof DancingObject)
					((DancingObject)lrit).getColumn().rollback();
				lrit = lrit.left();
			}
			
			downit = downit.down();
		}
		
		c.rollback();
	}
}
