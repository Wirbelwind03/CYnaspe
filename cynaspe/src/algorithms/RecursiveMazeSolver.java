package algorithms;

import controller.MazeController;
import enums.TileStatus;
import model.MazeModel;
import model.TileModel;

public class RecursiveMazeSolver {

    private MazeController mazeController;

    public RecursiveMazeSolver(MazeController mazeController) {
        this.mazeController = mazeController;
    }

    public boolean solve() {
        TileModel start = mazeController.getStartTile();
        TileModel end = mazeController.getEndTile();
        return explore(start, end);
    }

    private boolean explore(TileModel current, TileModel end) {
        if (current == null || current.isVisited) return false;

        current.isVisited = true;

        if (current.equals(end)) {
            current.status = TileStatus.PATH;
            return true;
        }

        for (TileModel voisin : mazeController.maze.getAccessibleNeighbors(current)) {
            if (explore(voisin, end)) {
                current.status = TileStatus.PATH;
                return true;
            }
        }

        return false;
    }
}
