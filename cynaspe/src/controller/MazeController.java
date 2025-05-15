package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.MazeModel;
import model.TileModel;

public class MazeController {    
    
    @FXML
    private Canvas mazeCanvas;
    private GraphicsContext gc;

    private MazeModel model;

    @FXML
    public void initialize(){
        gc  = mazeCanvas.getGraphicsContext2D();
        
        model = new MazeModel(5,5);
        
        for(int row = 0; row < model.numRows; row++){
            for (int column = 0; column < model.numCols; column++){
                TileModel tile = new TileModel(row, column);
                drawTile(tile, mazeCanvas.getWidth() / model.numRows, mazeCanvas.getHeight() / model.numCols);
            }
        }
        
    }

    public void drawTile(TileModel tileModel, double rowSize, double colSize){
        long x = Math.round(tileModel.row * rowSize);
        long y = Math.round(tileModel.column * colSize);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, rowSize, colSize);
    }
}
