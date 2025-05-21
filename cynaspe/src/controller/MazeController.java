package controller;

import enums.GenerationMode;
import enums.TileStatus;
import enums.WallDirection;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import model.MazeModel;
import model.TileModel;
import utils.KruskalMazeGenerator;

public class MazeController {
    private MazeModel maze;
    private Canvas mazeCanvas;
    private GraphicsContext gc;

    public MazeController(Canvas mazeCanvas) {
        this.mazeCanvas = mazeCanvas;
        this.gc = this.mazeCanvas.getGraphicsContext2D();

        Region cell = (Region) this.mazeCanvas.getParent();

        // Take the entire cell size
        this.mazeCanvas.widthProperty().bind(cell.widthProperty());
        this.mazeCanvas.heightProperty().bind(cell.heightProperty());
    }

    /**
     * Create the maze 
     */
    public void constructMaze(MazeConfigurationController mazeConfigurationController){
        maze = new MazeModel(mazeConfigurationController.getMazeNumRows(), mazeConfigurationController.getMazeNumColumns());
        // Use Kruskal algorithm to generate the maze
        KruskalMazeGenerator generator = new KruskalMazeGenerator(maze, mazeConfigurationController, mazeConfigurationController.getMazeType());

        // Show the maze generation depending on the mode
        switch (mazeConfigurationController.getGenerationMode()) {
            case GenerationMode.COMPLETE:
                // instant
                while (!generator.isComplete()){
                    generator.step();
                }
                renderMaze();
                break;

            case GenerationMode.STEP:
                /// step by step
                AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {
                        // Update every 10 fps
                        if (now - lastUpdate >= 100_000_000) {
                            lastUpdate = now;

                            if (!generator.isComplete()) {
                                generator.step();
                                renderMaze();
                            } else {
                                stop();
                            }
                        }
                    }
                };

                timer.start();
                break;
        
            
            default:
                break;
        }
    }

    /**
     * Render the maze on the canvas
     */
    public void renderMaze(){
        // Clear the entire canvas
        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        double tileSize = Math.min(mazeCanvas.getWidth() / maze.numCols, mazeCanvas.getHeight() / maze.numRows);

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
                else if (!tile.isVisited)
                    color = Color.GREY;
                else if (tile.status == TileStatus.PATH)
                    color = Color.YELLOW;
                else if (tile.status == TileStatus.VISITED)
                    color = Color.DARKGRAY;
                else
                    color = Color.WHITE;

                drawTile(tile, tileSize, tileSize, color);
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
        gc.setLineWidth(2);
        
        // Draw the top wall
        if (tileModel.walls.getOrDefault(WallDirection.TOP, false)) {
            gc.strokeLine(x, y, x + colSize, y);
        }

        // Draw the right wall
        if (tileModel.walls.getOrDefault(WallDirection.RIGHT, false)) {
            gc.strokeLine(x + colSize, y, x + colSize, y + rowSize);
        }

        // Draw the bottom wall
        if (tileModel.walls.getOrDefault(WallDirection.BOTTOM, false)) {
            gc.strokeLine(x, y + rowSize, x + colSize, y + rowSize);
        }

        //Draw the left wall
        if (tileModel.walls.getOrDefault(WallDirection.LEFT, false)) {
            gc.strokeLine(x, y, x, y + rowSize);
        }
    }

    public TileModel getStartTile(){
        return maze.tiles[0][0];
    }

    public TileModel getEndTile(){
        return maze.tiles[maze.numRows - 1][maze.numCols - 1];
    }
}
