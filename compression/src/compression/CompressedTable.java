/** 
 * Class representing an efficient implementation of a 2-dimensional table 
 * when lots of repeated entries as a doubly linked list. Idea is to record entry only when a 
 * value changes in the table as scan from left to right through 
 * successive rows.
 * 
 * @author cs62
 * @param <ValueType> type of value stored in the table
 */
package compression;

class CompressedTable<ValueType> implements TwoDTable<ValueType> {
	
	// List holding table entries - do not change
	protected CurDoublyLinkedList<Association<RowOrderedPosn, ValueType>> tableInfo; 

	protected int numRows;
	protected int numCols ;
	protected Association<RowOrderedPosn, ValueType> entry;

	/**
	 * Constructor for table of size rows x cols, all of whose values are initially
	 * set to defaultValue
	 * 
	 * @param rows: # of rows in table
	 * @param cols: # of columns in table
	 * @param defaultValue: initial value of all entries in table
	 */
	public CompressedTable(int rows, int cols, ValueType defaultValue) {
		tableInfo = new CurDoublyLinkedList<Association<RowOrderedPosn, ValueType>>();
		numRows = rows;
		numCols = cols;
		RowOrderedPosn first = new RowOrderedPosn(0, 0, rows, cols);
		Association<RowOrderedPosn,ValueType> def = new Association<RowOrderedPosn,ValueType> (first, defaultValue); 
		tableInfo.addFirst(def);
		
	}

	/**
	 * Given a (x, y, rows, cols) RowOrderedPosn object, it searches for it in the
	 * table which is represented as a doubly linked list with a current pointer. If
	 * the table contains the (x,y) cell, it sets the current pointer to it.
	 * Otherwise it sets it to the closest cell in the table which comes before that
	 * entry.
	 * 
	 * e.g., if the table only contains a cell at (0,0) and you pass the cell (3,3)
	 * it will set the current to (0,0).
	 */
	private void find(RowOrderedPosn findPos) {
		tableInfo.first();
		Association<RowOrderedPosn, ValueType> entry = tableInfo.currentValue();
		RowOrderedPosn pos = entry.getKey();
		while (!findPos.less(pos)) {
			// search through list until pass elt looking for
			tableInfo.next();
			if (tableInfo.isOff()) {
				break;
			}
			entry = tableInfo.currentValue();
			pos = entry.getKey();
		}
		tableInfo.back(); // Since passed desired entry, go back to it.
	}

	/**
	 * Given a legal (row, col) cell in the table, update its value to newInfo. 
	 * 
	 * @param row:row of cell to be updated
	 * @param col: column of cell to be update
	 * @param newInfo: new value to place in cell (row, col)
	 */
	public void updateInfo(int row, int col, ValueType newInfo) {
		
		//if the provided parameters are outside of the array, end the method
		if (row > (numRows-1) || col > (numCols-1)) {
			return;
		}
		
		//UPDATE PROCESS
		RowOrderedPosn target = new RowOrderedPosn(row, col, numRows, numCols);
		find(target);
		
		//if the newInfo matches the already existing info, end the method
		if (tableInfo.currentValue().getValue().equals(newInfo)) {
			return;
		}
		
		//preserve the old info
		ValueType oldInfo = tableInfo.currentValue().getValue();
		
		//if the to-be-updated node doesn't exist
		if (!tableInfo.currentValue().getKey().equals(target)) {
			
			//create the node and add it to the list
			Association<RowOrderedPosn, ValueType> newNode = 
					new Association<RowOrderedPosn,ValueType> (target, newInfo); 
			tableInfo.addAfterCurrent(newNode); 
			
		//if it does exist, update it 
		} else {
			tableInfo.currentValue().setValue(newInfo);
		}
		
		//STOPPER PRESERVATION PROCESS
		if (!(row == (numRows-1) && col == (numCols-1))) {
			tableInfo.next(); 
			
			//if we aren't at the end of the list or the next node doesn't match the next position
			if (tableInfo.isOff() || !tableInfo.currentValue().getKey().equals(target.next())) {
				Association<RowOrderedPosn, ValueType> stopper = 
						new Association<RowOrderedPosn,ValueType> (target.next(), oldInfo); 
				tableInfo.back(); 
				tableInfo.addAfterCurrent(stopper);
			}
		}
		
		//REDUNDANCY ELIMINATION PROCESS
		
		find(target);
		tableInfo.next();
		
		//target node was last item 
		if (tableInfo.isOff()) {
			find(target);
			tableInfo.back();
			
			//target node is also the first item; i.e., target node is the only item
			if(tableInfo.isOff()) {
				find(target);
				return;
			
			//target node is the last item, but not the only item 
			} else {
				
				//assign this node a 'prior' reference
				Association<RowOrderedPosn, ValueType> prior = tableInfo.currentValue();
			
				// prior node value is same as the update value
				if (prior.getValue().equals(newInfo)) {
					find(target);
					tableInfo.removeCurrent();
				}
			
			}
		
		//target node is not the last item	
		} else {
			Association<RowOrderedPosn, ValueType> succ = tableInfo.currentValue();
			find(target);
			tableInfo.back();
			
			//target node is the first item, but not only item
			if (tableInfo.isOff()) {
				find(target);
				
				//succeeding node value is same as the update value
				if (succ.getValue().equals(newInfo)) {
					tableInfo.next();
					tableInfo.removeCurrent();
					}
			
			//target node is not the first or last item
			} else {
				
				Association<RowOrderedPosn, ValueType> prior = tableInfo.currentValue();
				find(target);
				
				//prior and succeeding node value are the same as update value
				if (prior.getValue().equals(newInfo) && succ.getValue().equals(newInfo)) {
					tableInfo.removeCurrent();
					tableInfo.removeCurrent();
				
				//prior node value is not the same as update value, but succeeding node value is
				} else if (!(prior.getValue().equals(newInfo)) && succ.getValue().equals(newInfo)) {
					tableInfo.next();
					tableInfo.removeCurrent();
				
				//succeeding value is not the same as update value, but prior node value is
				} else if (prior.getValue().equals(newInfo) && !(succ.getValue().equals(newInfo))) {
					tableInfo.removeCurrent();
				
				//both prior and succeeding node value are not the same as update value
				} else {
					return;
				}
			}
		}
	}
		

	/**
	 * Returns contents of specified cell
	 * 
	 * @pre: (row,col) is legal cell in table
	 * 
	 * @param row: row of cell to be queried
	 * @param col: column of cell to be queried
	 * 
	 * @return value stored in (row, col) cell of table
	 */
	public ValueType getInfo(int row, int col) {
		RowOrderedPosn target = new RowOrderedPosn(row, col, numRows, numCols);
		find(target);
		return tableInfo.currentValue().getValue();
	
	}

	/**
	 *  @return
	 *  		 succinct description of contents of table
	 */
	public String toString() { // do not change
	    return tableInfo.otherString();
	}

	public String entireTable() { //do not change
		StringBuilder ans = new StringBuilder("");
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				ans.append(this.getInfo(r, c));
			}
			ans.append("\n");
		}
		return ans.toString();

	}

	/**
	 * program to test implementation of CompressedTable
	 * @param args
	 * 			ignored, as not used in main
	 */
	public static void main(String[] args) {
		
		// add your own tests to make sure your implementation is correct!!
		CompressedTable<String> table = new CompressedTable<String>(5, 6, "x");
		System.out.println("table is " + table);
		table.updateInfo(0, 1, "a");
		System.out.println("table is " + table);
		table.updateInfo(0, 1, "x");
		System.out.println("table is " + table);	
		
	}

}
