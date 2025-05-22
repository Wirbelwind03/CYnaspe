package solver;

import model.MazeModel;
import model.TileModel;

public class RecursiveMazeSolver {

    private MazeModel maze;
    private boolean solutionTrouvee = false;

    public RecursiveMazeSolver(MazeModel maze) {
        this.maze = maze;
    }

    public boolean solve() {
        TileModel start = maze.getStartTile();
        TileModel end = maze.getEndTile();
        return explore(start, end);
    }

    private boolean explore(TileModel current, TileModel end) {
        if (current == null || current.isVisited) return false;

        current.isVisited = true;

        if (current.equals(end)) {
            current.setAsPath();
            solutionTrouvee = true;
            return true;
        }

        for (TileModel voisin : maze.getVoisinsAccessibles(current)) {
            if (explore(voisin, end)) {
                current.setAsPath(); // marquer le chemin au retour
                return true;
            }
        }

        return false;
    }
}
