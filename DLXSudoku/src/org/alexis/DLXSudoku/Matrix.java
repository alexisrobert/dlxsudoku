package org.alexis.DLXSudoku;

public class Matrix {
	/**
	 * This function computes all links between nodes to convert DancingItem[][]
	 * to a double-linked circular list which can be used by DLX.
	 */
	public static void computeLinks(DancingItem[][] matrix) {
		// For each cell ...
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				
				// If you're in the first column, you don't need direct next/prev linking
				// and if you're null, too.
				if (j > 0 && matrix[i][j] != null) {
					
					// Find the next existing previous node
					int k = j-1;
					while (matrix[i][k] == null && k > 0) { k--; }
					
					// and link with it.
					if (matrix[i][k] != null) {
						System.out.println(String.format("Linking %dx%d <-> %dx%d (next/prev) ...", i,k,i,j));
						matrix[i][k].setRight(matrix[i][j]);
						matrix[i][j].setLeft(matrix[i][k]);
					}
				}
				
				// Else, find the last node for circular linking if you're on the first column
				if (j == 0 && matrix[i][j] != null) {
					int k = matrix[i].length-1;
					while(matrix[i][k] == null && k > 0) { k--; }

					if (matrix[i][k] != null) {
						System.out.println(String.format("Circular linking %dx%d <-> %dx%d (next/prev) ...", i,j,i,k));
						matrix[i][j].setLeft(matrix[i][k]);
						matrix[i][k].setRight(matrix[i][j]);
					}
				}
				
				// Do the same thing on up/down-coordinate
				if (i > 0 && matrix[i][j] != null) {
					int k = i-1;

					while (matrix[k][j] == null && k > 0) { k--; }

					if (matrix[k][j] != null) {
						System.out.println(String.format("Linking %dx%d <-> %dx%d (up/down) ...", k,j,i,j));
						matrix[k][j].setDown(matrix[i][j]);
						matrix[i][j].setUp(matrix[k][j]);
					}
				}
				
				// Do the same circular linking on rows
				if (i == 0 && matrix[i][j] != null) {
					int k = matrix.length-1;
					while(matrix[k][j] == null && k > 0) { k--; }

					if (matrix[k][j] != null) {
						System.out.println(String.format("Circular linking %dx%d <-> %dx%d (up/down) ...", i,j,k,j));
						matrix[i][j].setUp(matrix[k][j]);
						matrix[k][j].setDown(matrix[i][j]);
					}
				}
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
}
