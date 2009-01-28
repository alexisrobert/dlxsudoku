package org.alexis.DLXSudoku;

public class Solver {
	public int test() {
		return 42;
	}
	
	public static void main(String[] args) {
		DancingHeader hdr = new DancingHeader();
		
		System.out.println("Step 1. Linking matrix nodes ...");
		System.out.println();
		
		DancingItem[][] init = new DancingItem[][] {
			{new DancingColumn("A"),new DancingColumn("B"),new DancingColumn("C")},
			{null,null,new DancingObject(1)},
			{new DancingObject(2),new DancingObject(4),null},
			{new DancingObject(3),null,new DancingObject(5)}
		};
		
		Matrix.computeLinks(init);
		Matrix.linkNodesToColumn(init);
		Matrix.linkHeader(init, hdr);
		
		System.out.println(((DancingObject)init[2][0]).getColumn());
		System.out.println(hdr.left().right());
		
		System.out.println();
		System.out.println("Step 2. Computing column sizes ...");
		Matrix.computeSize(init);
		
		System.gc();
		
		System.out.println("Step 3. Solving matrix ...");
		
		DLX dlx = new DLX(hdr);
		dlx.solve(0);
	}
}
