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

	public MySolution(int newRows, int newCols, int startR, int startC, int endR, int endC, int[][] grid1, int[][] grid2) {
		newGrid = grid1;
		emptyGrid = grid2;
		rows = newRows;
		columns = newCols;
		startRow = startR;
		startCol = startC;
		endRow = endR;
		endCol = endC;

	}

	public MySolution(int x, int y) {
		rows = x+2;
		columns = y+2;
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
        for(int i=1; i<rows-1; i++ ) {
            for( int j=1; j<columns-1; j++ ) {
                this.newGrid[i][j]=emptyGrid[i][j];
            }
        }
    }

    boolean update() {
        boolean change=false;
        for( int i = startRow; i<endRow-1; i++ ) {
            for( int j = startCol; j<endCol-1; j++ ) {
                int num = (newGrid[i][j] % 4) + (newGrid[i-1][j] / 4) + (newGrid[i+1][j] / 4) + (newGrid[i][j-1] / 4) + (newGrid[i][j+1] / 4);
                emptyGrid[i][j] = num;
                if (newGrid[i][j]!=emptyGrid[i][j]) {
                    change=true;
                }
            }
        }
        if (change) { nextTimeStep(); }
        return change;
            
    }


	public Boolean compute() {
		if ((rows-2)*(columns-2) <= 9) {
			return update();
		} else {
			int newRAndC = ((rows-2)/2);
			int split = ((rows-2)/2)+2;
			MySolution topLeft = new MySolution(newRAndC,newRAndC,1,1,split,split,newGrid,emptyGrid);
			MySolution topRight = new MySolution(newRAndC,newRAndC,1,split-1,split,columns,newGrid,emptyGrid);
			MySolution bottomLeft = new MySolution(newRAndC,newRAndC,split-1,1,rows,split,newGrid,emptyGrid);
			MySolution bottomRight = new MySolution(newRAndC,newRAndC,split-1,split-1,rows,columns,newGrid,emptyGrid);

			
			topLeft.fork();
			topRight.fork();
			bottomLeft.fork();

			boolean ans1 = bottomRight.compute();
			boolean ans2 = topLeft.join();
			boolean ans3 = topRight.join();
			boolean ans4 = bottomLeft.join();

			// topLeft.fork();
			// topRight.fork();
			// boolean ans1 = bottomLeft.compute();
			// boolean ans2 = topLeft.join();
			// boolean ans3 = topRight.join();
			return ans1 & ans2 & ans3 & ans4;
			// return ans1 & ans2 & ans3;
 
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

	public static void main(String[] args) {

		try {
			String file = "8_by_8_all_4copy.csv";
			MySolution obj = new MySolution(readArrayFromCSV(file));
			ForkJoinPool pool = new ForkJoinPool();
			// System.out.println("The number of rows is " + obj.rows);
			// int split = ((obj.rows-2)/2)+2;
			// System.out.println("The split is " + split);
			// MySolution bottomRight = new MySolution(obj.rows,obj.columns,split-1,split-1,obj.newGrid,obj.emptyGrid);
			// System.out.println("This is the start...");
			// for( int i = bottomRight.startRow; i<bottomRight.rows-1; i++ ) {
            // 	for( int j = bottomRight.startCol; j<bottomRight.columns-1; j++ ) {
            //     	System.out.print(bottomRight.newGrid[i][i]);
            //     }
            // 	System.out.println();
            // }
            System.out.println("The rows of the input grid are " + obj.rows);
            System.out.println("The columns of the input grid are " + obj.columns);
			pool.invoke(obj);
			// obj.printGrid(obj.rows,obj.columns,obj.newGrid);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} 