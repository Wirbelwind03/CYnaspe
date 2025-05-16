package model;

import java.util.ArrayList;
import java.util.List;

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
}