package solver;

import model.MazeModel;
import model.TileModel;

import java.util.*;

public class BreadthFirstSolver {

    private MazeModel maze;
    private Map<TileModel, TileModel> parentMap = new HashMap<>();

    public BreadthFirstSolver(MazeModel maze) {
        this.maze = maze;
    }

    public boolean solve() {
        TileModel start = maze.getStartTile();
        TileModel end = maze.getEndTile();

        Queue<TileModel> file = new LinkedList<>();
        Set<TileModel> visited = new HashSet<>();

        file.add(start);
        visited.add(start);

        while (!file.isEmpty()) {
            TileModel current = file.poll();

            if (current.equals(end)) {
                marquerChemin(end);
                return true;
            }

            for (TileModel voisin : maze.getVoisinsAccessibles(current)) {
                if (!visited.contains(voisin)) {
                    visited.add(voisin);
                    file.add(voisin);
                    parentMap.put(voisin, current);
                }
            }
        }

        return false; // pas de solution
    }

    private void marquerChemin(TileModel end) {
        TileModel current = end;
        while (current != null) {
            current.setAsPath();
            current = parentMap.get(current);
        }
    }
}
