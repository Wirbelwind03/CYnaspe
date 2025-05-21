package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import controller.MazeController;
import enums.TileStatus;
import model.MazeModel;
import model.TileModel;

public class DjikstraSolver implements ISolverAlgorithm {
    MazeController mazeController;

    // The distance from the start to each tile,
    private Map<TileModel, Integer> distance;
    // The previous tiles with the shortest path
    private Map<TileModel, TileModel> previous;
    // The tiles that have been visited
    private Set<TileModel> visited;
    // Queue used to always expand the closest unvisited tile next
    private PriorityQueue<TileModel> queue;
    private boolean isFinished = false;
    private boolean pathTraced = false;

    public DjikstraSolver(MazeController mazeController) {
        this.mazeController = mazeController;

        distance = new HashMap<>();
        previous = new HashMap<>();
        visited = new HashSet<>();
        queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        isFinished = false;
        pathTraced = false;

        for (int r = 0; r < mazeController.maze.numRows; r++) {
            for (int c = 0; c < mazeController.maze.numCols; c++) {
                TileModel tile = mazeController.maze.tiles[r][c];
                distance.put(tile, Integer.MAX_VALUE);
            }
        }

        TileModel start = mazeController.getStartTile();
        distance.put(start, 0);
        queue.add(start);
    }

    @Override
    public boolean step(){
        if (isFinished) {
            if (!pathTraced) {
                renderPath();
                pathTraced = true;
            }
            return true; // fully done
        }

        if (queue.isEmpty()) {
            isFinished = true;
            return true;
        }

        TileModel current = queue.poll();
        if (visited.contains(current)) return false;

        visited.add(current);
        current.status = TileStatus.VISITED;

        if (current == mazeController.getEndTile()) {
            isFinished = true;
            return false;
        }

        for (TileModel neighbor : mazeController.maze.getAccessibleNeighbors(current)) {
            if (visited.contains(neighbor)) continue;

            int alt = distance.get(current) + 1;
            if (alt < distance.get(neighbor)) {
                distance.put(neighbor, alt);
                previous.put(neighbor, current);
                queue.add(neighbor);
            }
        }

        return false;
    }

    private void renderPath() {
        TileModel step = mazeController.getEndTile();
        while (step != null && previous.containsKey(step)) {
            step.status = TileStatus.PATH;
            step = previous.get(step);
        }

        if (step == mazeController.getStartTile()) {
            mazeController.getStartTile().status = TileStatus.PATH;
        }
    }
}
