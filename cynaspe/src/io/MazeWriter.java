package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.MazeModel;
import model.TileModel;

public class MazeWriter {
    public static void write(MazeModel maze, File file){
        try (FileWriter writer = new FileWriter(file)){
            // Write the number of rows and columns
            writer.write(String.format("%d,%d\n", maze.numRows, maze.numCols));
            for (TileModel[] row : maze.tiles){
                for (TileModel tile: row){
                    // row,column,wallsBits
                    writer.write(String.format("%d,%d,%s\n", tile.row, tile.column, tile.getWallBits()));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
