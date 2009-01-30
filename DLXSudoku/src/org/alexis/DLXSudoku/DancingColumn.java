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
	
	public void setSize(int size) {
		this.size = size;
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
		this.right.left = this.left;
		this.left.right = this.right;
		
		System.out.println("Covering "+this.toString());
		
		for (DancingItem row = this.down(); row != this; row = row.down()) {
			for (DancingItem right = row.right(); right != row; right = right.right()) {
				((DancingObject) right).cover();
			}
		}
	}
	
	public void rollback() {
		System.out.println("Uncovering "+this.toString());
		
		for (DancingItem row = this.up(); row != this; row = row.up()) {
			for (DancingItem left = row.left(); left != row; left = left.left()) {
				((DancingObject) left).rollback();
			}
		}
		
		// Uncovering column
		this.left.right = this;
		this.right.left = this;
	}
}
