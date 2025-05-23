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
                // Draw the path tile
                else if (tile.status == TileStatus.PATH)
                    color = Color.YELLOW;
                // Draw tile that has been visited
                else if (tile.status == TileStatus.VISITED)
                    color = Color.DARKGRAY;
                else
                    color = Color.WHITE;

                drawTile(tile, tileSize, color);
            }
        }
    }

    /**
     * Draw a tile on a canvas
     * @param tileModel
     * The tile to draw
     * @param tileSize
     * The size of the tile
     * @param color
     * The color of the tile
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

    /**
     * Draw a wall of a tile
     * @param tile
     * The tile to draw a wall to
     * @param tileSize
     * The size of the tile
     * @param wallDirection
     * The direction of the wall to draw
     */
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

    /**
     * Draw the wall of the hovered tile
     * @param tileSize
     * The size of the tile
     */
    private void drawHoveredWall(double tileSize){
        if (hoveredTile != null){
            // If there is a wall present, color it transparent red
            if (hoveredTile.isWallPresent(hoveredWall)){
                gc.setStroke(Color.color(1, 0, 0, 0.3)); 
            // If there is not, color it transparent black
            } else {
                gc.setStroke(Color.color(0, 0, 0, 0.3));
            }

            gc.setLineWidth(6);
    
            // Draw the hovered wall
            drawWall(hoveredTile, tileSize, hoveredWall);
        }
    }

    /**
     * Get the size of the tile from the canvas
     * @return
     * The tile size
     */
    public double getTileSize(){
        return Math.min(mazeCanvas.getWidth() / maze.numCols, mazeCanvas.getHeight() / maze.numRows);
    }

    /**
     * Get the start tile of the maze
     * @return
     * A TileModel that represent the start tile
     */
    public TileModel getStartTile(){
        return maze.tiles[0][0];
    }

    /**
     * Get the end tile of the maze
     * @return
     * A TileModel that represent the end tile
     */
    public TileModel getEndTile(){
        return maze.tiles[maze.numRows - 1][maze.numCols - 1];
    }

    /**
     * Reset all the tiles to unvisited
     */
    public void resetTileStatus(){
        maze.resetTileStatus();
    }

    /**
     * Check if the row and column given is inside the maze
     * @param row
     * The row index to check
     * @param column
     * The column index to check
     * @return
     * True if inside the maze, False if not
     */
    public boolean isInsideMaze(int row, int column){
        return maze.isInsideMaze(row, column);
    }

    /**
     * Add a wall to a tile
     * @param tile
     * The tile we want to add a wall
     * @param wallDirection
     * The direction of the wall we want to add
     */
    public void addWall(TileModel tile, WallDirection wallDirection){
        maze.addWall(tile, wallDirection);
    }

    /**
     * Remove the wall of a tile
     * @param tile
     * The tile we want to remove a wall
     * @param wallDirection
     * The direction of the wall we want to remove
     */
    public void removeWall(TileModel tile, WallDirection wallDirection){
        maze.removeWall(tile, wallDirection);
    }
}
