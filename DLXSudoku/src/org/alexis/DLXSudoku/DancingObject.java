package org.alexis.DLXSudoku;

public class DancingObject extends DancingItem {
	private Object data;
	private DancingColumn column;
	
	public DancingObject() {
		super();
	}
	
	public DancingObject(Object data) {
		super();
		this.data = data;
	}
	
	/* Dancing Links part - MAGIC KITTIES INSIDE */
	public void rollback() {
		this.up.down = this;
		this.down.up = this;
		
		this.column.setSize(this.column.getSize()+1);
	}
	
	public void cover() {
		this.up.down = this.down;
		this.down.up = this.up;
		
		this.column.setSize(this.column.getSize()-1);
	}
	/* end of magic land */
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public void setColumn(DancingColumn c) {
		this.column = c;
	}
	
	public DancingColumn getColumn() {
		return this.column;
	}
}
