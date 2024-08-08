import java.util.concurrent.RecursiveTask;


public class MyThread extends RecursiveTask<Boolean> {

	int startRow;
	int startCol;
	int endRow;
	int endCol;
	Grid myGrid;

	public MyThread(Grid grd) {
		myGrid = grd;
		startRow = 1;
		startCol = 1;
		endRow = grd.getRows()+1;
		endCol = grd.getColumns()+1;
	}

	public MyThread(Grid grd, int startR, int startC, int endR, int endC) {
		myGrid = grd;
		startRow = startR;
		startCol = startC;
		endRow = endR;
		endCol = endC;
	}

	public Boolean compute() {
		if ((endRow-startRow)*((endCol-1)-startCol) <= 200) {
			return myGrid.update(startRow, startCol, endRow, endCol);
		} else {
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
		}

	}

}