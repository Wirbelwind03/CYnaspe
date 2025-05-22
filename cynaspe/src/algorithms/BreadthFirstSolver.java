package algorithms;

import model.MazeModel;
import model.TileModel;

import java.util.*;

import controller.MazeController;
import enums.TileStatus;

public class BreadthFirstSolver implements ISolverAlgorithm {

    private MazeController mazeController;
    private Map<TileModel, TileModel> parentMap = new HashMap<>();
    private Queue<TileModel> queue = new LinkedList<>();
    private Set<TileModel> visited = new HashSet<>();

    private boolean isFinished = false;
    private TileModel pathStep = null;
    private int pathCount = 0;

    public BreadthFirstSolver(MazeController mazeController) {
        this.mazeController = mazeController;
    }

    @Override
    public boolean step() {
        // Trace the path
        if (isFinished && pathStep != null) {
            pathStep.status = TileStatus.PATH;
            pathCount++;
            pathStep = parentMap.get(pathStep);

            return pathStep == null;
        }

        if (!isFinished) {
            if (queue.isEmpty()) {
                TileModel start = mazeController.getStartTile();
                queue.add(start);
                visited.add(start);
            }

            if (!queue.isEmpty()) {
                TileModel current = queue.poll();

                if (current.equals(mazeController.getEndTile())) {
                    isFinished = true;
                    pathStep = current;
                    return false;
                }

                for (TileModel neighbor : mazeController.maze.getAccessibleNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                        parentMap.put(neighbor, current);
                        neighbor.status = TileStatus.VISITED;
                    }
                }

                return false;
            }

            // No path found
            isFinished = true;
            return true;
        }
        return true;
    }

    @Override
    public int getVisitedCount() {
        return visited.size();
    }

    @Override
    public int getPathCount() {
        return pathCount;
    }

    @Override
    public boolean isComplete() {
        return isFinished && pathStep == null;
    }
}
