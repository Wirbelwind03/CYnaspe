package controller;

import enums.WallDirection;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.GridModel;
import model.MazeModel;
import model.TileModel;
import utils.Kruskal;

/**
 * Gere les données du labyrinte avec l'interface qui affice le labyrinthe
 */
public class MazeController {    
    
    @FXML
    private Canvas mazeCanvas;
    private GraphicsContext gc;

    private MazeModel maze;

    @FXML
    public void initialize(){
        gc  = mazeCanvas.getGraphicsContext2D();
        
        GridModel grid = new GridModel(10,10);
        Kruskal.generateMaze(grid);
        maze = new MazeModel(grid);

        for(int row = 0; row < maze.grid.numRows; row++){
            for (int column = 0; column < maze.grid.numCols; column++){
                TileModel tile = maze.grid.tiles[row][column];

                if (row == 0 && column == 0)
                    drawTile(tile, mazeCanvas.getWidth() / maze.grid.numRows, mazeCanvas.getHeight() / maze.grid.numCols, Color.LIGHTGREEN);
                else if (row == maze.grid.numRows - 1 && column == maze.grid.numCols - 1)
                    drawTile(tile, mazeCanvas.getWidth() / maze.grid.numRows, mazeCanvas.getHeight() / maze.grid.numCols, Color.RED);
                else
                    drawTile(tile, mazeCanvas.getWidth() / maze.grid.numRows, mazeCanvas.getHeight() / maze.grid.numCols, Color.WHITE);
            }
        }
        
    }

    /**
     * Dessine une case sur un canvas
     * @param tileModel
     * La case à dessiner
     * @param rowSize
     * La taille des lignes pour les cases
     * @param colSize
     * La taille des colonnes pour les cases
     * @param color
     * La couleur de la case
     */
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
