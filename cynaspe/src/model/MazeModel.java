package model;

import java.util.ArrayList;
import java.util.List;

import enums.TileStatus;
import enums.WallDirection;
import utils.Helpers;

/**
 * Class that represent a maze
 */
public class MazeModel {
    public int numRows;
    public int numCols;
    public TileModel[][] tiles;

    /**
     * Construct a grid with the size given
     * @param numRows
     * The number of row for the grid
     * @param numCols
     * The number of column for the grid
     */
    public MazeModel(int numRows, int numCols){
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Number of rows and columns must be positive");
        }
        this.numRows = numRows;
        this.numCols = numCols;
        tiles = ConstructGrid();
    }


    /**
     * Construct a maze with a 2d array of TileModel
     * @param tiles
     * The 2D array of tile we want to create the maze from
     */
    public MazeModel(TileModel[][] tiles){
        if (tiles == null || tiles.length == 0) {
            throw new IllegalArgumentException("Tiles cannot be null or empty");
        }
        int cols = tiles[0].length;
        for (TileModel[] row : tiles) {
            if (row == null || row.length != cols) {
                throw new IllegalArgumentException("All rows must be non-null and have the same number of columns");
            }
        }
        this.tiles = tiles;
        this.numRows = tiles.length;
        this.numCols = (tiles.length > 0) ? tiles[0].length : 0; // Check if there are row before getting the number of columns
    }

    /**
     * Construct the grid by putting a TileModel in each row and column
     * @return
     * A 2D array made of TileModel
     */
    private TileModel[][] ConstructGrid(){
        TileModel[][] tiles = new TileModel[numRows][];
        // For each row
        for (int row = 0; row < numRows; row++){
            // Assign columns to the row
            tiles[row] = new TileModel[numCols];
            // For each column
            for (int column = 0; column < numCols; column++){
                // Create the tile
                tiles[row][column] = new TileModel(row, column);
            }
        }
        return tiles;
    }

    /**
     * Reset all the tiles to unvisited
     */
    public void resetTileStatus(){
        for (TileModel[] row : tiles){
            for (TileModel tile: row){
                tile.status = TileStatus.UNVISITED;
            }
        }
    }

    /**
     * Get the edges of the grid
     * @return
     * A list of the edges of the grid
     */
    public List<EdgeModel> getEdges(){
        List<EdgeModel> edges = new ArrayList<>();
        for (int row = 0; row < numRows; row++){
            for (int column = 0; column < numCols; column++){
                if (row > 0) edges.add(new EdgeModel(tiles[row][column], tiles[row -1][column]));
                if (column > 0) edges.add(new EdgeModel(tiles[row][column], tiles[row][column - 1]));
            }
        }
        return edges;
    }

    /**
     * Get a tile from the maze
     * @param row
     * The row we want to get the tile from
     * @param column
     * The column we want to get the tile from
     * @return
     * A TileModel if it was inside the maze
     */
    public TileModel getTile(int row, int column){
        if (!isInsideMaze(row, column)){
            throw new IndexOutOfBoundsException("Invalid tile position: (" + row + ", " + column + ")");
        }
        return tiles[row][column];
    }

    /**
     * Get the neighbor of a tile, with the direction choose
     * @param tile
     * The tile which we want to get the neighbor
     * @param direction
     * The direction to get the neighbor
     * @return
     * A TileModel that represent the neighbor of the tile entered in argument
     * Null otherwise
     */
    public TileModel getNeighbor(TileModel tile, WallDirection direction) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }

        int newRow = tile.row;
        int newCol = tile.column;

        switch (direction) {
            case TOP:    newRow--; break; // Move up
            case BOTTOM: newRow++; break; // Move down
            case LEFT:   newCol--; break; // Move left
            case RIGHT:  newCol++; break; // Move right
        }

        // Check if the new position is inside the maze
        if (isInsideMaze(newRow, newCol)) {
            // Return the neighbor tile
            return tiles[newRow][newCol];
        }
        // Otherwise, return null
        return null;
    }

    /**
     * Check if the row and column are still inside the maze
     * @param row
     * The row we want to check
     * @param column
     * The column we want to check
     * @return
     * A boolean that is True if it's inside the maze, and False if not
     */
    public boolean isInsideMaze(int row, int column){
        if (row >= 0 && row < numRows && column >= 0 && column < numCols) {
            return true;
        }
        return false;
    }

    /**
     * Add a wall to a tile
     * @param tile
     * The tile we want to add a wall
     * @param wallDirection
     * The direction of the wall we want to add
     */
    public void addWall(TileModel tile, WallDirection wallDirection){
        tile.addWall(wallDirection);
        // Add the wall to the neighbor too
        TileModel neighbor = getNeighbor(tile, wallDirection);
        if (neighbor != null){
            WallDirection oppositeWallDirection = Helpers.getOppositeDirection(wallDirection);
            neighbor.addWall(oppositeWallDirection);
        }
    }

    /**
     * Remove the wall of a tile
     * @param tile
     * The tile we want to remove a wall
     * @param wallDirection
     * The direction of the wall we want to remove
     */
    public void removeWall(TileModel tile, WallDirection wallDirection){
        tile.removeWall(wallDirection);
        // Remove the wall of the neighbor too
        TileModel neighbor = getNeighbor(tile, wallDirection);
        if (neighbor != null){
            WallDirection oppositeWallDirection = Helpers.getOppositeDirection(wallDirection);
            neighbor.removeWall(oppositeWallDirection);
        }
    }

    /**
     * Get all the neighbor accessible (aka that doesn't have a wall between them) from the tile entered in argument
     * @param tile
     * The tile we want to get the accessible neighbors
     * @return
     * A List<TileModel> of the accessible neighbors of the tile entered as argument
     */
    public List<TileModel> getAccessibleNeighbors(TileModel tile) {
        List<TileModel> neighbors = new ArrayList<>();
        // Get the position of the argument tile
        int r = tile.row;
        int c = tile.column;

        // Check top
        if (r > 0) { // Check if it's not the top row
            TileModel up = tiles[r - 1][c];
            if (!tile.hasWallWith(up)) neighbors.add(up); // If there isn't a wall at the top, the neighbor is accessible
        }

        // Check bottom
        if (r < numRows - 1) { // Check if it's not the bottom row
            TileModel down = tiles[r + 1][c];
            if (!tile.hasWallWith(down)) neighbors.add(down); // If there isn't a wall at the bottom, the neighbor is accessible
        }

        // Check left
        if (c > 0) { // Check if it's not the left column
            TileModel left = tiles[r][c - 1];
            if (!tile.hasWallWith(left)) neighbors.add(left); // If there isn't a wall at the left, the neighbor is accessible
        }

        // Check right
        if (c < numCols - 1) { // Check if it's not the right column
            TileModel right = tiles[r][c + 1];
            if (!tile.hasWallWith(right)) neighbors.add(right); // If there isn't a wall at the right, the neighbor is accessible
        }

        return neighbors;
    }

    /**
     * Get the start tile of the maze
     * @return
     * A TileModel that represent the start tile
     */
    public TileModel getStartTile(){
        if (numRows == 0 || numCols == 0) {
            throw new IllegalStateException("Maze has no tiles");
        }
        return tiles[0][0];
    }

    /**
     * Get the end tile of the maze
     * @return
     * A TileModel that represent the end tile
     */
    public TileModel getEndTile(){
        if (numRows == 0 || numCols == 0) {
            throw new IllegalStateException("Maze has no tiles");
        }
        return tiles[numRows - 1][numCols - 1];
    }

    public void renderMazeConsole(){
        for (int row = 0; row < numRows; row++) {
            /// Print each wall direction
            /// Example of a tile with all walls
            /// +---+
            /// |   |
            /// +---+

            // Print top wall for each tile in the row
            for (int col = 0; col < numCols; col++) {
                TileModel tile = getTile(row, col);
                System.out.print("+");
                System.out.print(tile.isWallPresent(WallDirection.TOP) ? "---" : "   ");
            }
            System.out.println("+"); // Print the last corner and jump to the next line

            // Print left wall and the content of tile
            for (int col = 0; col < numCols; col++) {
                TileModel tile = getTile(row, col);
                System.out.print(tile.isWallPresent(WallDirection.LEFT) ? "|" : " ");
                if (tile.status == TileStatus.PATH) {
                    System.out.print(" # ");
                } 
                else if (tile.status == TileStatus.VISITED){
                    System.out.print(" o ");
                }
                else {
                    System.out.print("   ");
                }
            }
            // Right wall of the last tile
            TileModel lastTile = getTile(row, numCols - 1);
            System.out.println(lastTile.isWallPresent(WallDirection.RIGHT) ? "|" : " ");

            // Print bottom walls at the end
            if (row == numRows - 1) {
                for (int col = 0; col < numCols; col++) {
                    TileModel tile = getTile(row, col);
                    System.out.print("+");
                    System.out.print(tile.isWallPresent(WallDirection.BOTTOM) ? "---" : "   ");
                }
                System.out.println("+");
            }
        }
    }
}