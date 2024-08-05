//Copyright M.M.Kuttel 2024 CSC2002S, UCT
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

//This class is for the grid for the Abelian Sandpile cellular automaton
public class GridDuplicate {
    private int rows, columns;
    private int [][] grid; //grid 
    private int [][] updateGrid;//grid for next time step
    
    public GridDuplicate(int w, int h) {
        rows = w+2; //for the "sink" border
        columns = h+2; //for the "sink" border
        grid = new int[this.rows][this.columns];
        updateGrid = new int[this.rows][this.columns];
        /* grid  initialization */
        for(int i=0; i<this.rows; i++ ) {
            for( int j=0; j<this.columns; j++ ) {
                grid[i][j]=0;
                updateGrid[i][j]=0;
            }
        }
    }


    public GridDuplicate(int[][] newGrid) {
        this(newGrid.length,newGrid[0].length); //call constructor above
        
        //don't copy over sink border
        for(int i=1; i<rows-1; i++ ) {
            for( int j=1; j<columns-1; j++ ) {
                this.grid[i][j]=newGrid[i-1][j-1];
            }
        }
    }

    // We have the empty grid initialised and we have the given grid put into the grid variable.

    public GridDuplicate(GridDuplicate copyGrid) {
        this(copyGrid.rows,copyGrid.columns); //call constructor above
        /* grid  initialization */
        for(int i=0; i<rows; i++ ) {
            for( int j=0; j<columns; j++ ) {
                this.grid[i][j]=copyGrid.get(i,j);
            }
        }
    }
    
    public int getRows() {
        return rows-2; //less the sink
    }

    public int getColumns() {
        return columns-2;//less the sink
    }


    int get(int i, int j) {
        return this.grid[i][j];
    }

    void setAll(int value) {
        //borders are always 0
        for( int i = 1; i<rows-1; i++ ) {
            for( int j = 1; j<columns-1; j++ )          
                grid[i][j]=value;
            }
    }
    

    //for the next timestep - copy updateGrid into grid
    public void nextTimeStep(int rs, int cs, int re, int ce) {
        for(int i=rs; i<re; i++ ) {
            for( int j=cs;  j<ce; j++ ) {
                this.grid[i][j]=updateGrid[i][j];
            }
        }
    }
    
    
    boolean update(int rowStart, int colStart, int rowEnd, int colEnd) {
        // System.out.println("We are now in the update function...");
        boolean change=false;
        // System.out.println("The value of rowStart is " + rowStart + " and the value of rowEnd is " + rowEnd);
        // System.out.println("The value of colStart is " + colStart + " and the value of colEnd is " + colEnd);
        for( int i = rowStart; i<rowEnd; i++ ) {
            for( int j = colStart; j<colEnd; j++ ) {
                int num = (grid[i][j] % 4) + (grid[i-1][j] / 4) + (grid[i+1][j] / 4) + (grid[i][j-1] / 4) + (grid[i][j+1] / 4);
                // System.out.println("The row is " + i + " and the column and the column is " + j);
                // System.out.println("The num is " + num);
                updateGrid[i][j] = num;
                if (grid[i][j]!=updateGrid[i][j]) {
                    // System.out.println("The change is true...");
                    change=true;
                }
            }
        }
        // System.out.println("We are now printing the grid below...");
        // printGrid();
        if (change) { nextTimeStep(rowStart, colStart, rowEnd, colEnd); }
        // printGrid();
        return change;
            
    }
    
    
    
    //display the grid in text format
    void printGrid( ) {
        int i,j;
        //not border is not printed
        System.out.printf("Grid:\n");
        System.out.printf("+");
        for( j=1; j<columns-1; j++ ) System.out.printf("  --");
        System.out.printf("+\n");
        for( i=1; i<rows-1; i++ ) {
            System.out.printf("|");
            for( j=1; j<columns-1; j++ ) {
                if ( grid[i][j] > 0) 
                    System.out.printf("%4d", grid[i][j] );
                else
                    System.out.printf("    ");
            }
            System.out.printf("|\n");
        }
        System.out.printf("+");
        for( j=1; j<columns-1; j++ ) System.out.printf("  --");
        System.out.printf("+\n\n");
    }
    
    //write grid out as an image
    void gridToImage(String fileName) throws IOException {
        BufferedImage dstImage =
                new BufferedImage(rows, columns, BufferedImage.TYPE_INT_ARGB);
        //integer values from 0 to 255.
        int a=0;
        int g=0;//green
        int b=0;//blue
        int r=0;//red

        for( int i=0; i<rows; i++ ) {
            for( int j=0; j<columns; j++ ) {
                g=0;//green
                b=0;//blue
                r=0;//red

                switch (grid[i][j]) {
                    case 0:
                        break;
                    case 1:
                        g=255;
                        break;
                    case 2:
                        b=255;
                        break;
                    case 3:
                        r = 255;
                        break;
                    default:
                        break;
                
                }
                        // Set destination pixel to mean
                        // Re-assemble destination pixel.
                      int dpixel = (0xff000000)
                                | (a << 24)
                                | (r << 16)
                                | (g<< 8)
                                | b; 
                      dstImage.setRGB(i, j, dpixel); //write it out
            }
        }
        
        File dstFile = new File(fileName);
        ImageIO.write(dstImage, "png", dstFile);
    }
}