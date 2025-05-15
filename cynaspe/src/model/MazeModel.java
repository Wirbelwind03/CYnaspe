package model;

public class MazeModel {
    public int numRows;
    public int numCols;
    public TileModel[][] tiles;

    public MazeModel(int numRows, int numCols){
        this.numRows = numRows;
        this.numCols = numCols;

        tiles = ConstructGrid();
    }

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
}