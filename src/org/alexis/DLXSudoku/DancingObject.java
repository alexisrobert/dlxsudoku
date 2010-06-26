package org.alexis.DLXSudoku;

public class DancingObject extends DancingItem {
	private Object data;
	private DancingColumn column;
	private String row;
	
	public DancingObject() {
		super();
	}
	
	public DancingObject(Object data) {
		super();
		this.data = data;
	}
	
	/* Dancing Links part - MAGIC KITTIES INSIDE */
	public void rollback() {
		this.covered = false;
		this.up.down = this;
		this.down.up = this;
	}
	
	public void cover() {
		this.covered = true;
		this.up.down = this.down;
		this.down.up = this.up;
	}
	/* end of magic land */
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public String getRow() {
		return this.row;
	}
	
	public void setRow(String row) {
		this.row = row;
	}
	
	public void setColumn(DancingColumn c) {
		this.column = c;
	}
	
	public DancingColumn getColumn() {
		return this.column;
	}
}
