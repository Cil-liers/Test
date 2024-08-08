import java.util.concurrent.ForkJoinPool;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {


	static long timeBefore;
	static long timeAfter;

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

    private static void tick() {
    	timeBefore = System.currentTimeMillis();
    }

    private static void tock() {
    	timeAfter = System.currentTimeMillis();
    }

	public static void main(String[] args) {

		String file = "65_by_65_all_4.csv";
		Grid grid = new Grid(readArrayFromCSV(file));
		ForkJoinPool pool = new ForkJoinPool();
		tick();
		while(pool.invoke(new MyThread(grid))) {
			grid.nextTimeStep();
		}
		tock();
		System.out.println("The time taken was " + (timeAfter-timeBefore) + " ms");
		try {
			grid.gridToImage("test3.png");
		} catch (IOException e) {
			System.exit(0);
		}
		
		
	}
}