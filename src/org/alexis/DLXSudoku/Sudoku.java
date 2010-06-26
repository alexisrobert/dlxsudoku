package org.alexis.DLXSudoku;

import java.util.HashMap;
import java.util.Vector;

public class Sudoku {
	private int size;
	private int block_step;
	private HashMap<String,DancingColumn> columnindex;
	private HashMap<String,DancingObject> rowindex;
	
	public Sudoku(int size) {
		this.size = size;
		this.block_step = (int) Math.sqrt(size);
		this.columnindex = new HashMap<String,DancingColumn>();
		this.rowindex = new HashMap<String,DancingObject>();
	}
	
	private Vector<String> getColumns() {
		Vector<String> vect = new Vector<String>();
		
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				vect.add(String.format("R%dC%d",row+1,column+1));
			}
		}
		
		for (int row = 0; row < size; row++) {
			for (int number = 0; number < size; number++) {
				vect.add(String.format("R%d#%d",row+1,number+1));
			}
		}
		
		for (int column = 0; column < size; column++) {
			for (int number = 0; number < size; number++) {
				vect.add(String.format("C%d#%d",column+1,number+1));
			}
		}
		
		return vect;
	}
	
	private void buildColumns(DancingHeader hdr) {
		System.out.println(String.format("- Building %d columns ...",3*size*size));
		
		// 4 constraints, each for (x,y)
		
		DancingItem colptr = hdr;
		
		// First constraint : row-column constraint or cell constraint
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				DancingColumn cell = new DancingColumn(
						String.format("R%dC%d",row+1,column+1));
				
				columnindex.put(cell.getName(), cell);
				
				colptr.setRight(cell);
				cell.setLeft(colptr);
				colptr = (DancingItem)cell;
				System.out.println(cell);
			}
		}
		
		// Second constraint : row-number constraint
		for (int row = 0; row < size; row++) {
			for (int number = 0; number < size; number++) {
				DancingColumn rownumber = new DancingColumn(
						String.format("R%d#%d",row+1,number+1));
				
				columnindex.put(rownumber.getName(), rownumber);
				
				colptr.setRight(rownumber);
				rownumber.setLeft(colptr);
				colptr = (DancingItem)rownumber;
				System.out.println(rownumber);
			}
		}
		
		
		// Third constraint : column-number constraint
		for (int column = 0; column < size; column++) {
			for (int number = 0; number < size; number++) {
				DancingColumn colnumber = new DancingColumn(
						String.format("C%d#%d",column+1,number+1));
				
				columnindex.put(colnumber.getName(), colnumber);
				
				colptr.setRight(colnumber);
				colnumber.setLeft(colptr);
				colptr = (DancingItem)colnumber;
				System.out.println(colnumber);
			}
		}
		
		// Forth constraint : bow-number constraint
		for (int box = 0 ; box < Math.pow((((float)size)/block_step),2); box++) {
			for (int number = 0; number < size; number++) {
				DancingColumn colbox = new DancingColumn(
						String.format("B%d#%d", box+1, number+1));
				
				columnindex.put(colbox.getName(), colbox);
				
				colptr.setRight(colbox);
				colbox.setLeft(colptr);
				
				colptr = (DancingItem)colbox;
				System.out.println(colptr);
			}
		}
	}
	
	public DancingObject makeItem(int idx, DancingColumn[] columnlinks, String rowname) {
		DancingColumn column = columnlinks[idx];
		DancingObject obj = new DancingObject(0);
		obj.setRow(rowname);
		obj.setColumn(column);
		column.setSize(column.getSize()+1);
		
		// 1. Link with the last object of this column (for up/down link)
		DancingItem ptr = column;
		while(ptr.down() != null && ptr.down() != column) { ptr = ptr.down(); }
		ptr.setDown(obj);
		obj.setUp(ptr);
		
		// 2. Link with the last object of this row (for prev/next link)
		if (idx > 0) {
			DancingColumn columnlink = columnlinks[idx-1];
			
			// fetch the last item of this column, and link with it left
			ptr = columnlink;
			while(ptr.down() != null && ptr.down() != columnlink) { ptr = ptr.down(); }
			ptr.setRight(obj);
			obj.setLeft(ptr);
			
			System.out.println(column+" linking with "+columnlink);
		}
		
		// 3. If we've created the last node of the column, 
		//    link it with the first one (toroidally)
		if (idx == columnlinks.length-1) {
			DancingColumn columnlink = columnlinks[0];
			
			ptr = columnlink;
			while(ptr.down() != null && ptr.down() != columnlink) { ptr = ptr.down(); }
			ptr.setLeft(obj);
			obj.setRight(ptr);
			
			System.out.println(column+" linking with "+columnlink);
		}
		
		return obj;
	}
	
	public void insertItems(DancingHeader hdr) {
		System.out.println("- Inserting items ...");
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				for (int number = 0; number < size; number++) {
					// WARNING! Must be in the same order than columns in your matrix!
					DancingColumn columns[] = new DancingColumn[] {
							columnindex.get(String.format("R%dC%d",row+1,column+1)),
							columnindex.get(String.format("R%d#%d",row+1,number+1)),
							columnindex.get(String.format("C%d#%d",column+1,number+1)),
							columnindex.get(String.format("B%d#%d",getBox(row+1,column+1),number+1))
					};
					
					DancingObject curobj = null;
					
					for (int i = 0; i < columns.length; i++) {
						curobj = makeItem(i, columns, String.format("R%dC%d#%d",
								row+1,column+1,number+1));
						if (i==0) rowindex.put(curobj.getRow(), curobj);
					}
				}
			}
		}
	}
	
	public void toricLinks(DancingHeader hdr) {
		// This function will not make ALL toric links because
		// a lot of them (nodes' left/right ones) are made by
		// insertItems.
		
		System.out.println("- Finish toric links ...");
		
		// 1. Columns up/down
		for (String columnname : columnindex.keySet()) {
			DancingColumn column = columnindex.get(columnname);
			
			DancingItem ptr = column;
			while(ptr.down() != null && ptr.down() != column) { ptr = ptr.down(); }
			ptr.setDown(column);
			column.setUp(ptr);
		}
		
		// 2. Columns left/right
		DancingColumn column = (DancingColumn)hdr.right();
		DancingColumn ptr = column;
		
		while(ptr.right() != null && ptr.right() != hdr) { ptr = (DancingColumn)ptr.right(); }
		ptr.setRight(hdr);
		hdr.setLeft(ptr);
	}
	
	public DancingHeader buildMatrix() {
		System.out.println(String.format("==> Pre-building Sudoku matrix step (size=%d) ...",size));
		DancingHeader hdr = new DancingHeader();
		
		buildColumns(hdr);
		insertItems(hdr);
		toricLinks(hdr);
		
		return hdr;
	}
	
	public void insertGrid(int[][] grid) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				if (grid[r][c] != 0) {
					System.out.println(String.format("Selecting row R%dC%d#%d", r+1,c+1,grid[r][c]));
					selectRow(String.format("R%dC%d#%d",r+1,c+1,grid[r][c]));
				}
			}
		}
	}
	
	private void selectRow(String index) {
		DancingItem start = rowindex.get(index);
		DancingItem it = start;
		do {
			((DancingObject)it).getColumn().cover();
			it = it.right();
		} while (it != start);
	}
	
	public void printMatrix() {
		DancingObject row = null;
		System.out.print(",");
		for (String colname : getColumns()) {
			System.out.print(colname);
			if (columnindex.get(String.format(colname)).isCovered()) System.out.print("*");
			System.out.print(",");
		}
		System.out.println();
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				for (int n = 0; n < size; n++) {
					System.out.print(String.format("R%dC%d#%d,",r+1,c+1,n+1));
					row = rowindex.get(String.format("R%dC%d#%d",r+1,c+1,n+1));
					
					for (String colname : getColumns()) {
						if (row.getColumn().getName().equals(colname)) {
							if (row.isCovered()) System.out.print("*,");
							else System.out.print("1,");
							row = (DancingObject)row.right();
							if (row.equals(rowindex.get(String.format("R%dC%d#%d",r+1,c+1,n+1))))
								break;
						} else {
							System.out.print("0,");
						}
					}
					System.out.println("");
				}
			}
		}
	}
	
	public int getBox(int r, int c) {
		int row = r-1;
		int col = c-1;
		return ((row/block_step)+1) + block_step*(col/block_step); // haha, i'm not sure that's right!
	}
}