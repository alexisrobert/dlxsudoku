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
		
		DancingItem it = this.down();
		DancingItem it2 = null;
		while (it != this) {
			try {
				it2 = it.right();
				while (it2 != it) {
					System.out.println("Covering object "+it2.toString());
					((DancingObject) it2).cover();
					it2 = it2.right();
				}
			} finally {
				it = it.down();
			}
		}
	}
	
	public void rollback() {
		System.out.println("Uncovering "+this.toString());
		DancingItem it = this.up();
		DancingItem it2 = null;
		
		while (it != this) {
			try {
				if (it.left() == null) continue;
				
				it2 = it.left();
				while (it2 != it) {
					System.out.println("Uncovering object "+it2.toString());
					((DancingObject) it2).rollback();
					it2 = it2.left();
				}
			} finally {
				it = it.up();
			}
		}
		
		// Uncovering column
		this.left.right = this;
		this.right.left = this;
	}
}
