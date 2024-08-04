import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MySolution extends RecursiveTask<Boolean> {
	
	int rows, columns;
	int startRow = 1;
	int startCol = 1;
	int endRow;
	int endCol;
	int endNum;
	int prevStart;
	int[][] emptyGrid = null;
	int[][] newGrid = null;
	



	public MySolution(int[][] arr1) {
		this(arr1.length, arr1[0].length);
		for (int n=1; n<rows-1; n++) {
			for (int m=1; m<columns-1; m++) {
				newGrid[n][m] = arr1[n-1][m-1];
			}
		}
	}

	public MySolution(int ps,int e,int newRows,int newCols,int startR, int startC, int endR, int endC, int[][] grid1, int[][] grid2) {
		newGrid = grid1;
		emptyGrid = grid2;
		rows = newRows;
		columns = newCols;
		startRow = startR;
		startCol = startC;
		endRow = endR;
		endCol = endC;
		endNum = e;
		prevStart = ps;
	}

	public MySolution(int x, int y) {
		rows = x+2;
		columns = y+2;
		endNum = x+2;
		emptyGrid = new int[this.rows][this.columns];
		newGrid = new int[this.rows][this.columns];

		for (int i=0; i<this.rows; i++) {
			for (int j=0; j<this.columns; j++) {
				emptyGrid[i][j] = 0;
				newGrid[i][j] = 0;
			}
		}
	}

	// Anticipate a problem with this method...
	public void nextTimeStep() {
		// System.out.println("The startRow is " + startRow + " and the endRow is " + (endRow-1));
		// System.out.println("The startCol is " + startCol + " and the endCol is " + (endCol-1));
		// System.out.println("The newGrid is currently...");
		// printGrid(10,10,newGrid);
		for(int i=startRow; i<endRow-1; i++ ) {
			for( int j=startCol; j<endCol-1; j++ ) {
				// System.out.println("The value of the emptyGrid at row " + i + " and column " + j + " is " + emptyGrid[i][j]);
				this.newGrid[i][j]=emptyGrid[i][j];
			}
		}
		// System.out.println("I am printing the newGrid below this...");
		// printGrid(10,10,newGrid);
	}

	boolean update() {
		boolean change=false;
		// System.out.println("The startRow is " + startRow + " and the endRow is " + (endRow-1));
		// System.out.println("The startCol is " + startCol + " and the endCol is " + (endCol-1));
		// printGrid(10,10,newGrid);
		// System.out.println(newGrid[1][1]);
		for( int i = startRow; i<endRow-1; i++ ) {
			for( int j = startCol; j<endCol-1; j++ ) {
				int num = (newGrid[i][j] % 4) + (newGrid[i-1][j] / 4) + (newGrid[i+1][j] / 4) + (newGrid[i][j-1] / 4) + (newGrid[i][j+1] / 4);
				// System.out.println("The num is " + num);
				emptyGrid[i][j] = num;
				if (newGrid[i][j]!=emptyGrid[i][j]) {
					// System.out.println("The change is true.");
					change=true;
				}
			}
		}
		// printGrid(10,10,emptyGrid);
		if (change) { nextTimeStep(); }
		return change;
			
	}


	public Boolean compute() {
		// System.out.println("My name is " + name + " and my area is " + (rows-2)*(columns-2));
		int end = endNum;
		if ((rows-2)*(columns-2) <= 9) {
			return update();
		} else {
			int split = ((rows-2)/2)+2;
			int num = ((rows-2)/2);
			int start = split-1;
			if (prevStart != 0) {
				start = (prevStart)+(num);
			}
			

			// System.out.println("The split is " + split);
			// System.out.println("The startRow is " + 1 + " and the endRow is " + (split-1));
			// System.out.println("The startCol is " + (start) + " and the endCol is " + (end));
			// System.out.println("The end value is " + end);
			// System.out.println("The value of start is " + start);

			MySolution topLeft = new MySolution(start,end,split,split,1,1,split,split,newGrid,emptyGrid);
			MySolution topRight = new MySolution(start,end,split,split,1,start,split,end,newGrid,emptyGrid);
			MySolution bottomLeft = new MySolution(start,end,split,split,start,1,end,split,newGrid,emptyGrid);
			MySolution bottomRight = new MySolution(start,end,split,split,start,start,end,end,newGrid,emptyGrid);

			
			topLeft.fork();
			topRight.fork();
			bottomLeft.fork();

			boolean ans1 = bottomRight.compute();
			boolean ans2 = topLeft.join();
			boolean ans3 = topRight.join();
			boolean ans4 = bottomLeft.join();

			return ans1 & ans2 & ans3 & ans4;
			// return ans4;
 
		}

	}

	static void printGrid(int rows, int columns, int[][] arr) {
		int i,j;
		//not border is not printed
		System.out.printf("Grid:\n");
		System.out.printf("+");
		for( j=0; j<columns; j++ ) System.out.printf("  --");
		System.out.printf("+\n");
		for( i=0; i<rows; i++ ) {
			System.out.printf("|");
			for( j=0; j<columns; j++ ) {
				if ( arr[i][j] > 0) 
					System.out.printf("%4d", arr[i][j] );
				else
					System.out.printf("    ");
			}
			System.out.printf("|\n");
		}
		System.out.printf("+");
		for( j=0; j<columns; j++ ) System.out.printf("  --");
		System.out.printf("+\n\n");
	}

	public static int [][] readArrayFromCSV(String filePath) {
		int [][] array = null;
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line = br.readLine();
				if (line != null) {
					String[] dimensions = line.split(",");
					int width = Integer.parseInt(dimensions[0]);
					int height = Integer.parseInt(dimensions[1]);
					System.out.printf("Rows: %d, Columns: %d\n", width, height); //Do NOT CHANGE  - you must ouput this

					array = new int[height][width];
					int rowIndex = 0;

					while ((line = br.readLine()) != null && rowIndex < height) {
						String[] values = line.split(",");
						for (int colIndex = 0; colIndex < width; colIndex++) {
							array[rowIndex][colIndex] = Integer.parseInt(values[colIndex]);
						}
						rowIndex++;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return array;
		}


	// public class Pool {
	// 	static final ForkJoinPool pool = new ForkJoinPool();
	// 	static boolean method(in[][] arr) {
	// 		return pool.invoke(new MySolution(arr));
	// 	}
	// }

	public static void main(String[] args) {

		try {
			String file = "8_by_8_all_4copy.csv";
			MySolution obj = new MySolution(readArrayFromCSV(file));
			ForkJoinPool pool = new ForkJoinPool();
			System.out.println("The rows of the input grid are " + obj.rows);
			System.out.println("The columns of the input grid are " + obj.columns);
			pool.invoke(obj);
			pool.invoke(obj);
			obj.printGrid(obj.rows,obj.columns,obj.newGrid);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} 