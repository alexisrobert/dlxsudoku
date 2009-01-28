package org.alexis.DLXSudoku;

public abstract class DancingItem {
	protected DancingItem left;
	protected DancingItem right;
	protected DancingItem up;
	protected DancingItem down;
	
	public DancingItem() {}
	
	public void insertNext(DancingItem obj) {
		this.right = obj;
		obj.left = this;
	}
	
	public void insertUp(DancingItem obj) {
		this.up = obj;
		obj.down = this;
	}
	
	public void insertDown(DancingItem obj) {
		this.down = obj;
		obj.up = this;
	}
	
	public DancingItem up() {
		return this.up;
	}
	
	public DancingItem down() {
		return this.down;
	}
	
	public DancingItem left() {
		return this.left;
	}
	
	public DancingItem right() {
		return this.right;
	}
	
	public void setRight(DancingItem item) {
		this.right = item;
	}
	
	public void setLeft(DancingItem item) {
		this.left = item;
	}
	
	public void setUp(DancingItem item) {
		this.up = item;
	}
	
	public void setDown(DancingItem item) {
		this.down = item;
	}
}
