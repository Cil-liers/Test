import java.util.concurrent.RecursiveTask;


public class MyThread extends RecursiveTask<Boolean> {

	int startRow;
	int startCol;
	int endRow;
	int endCol;
	GridDuplicate myGrid;

	public MyThread(GridDuplicate grd) {
		myGrid = grd;
		// grd.printGrid();

		startRow = 1;
		startCol = 1;
		endRow = grd.getRows()+1;
		// System.out.println("The endRow is " + endRow);
		endCol = grd.getColumns()+1;
		// System.out.println("The endCol is " + endCol);
	}

	public MyThread(GridDuplicate grd, int startR, int startC, int endR, int endC) {
		myGrid = grd;
		startRow = startR;
		startCol = startC;
		endRow = endR;
		endCol = endC;
	}

	public Boolean compute() {
		// System.out.println("The area is " + (endRow-startRow)*((endCol-1)-startCol));
		if ((endRow-startRow)*((endCol-1)-startCol) <= 64) {
			// grid.printGrid();
			// System.out.println("The startRow is " + startRow + " and the endCol is " + endCol);
			// System.out.println("The startCol is " + startCol + " and the endRow is " + endRow); 
			return myGrid.update(startRow, startCol, endRow, endCol);
		} else {	
			// System.out.println("The startRow is " + startRow + " and the endRow is " + endRow);
			// System.out.println("The startCol is " + startCol + " and the endCol is " + endCol);
			MyThread topLeft = new MyThread(myGrid, startRow, startCol, (startRow+endRow)/2, (startCol+endCol)/2);
			MyThread topRight = new MyThread(myGrid, startRow, (startCol+endCol)/2, (startRow+endRow)/2, endCol);
			MyThread bottomLeft = new MyThread(myGrid, (startRow+endRow)/2, startCol, endRow, (startCol+endCol)/2);
			MyThread bottomRight = new MyThread(myGrid, (startRow+endRow)/2, (startCol+endCol)/2, endRow, endCol);

			
			topLeft.fork();
			topRight.fork();
			bottomLeft.fork();

			boolean ans1 = bottomRight.compute();
			boolean ans2 = topLeft.join();
			boolean ans3 = topRight.join();
			boolean ans4 = bottomLeft.join();

			return ans1 | ans2 | ans3 | ans4;
			// return ans2;
		}

	}

}