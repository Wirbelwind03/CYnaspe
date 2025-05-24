package controller;

import enums.WallDirection;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.TileModel;

public class MazeCanvasController extends Controller {
    private final MazeController mazeController;
    private final Canvas canvas;

    public MazeCanvasController(Canvas canvas, MazeController mazeController) {
        this.canvas = canvas;
        this.mazeController = mazeController;
        setup();
    }

    private void setup(){
        canvas.setFocusTraversable(true);
        canvas.setOnMouseEntered(e -> canvas.requestFocus());
        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setOnMouseMoved(this::onMouseMoved);
        canvas.setOnMouseExited(e -> mazeController.hoveredTile = null);
        canvas.setOnKeyPressed(this::onKeyPressed);
    }

    /**
     * When the user mouse click on the MazeCanvas
     * @param event
     * The mouse that has clicked
     */
    private void onMouseClicked(MouseEvent event) {
        if (mazeController.isGenerating || !mazeController.hasMaze() || mazeController.hoveredTile == null) return;
        
        // Add a wall with the direction the user want
        mazeController.addWall(mazeController.hoveredTile, mazeController.hoveredWall);

        // Render the maze with the new added wall
        mazeController.renderMaze(false);
    }

    /**
     * When the user mouse move on the canvas
     * @param event
     * The mouse that has entered the canvas
     */
    private void onMouseMoved(MouseEvent event) {
        if (mazeController.isGenerating || !mazeController.hasMaze()) return;

        canvas.requestFocus();
    
        // Get the position of the mouse
        double x = event.getX();
        double y = event.getY();
    
        // Calculate the tileSize
        double tileSize = mazeController.getTileSize();
    
        // Get the row and column the mouse is in
        int column = (int)(x / tileSize);
        int row = (int)(y / tileSize);
    
        // Assign the hovered tile by getting the tile where the mouse is
        if (mazeController.isInsideMaze(row, column)) {
            TileModel newHovered = mazeController.getTile(row, column);
            // If the hovered tile is a new one
            if (mazeController.hoveredTile == null ||
                newHovered.row != mazeController.hoveredTile.row ||
                newHovered.column != mazeController.hoveredTile.column) {
                // Assign it and render the maze, so the hovered tile is shown
                mazeController.hoveredTile = newHovered;
                mazeController.renderMaze(false);
            }
        }
    }

    /**
     * When the user press a key on the canvas
     * @param event
     * The key that has been pressed
     */
    private void onKeyPressed(KeyEvent event) {
        if (mazeController.isGenerating || !mazeController.hasMaze() || mazeController.hoveredTile == null) return;

        switch (event.getCode()) {
            // ZQSD keys
            case Z:
                // If the user is at the top border
                if (mazeController.hoveredTile.row == 0)
                    return;
                mazeController.hoveredWall = WallDirection.TOP;
                break;
            case D:
                // If the user is at the right border
                if (mazeController.hoveredTile.column == mazeController.maze.numCols - 1)
                    return;
                mazeController.hoveredWall = WallDirection.RIGHT;
                break;
            case S:
                // If the user is at the bottom border
                if (mazeController.hoveredTile.row == mazeController.maze.numRows - 1)
                    return;
                mazeController.hoveredWall = WallDirection.BOTTOM;
                break;
            case Q:
                // If the user is at the left border
                if (mazeController.hoveredTile.column == 0)
                    return;
                mazeController.hoveredWall = WallDirection.LEFT;
                break;
            // Del key
            case DELETE:
                if (mazeController.hoveredWall != null) {
                    mazeController.removeWall(mazeController.hoveredTile, mazeController.hoveredWall);
                }
                break;
            default:
                return;
        }

        // Render the wall that is going to be added
        mazeController.renderMaze(false);
    }
}