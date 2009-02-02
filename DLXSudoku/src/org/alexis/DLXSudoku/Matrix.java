package org.alexis.DLXSudoku;

public class Matrix {
	public static DancingItem[][] items = null;
	/**
	 * This function computes all links between nodes to convert DancingItem[][]
	 * to a double-linked circular list which can be used by DLX.
	 */
	public static void computeLinks(DancingItem[][] matrix) {
		// For each cell ...
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[y].length; x++) {
				if (matrix[y][x] == null) continue;
				
				/* 1. LEFT/NEXT LINKING */
				
				// Find the <- left existing previous node
				int k = x-1;
				if (x == 0) k = matrix[y].length-1; // i've we're on the first row, link with the other side
				
				while (matrix[y][k] == null && k > 0) { k--; }
				
				// if we've no <- left nodes to link, test from the other side
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
				if (y == 0) k = matrix.length-1;
				
				while (k > 0 && matrix[k][x] == null) { k--; }
				
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
		int min = ((DancingColumn)hdr.right()).getSize();
		DancingColumn minobj = (DancingColumn)hdr.right();
		
		for (DancingItem item = hdr.left(); item != hdr; item = item.left()) {
			if (item instanceof DancingColumn && ((DancingColumn)item).getSize() <= min) {
				minobj = (DancingColumn)item;
				min = minobj.getSize();
			}
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
	
	/** Prints Matrix */
	public static void printMatrix(DancingItem[][] matrix) {
		for (int x = 0; x < matrix[0].length; x++) {
			if (matrix[0][x].isCovered()) continue;
			
			System.out.print(((DancingColumn)matrix[0][x]).getName()+" ");
			
			for (int y = 1; y < matrix.length; y++) {
				int k = x;
				
				while (matrix[y][k] == null && k > 0) { k--; }
				if (matrix[y][k] == null) {
					k = x;
					while (matrix[y][k] == null && k < matrix[y].length) { k++; }
				}
				
				if (matrix[y][k] == null || matrix[y][k].isCovered()
						|| ((DancingObject)matrix[y][k]).getColumn().isCovered())
					continue;
				
				if (matrix[y][x] == null) System.out.print("0 ");
				else System.out.print(((DancingObject)matrix[y][x]).getData()+" ");
			}
			
			System.out.println();
		}
	}
}
