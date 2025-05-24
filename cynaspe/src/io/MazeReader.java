package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import enums.WallDirection;
import model.MazeModel;
import model.TileModel;

/**
 * Class responsible for reading maze data from a file.
 * <p>
 * Provides a static method to parse a maze file and create a {@code MazeModel}.
 * </p>
 */
public class MazeReader {
    /**
     * Default constructor.
     */
    public MazeReader() {
        
    }

    /**
     * Read a .maze file
     * @param file
     * The file to get the maze model from
     * @return
     * A {@code MazeModel} generated from the file
     */
    public static MazeModel read(File file){
        Path path = file.toPath();
        try {

            List<String> lines = Files.readAllLines(path);
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Maze file is empty");
            }
            // Get the first line, which contains the size of the maze
            String firstLine = lines.get(0);
            // Separate the strings of the first line, by separating the ","
            String[] mazeSizeParts = firstLine.split(",");
            if (mazeSizeParts.length != 2) {
                throw new IllegalArgumentException("First line must contain two integers seperated by ,");
            }
            // Transform the string into integers
            int numRows, numCols;
            try {
                numRows = Integer.parseInt(mazeSizeParts[0].trim());
                numCols = Integer.parseInt(mazeSizeParts[1].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Maze size must be integers", e);
            }

            TileModel[][] tiles = new TileModel[numRows][numCols];
            // For each line, read the written tile
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isEmpty()) continue; // skip empty lines

                // Split the parts of the line
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Line " + (i+1) + " : must have 3 parts: row,col,wallBits");
                }
                
                int row;
                int col;
                try {
                    row = Integer.parseInt(parts[0].trim());
                    col = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Line " + (i+1) + " : Row or column must be integer", e);
                }

                // Check if the row or column are inside the maze
                if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
                    throw new IndexOutOfBoundsException("Line " + (i+1) + " : Row or column are outside the maze");
                }

                // Get the wallBits
                String wallBits = parts[2].trim();
                // Check if the wallBits are in the correct format
                if (wallBits.length() != 4 || !wallBits.matches("[01]{4}")) {
                    throw new IllegalArgumentException("At line " + (i+1) + " : wallBits must be 4 characters of '0' or '1' on line ");
                }
                // Get the wall of the tile
                Map<WallDirection, Boolean> walls = TileModel.getWalls(wallBits);
                tiles[row][col] = new TileModel(row, col, walls);
            }

            return new MazeModel(tiles);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }   
}
