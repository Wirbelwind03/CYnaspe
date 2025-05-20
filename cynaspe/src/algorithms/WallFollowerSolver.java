package algorithms;

import controller.MazeController;
import enums.TileStatus;

import model.TileModel;

public class WallFollowerSolver implements ISolverAlgorithm {

    private MazeController mazeController;
    private TileModel current;
    private int direction;

    public WallFollowerSolver(MazeController mazeController) {
        this.mazeController = mazeController;
        this.current = mazeController.getStartTile();
        this.direction = 1; // Start at the left
    }

    public void solve() {
        TileModel end = mazeController.getEndTile();

        while (!current.equals(end)) {
            current.status = TileStatus.PATH;

            // Turn right
            int right = (direction + 1) % 4;
            TileModel rightNeighbor = getNeighbor(current, right);
            if (rightNeighbor != null && !current.hasWallWith(rightNeighbor)) {
                current = rightNeighbor;
                direction = right;
                continue;
            }

            // Go forward
            TileModel forwardNeighbor = getNeighbor(current, direction);
            if (forwardNeighbor != null && !current.hasWallWith(forwardNeighbor)) {
                current = forwardNeighbor;
                continue;
            }

            // Turn left
            int left = (direction + 3) % 4;
            TileModel leftNeighbor = getNeighbor(current, left);
            if (leftNeighbor != null && !current.hasWallWith(leftNeighbor)) {
                current = leftNeighbor;
                direction = left;
                continue;
            }

            // Turn around
            direction = (direction + 2) % 4;
        }

        // Mark last tile too
        end.status = TileStatus.PATH;
    }

    private TileModel getNeighbor(TileModel tile, int dir) {
        int row = tile.row;
        int col = tile.column;

        switch (dir) {
            case 0: return (row > 0) ? mazeController.maze.tiles[row - 1][col] : null; // Up
            case 1: return (col < mazeController.maze.numCols - 1) ? mazeController.maze.tiles[row][col + 1] : null; // Right
            case 2: return (row < mazeController.maze.numRows - 1) ? mazeController.maze.tiles[row + 1][col] : null; // Down
            case 3: return (col > 0) ? mazeController.maze.tiles[row][col - 1] : null; // Left
            default: return null;
        }
    }
}

