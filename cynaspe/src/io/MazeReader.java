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

public class MazeReader {
    public static MazeModel read(File file){
        Path path = file.toPath();
        try {

            List<String> lines = Files.readAllLines(path);
            // Get the first line, which contains the size of the maze
            String firstLine = lines.get(0);
            // Separate the strings of the first line, by separating the ","
            String[] mazeSizeParts = firstLine.split(",");
            // Transform the string into integers
            int numRows = Integer.parseInt(mazeSizeParts[0]);
            int numCols = Integer.parseInt(mazeSizeParts[1]);

            TileModel[][] tiles = new TileModel[numRows][numCols];
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                String wallBits = parts[2];
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
