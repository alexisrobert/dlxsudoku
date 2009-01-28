package org.alexis.DLXSudoku;

public class Solver {
	public int test() {
		return 42;
	}
	
	public static void main(String[] args) {
		System.out.println("Step 1. Linking matrix nodes ...");
		System.out.println();
		
		DancingItem[][] init = new DancingItem[][] {
			{new DancingColumn("A"),new DancingColumn("B"),new DancingColumn("C")},
			{null,null,new DancingObject(1)},
			{new DancingObject(1),null,null},
			{new DancingObject(1),null,new DancingObject(1)}
		};
		
		Matrix.computeLinks(init);
		Matrix.linkNodesToColumn(init);
		
		System.out.println(((DancingObject)init[2][0]).getColumn());
		
		System.out.println();
		System.out.println("Step 2. Computing column sizes ...");
		Matrix.computeSize(init);
		
		System.gc();
		
		System.out.println("Step 3. Covering column A ...");
		((DancingColumn) init[0][0]).cover();
		System.out.println();
		
		System.out.println("Step 4. Going left from column B and down 1x2 ...");
		System.out.println((DancingColumn) init[0][2]);
		System.out.println(((DancingColumn) init[0][1]).left());
		System.out.println(((DancingItem) init[1][2]));
		System.out.println(((DancingItem) init[1][2]).down().down());
		
		System.out.println();
		System.out.println("Step 5. Rollback covering ...");
		((DancingColumn) init[0][0]).rollback();
		
		System.out.println();
		
		System.out.println("Step 6. Going left from column B and down 1x2 ...");
		System.out.println((DancingColumn) init[0][2]);
		System.out.println(((DancingColumn) init[0][1]).left());
		System.out.println(((DancingItem) init[1][2]));
		System.out.println(((DancingItem) init[1][2]).down());
	}
}
