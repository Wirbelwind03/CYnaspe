package model;

import java.util.ArrayList;
import java.util.List;

import enums.WallDirection;
import utils.Helpers;

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
        this.numRows = numRows;
        this.numCols = numCols;
        tiles = ConstructGrid();
    }

    /**
     * Construct the grid by putting a TileModel in each row and column
     * @return
     * A 2D array made of TileModel
     */
    private TileModel[][] ConstructGrid(){
        TileModel[][] tiles = new TileModel[numRows][];
        for (int row = 0; row < numRows; row++){
            tiles[row] = new TileModel[numCols];
            for (int column = 0; column < numCols; column++){
                tiles[row][column] = new TileModel(row, column);
            }
        }
        return tiles;
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

    public TileModel getTile(int row, int column){
        return tiles[row][column];
    }

    /**
     * Get the neighbor of a tile, with the direction choose
     * @param tile
     * The tile which we want to get the neighbor
     * @param direction
     * The direction the neighbor is going to be get
     * @return
     * The neighbor tile
     */
    public TileModel getNeighbor(TileModel tile, WallDirection direction) {
        int newRow = tile.row;
        int newCol = tile.column;

        switch (direction) {
            case TOP:    newRow--; break;
            case BOTTOM: newRow++; break;
            case LEFT:   newCol--; break;
            case RIGHT:  newCol++; break;
        }

        //
        if (isInsideMaze(newRow, newCol)) {
            return tiles[newRow][newCol];
        }
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
}