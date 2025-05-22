package controller;

import enums.TileStatus;
import enums.WallDirection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import model.MazeModel;
import model.TileModel;

public class MazeController extends Controller {
    public MazeModel maze;
    private Canvas mazeCanvas;
    private GraphicsContext gc;
    
    public boolean isGenerating = false;

    public TileModel hoveredTile = null;
    public WallDirection hoveredWall = WallDirection.RIGHT;

    public MazeController(Canvas mazeCanvas) {
        this.mazeCanvas = mazeCanvas;
        this.gc = this.mazeCanvas.getGraphicsContext2D();

        Region cell = (Region) this.mazeCanvas.getParent();

        // Take the entire cell size
        this.mazeCanvas.widthProperty().bind(cell.widthProperty());
        this.mazeCanvas.heightProperty().bind(cell.heightProperty());
    }

    public void setMaze(MazeModel maze){
        this.maze = maze;
        renderMaze(false);
    }

    public boolean hasMaze(){
        return maze != null;
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
        return maze.getTile(row, column);
    }

    /**
     * Render the maze on the canvas
     */
    public void renderMaze(boolean isGenerating){
        // Clear the entire canvas
        double width = mazeCanvas.getWidth();
        double height = mazeCanvas.getHeight();

        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.WHITE); // Background color
        gc.fillRect(0, 0, width, height); // Clear with the background color
        gc.clearRect(0, 0, width, height); // Clear the canvas

        double tileSize = getTileSize();

        for(int row = 0; row < maze.numRows; row++){
            for (int column = 0; column < maze.numCols; column++){
                TileModel tile = maze.tiles[row][column];

                Color color = null;
                // Draw the start tile
                if (row == 0 && column == 0)
                    color = Color.LIGHTGREEN;
                // Draw the end tile
                else if (row == maze.numRows - 1 && column == maze.numCols - 1)
                    color = Color.RED;
                // Draw the other tiles
                else if (!tile.isVisited && isGenerating)
                    color = Color.GREY;
                else if (tile.status == TileStatus.PATH)
                    color = Color.YELLOW;
                else if (tile.status == TileStatus.VISITED)
                    color = Color.DARKGRAY;
                else
                    color = Color.WHITE;

                drawTile(tile, tileSize, color);
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
    public void drawTile(TileModel tileModel, double tileSize, Color color){
        long x = Math.round(tileModel.column * tileSize);
        long y = Math.round(tileModel.row * tileSize);

        if (color != null){
            gc.setFill(color);
            gc.fillRect(x, y, tileSize, tileSize);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        
        for (WallDirection direction : WallDirection.values()){
            if (!tileModel.walls.getOrDefault(direction, false)) continue;

            drawWall(tileModel, tileSize, direction);
        }
    
        drawHoveredWall(tileSize);
    }

    private void drawWall(TileModel tile, double tileSize, WallDirection wallDirection){
        double x = tile.column * tileSize;
        double y = tile.row * tileSize;

        switch (wallDirection) {
            case TOP: gc.strokeLine(x, y, x + tileSize, y); break;
            case RIGHT: gc.strokeLine(x + tileSize, y, x + tileSize, y + tileSize); break;
            case BOTTOM: gc.strokeLine(x, y + tileSize, x + tileSize, y + tileSize); break;
            case LEFT: gc.strokeLine(x, y, x, y + tileSize); break;
        }
    }

    private void drawHoveredWall(double tileSize){
        if (hoveredTile != null){
            if (hoveredTile.isWallPresent(hoveredWall)){
                gc.setStroke(Color.color(1, 0, 0, 0.3)); 
            } else {
                gc.setStroke(Color.color(0, 0, 0, 0.3));
            }

            gc.setLineWidth(6);
    
            drawWall(hoveredTile, tileSize, hoveredWall);
        }
    }

    public double getTileSize(){
        return Math.min(mazeCanvas.getWidth() / maze.numCols, mazeCanvas.getHeight() / maze.numRows);
    }

    public TileModel getStartTile(){
        return maze.tiles[0][0];
    }

    public TileModel getEndTile(){
        return maze.tiles[maze.numRows - 1][maze.numCols - 1];
    }

    /**
     * Reset all the tiles to unvisited
     */
    public void resetTileStatus(){
        maze.resetTileStatus();
    }

    public boolean isInsideMaze(int row, int column){
        return maze.isInsideMaze(row, column);
    }

    public void addWall(TileModel tile, WallDirection wallDirection){
        maze.addWall(tile, wallDirection);
    }

    public void removeWall(TileModel tile, WallDirection wallDirection){
        maze.removeWall(tile, wallDirection);
    }
}
