package org.alexis.DLXSudoku;

public class Matrix {
	/**
	 * This function computes all links between nodes to convert DancingItem[][]
	 * to a double-linked circular list which can be used by DLX.
	 */
	public static void computeLinks(DancingItem[][] matrix) {
		// For each cell ...
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[y].length; x++) {
				if (matrix[y][x] == null) continue;
				
				System.out.println(x+","+y);
				
				/* 1. LEFT/NEXT LINKING */
				
				// Find the <- left existing previous node
				int k = x-1;
				if (x == 0) k = matrix[y].length-1; // i've we're on the first row, link with the other side
				
				while (matrix[y][k] == null && k > 0) { k--; }
				
				// if we've no <- left nodes to link, test -> right
				if (matrix[y][k] == null) {
					k = matrix[y].length-1;
					
					// no k escape because it will link with itselfs
					while (matrix[y][k] == null) { k--; }
				}
				
				// and link with it.
				System.out.println(String.format("Linking %dx%d <-> %dx%d (left/right) ...", k,y,x,y));
				matrix[y][k].setRight(matrix[y][x]);
				matrix[y][x].setLeft(matrix[y][k]);
				
				/* 2. SAME THING WITH UP/DOWN */
				
				k = y-1;
				if (k < 0) k = matrix.length-1;
				
				while (matrix[k][x] == null && k > 0) { k--; }
					
				if (matrix[k][x] == null) {
					k = matrix.length-1;
					while (matrix[k][x] == null) { k--; }
				}
				
				System.out.println(String.format("Linking %dx%d <-> %dx%d (up/down) ...", x,k,x,y));
				matrix[k][x].setDown(matrix[y][x]);
				matrix[y][x].setUp(matrix[k][x]);
			}
		}
	}
	
	/** Launches computeSize() on every column */
	public static void computeSize(DancingItem[][] init) {
		for (DancingItem dancingItem : init[0]) {
			((DancingColumn)dancingItem).computeSize();
		}
	}
	
	/** Sets DancingColumn's column attribute */
	public static void linkNodesToColumn(DancingItem[][] init) {
		// in each column
		DancingItem it = null;
		for (DancingItem column : init[0]) {
			it = column.down();
			
			// for each row in this column
			while (it != column) {
				
				// sets the DancingColumn's column attribute to the current column
				if (it instanceof DancingObject)
					((DancingObject) it).setColumn((DancingColumn)column);
				
				it = it.down();
			}
		}
	}
	
	/** Find the column which has minimal 1's */
	public static DancingColumn getColumnHeuristic(DancingHeader hdr) {
		DancingItem it = hdr.right();
		
		int min = ((DancingColumn)it).getSize();
		DancingColumn minobj = (DancingColumn)it;
		
		while (it != hdr) {
			if (it instanceof DancingColumn && ((DancingColumn)it).getSize() < min) {
				minobj = (DancingColumn)it;
				min = minobj.getSize();
			}
			it = it.right();
		}
		
		return minobj;
	}
	
	/** Links with DancingHeader */
	public static void linkHeader(DancingItem[][] init, DancingHeader hdr) {
		DancingItem first = init[0][0];
		
		hdr.setRight(first);
		hdr.setLeft(first.left());
		
		first.left().setRight(hdr);
		first.setLeft(hdr);
	}
}
