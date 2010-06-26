package org.alexis.DLXSudoku;

public class DancingColumn extends DancingItem {
	private String name;
	private int size;
	
	public DancingColumn(String name) {
		super();
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void computeSize() {
		this.size = 0;
		DancingItem it = this.down();
		while (it != this) {
			this.size++;
			it = it.down();
		}
	}
	
	public String toString() {
		return String.format("Column %s (size=%d)", this.name, this.size);
	}
	
	public void cover() {
		// Covering column element
		System.out.println("Covering "+this);
		this.covered = true;
		
		for (DancingItem row = this.down(); row != this; row = row.down()) {
			for (DancingItem rightnode = row.right(); rightnode != row; rightnode = rightnode.right()) {
				((DancingObject)rightnode).cover();
				((DancingObject)rightnode).getColumn().size--;
			}
		}
		
		this.right.left = this.left;
		this.left.right = this.right;
		
		Matrix.printMatrix(Matrix.items);
	}
	
	public void rollback() {
		this.covered = false;
		System.out.println("Uncovering "+this.toString());
		
		for (DancingItem row = this.up(); row != this; row = row.up()) {
			for (DancingItem leftnode = row.left(); leftnode != row; leftnode = leftnode.left()) {
				((DancingObject)leftnode).rollback();
				((DancingObject)leftnode).getColumn().size++;
			}
		}
		
		// Uncovering column
		this.left.right = this;
		this.right.left = this;
	}
}
