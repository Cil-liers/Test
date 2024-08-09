/*
	Author: Benjamin Cilliers
	Date Created: 05/08/2024
*/

import java.util.concurrent.ForkJoinPool;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {


	static long timeBefore;
	static long timeAfter;


	// Method that reads the input csv file and converts it into a 2-dimensional array
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

	// Method recording the time before computation
	private static void tick() {
		timeBefore = System.currentTimeMillis();
	}

	// Method recording the time after the computation
	private static void tock() {
		timeAfter = System.currentTimeMillis();
	}


	// Main method
	public static void main(String[] args) {

		String file = "517_by_517_centre_534578.csv";
		Grid grid = new Grid(readArrayFromCSV(file));
		ForkJoinPool pool = new ForkJoinPool(); // Creation of ForkJoinPool
		tick();
		while(pool.invoke(new MyThread(grid))) { // Invocation of the pool and the creation of MyThread object
			grid.nextTimeStep();
		}
		tock();
		System.out.println("The time taken was " + (timeAfter-timeBefore) + " ms");
		try {
			grid.gridToImage("test3.png"); // Converting the grid to an image and calling the given file name
		} catch (IOException e) {
			System.exit(0);
		}
		
		
	}
}