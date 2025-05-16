package controller;

import enums.WallDirection;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.MazeModel;
import model.TileModel;
import utils.Kruskal;

public class MazeController {    
    
    @FXML
    private Canvas mazeCanvas;
    private GraphicsContext gc;

    private MazeModel maze;

    @FXML
    public void initialize(){
        gc  = mazeCanvas.getGraphicsContext2D();
        
        maze = new MazeModel(10,10);
        new Kruskal(maze);

        for(int row = 0; row < maze.numRows; row++){
            for (int column = 0; column < maze.numCols; column++){
                TileModel tile = maze.tiles[row][column];

                if (row == 0 && column == 0)
                    drawTile(tile, mazeCanvas.getWidth() / maze.numRows, mazeCanvas.getHeight() / maze.numCols, Color.LIGHTGREEN);
                else if (row == maze.numRows - 1 && column == maze.numCols - 1)
                    drawTile(tile, mazeCanvas.getWidth() / maze.numRows, mazeCanvas.getHeight() / maze.numCols, Color.RED);
                else
                    drawTile(tile, mazeCanvas.getWidth() / maze.numRows, mazeCanvas.getHeight() / maze.numCols, Color.WHITE);
            }
        }
        
    }

    public void drawTile(TileModel tileModel, double rowSize, double colSize, Color color){
        long x = Math.round(tileModel.column * colSize);
        long y = Math.round(tileModel.row * rowSize);

        if (color != null){
            gc.setFill(color);
            gc.fillRect(x, y, colSize, rowSize);
        }

        gc.setStroke(Color.BLACK);

        if (tileModel.walls.getOrDefault(WallDirection.TOP, false)) {
            gc.strokeLine(x, y, x + colSize, y);
        }

        if (tileModel.walls.getOrDefault(WallDirection.RIGHT, false)) {
            gc.strokeLine(x + colSize, y, x + colSize, y + rowSize);
        }

        if (tileModel.walls.getOrDefault(WallDirection.BOTTOM, false)) {
            gc.strokeLine(x, y + rowSize, x + colSize, y + rowSize);
        }

        if (tileModel.walls.getOrDefault(WallDirection.LEFT, false)) {
            gc.strokeLine(x, y, x, y + rowSize);
        }
    }

}
