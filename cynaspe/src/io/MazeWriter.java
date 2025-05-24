package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.MazeModel;
import model.TileModel;

/**
 * Class responsible for writing maze data to a file.
 * <p>
 * Provides a static method to write a {@code MazeModel} into a file.
 * </p>
 */
public class MazeWriter {

    /**
     * Write the given maze model to a file.
     * @param maze
     * The maze to write in the file
     * @param file
     * The file where we want to write the file
     */
    public static void write(MazeModel maze, File file){
        try (FileWriter writer = new FileWriter(file)){
            // Write the number of rows and columns
            writer.write(String.format("%d,%d\n", maze.numRows, maze.numCols));
            for (TileModel[] row : maze.tiles){
                for (TileModel tile: row){
                    // row,column,wallsBits
                    // Example : row = 0, column = 1, walls = TOP, RIGHT
                    // = 0,1,1100
                    writer.write(String.format("%d,%d,%s\n", tile.row, tile.column, tile.getWallBits()));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
