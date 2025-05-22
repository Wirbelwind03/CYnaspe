package algorithms;

import model.TileModel;

import java.util.*;

import controller.MazeController;
import enums.TileStatus;

public class BreadthFirstSolver extends Solver implements ISolverAlgorithm {

    // Queue for the algorithm
    private Queue<TileModel> queue = new LinkedList<>();

    public BreadthFirstSolver(MazeController mazeController) {
        this.mazeController = mazeController;

        TileModel start = mazeController.getStartTile();
        queue.add(start);
        visited.add(start);

        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean step() {
        endTime = System.currentTimeMillis();

        // Trace the path
        if (isFinished && pathStep != null) {
            pathStep.status = TileStatus.PATH;
            pathCount++;
            pathStep = parentMap.get(pathStep);

            return pathStep == null;
        }

        if (!isFinished) {
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

    @Override
    public long getExecutionTime(){
        return isFinished ? (endTime - startTime) : 0;
    }
}
