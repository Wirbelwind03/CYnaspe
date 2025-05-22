package algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import controller.MazeController;
import enums.TileStatus;
import model.TileModel;

public class DjikstraSolver extends Solver implements ISolverAlgorithm {
    // The distance from the start to each tile,
    private Map<TileModel, Integer> distance;
    // Queue used to always expand the closest unvisited tile next
    private PriorityQueue<TileModel> queue;

    private boolean pathTracing = false;

    public DjikstraSolver(MazeController mazeController) {
        this.mazeController = mazeController;

        distance = new HashMap<>();
        parentMap = new HashMap<>();
        visited = new HashSet<>();
        queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        for (int r = 0; r < mazeController.maze.numRows; r++) {
            for (int c = 0; c < mazeController.maze.numCols; c++) {
                TileModel tile = mazeController.maze.tiles[r][c];
                distance.put(tile, Integer.MAX_VALUE);
            }
        }

        TileModel start = mazeController.getStartTile();
        distance.put(start, 0);
        queue.add(start);

        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean step(){
        endTime = System.currentTimeMillis();

        if (!isFinished) {
            if (!queue.isEmpty()) {
                TileModel current = queue.poll();

                if (visited.contains(current)) return false;
    
                visited.add(current);
                current.status = TileStatus.VISITED;
    
                if (current == mazeController.getEndTile()) {
                    isFinished = true;
                    pathTracing = true;
                    pathStep = current;
                    return false;
                }
    
                for (TileModel neighbor : mazeController.maze.getAccessibleNeighbors(current)) {
                    if (visited.contains(neighbor)) continue;
    
                    int alt = distance.get(current) + 1;
                    if (alt < distance.get(neighbor)) {
                        distance.put(neighbor, alt);
                        parentMap.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
    
                return false;
            } else {
                // No more tiles to explore
                isFinished = true;
                pathTracing = true;
                pathStep = mazeController.getEndTile();
                return false;
            }
        }

        if (pathTracing && pathStep != null) {
            if (parentMap.containsKey(pathStep)) {
                pathStep.status = TileStatus.PATH;
                pathCount++;
                pathStep = parentMap.get(pathStep);
                return false; // Continue tracing
            } else {
                // Reached the start tile
                if (pathStep == mazeController.getStartTile()) {
                    pathStep.status = TileStatus.PATH;
                    pathCount++;
                }
                pathStep = null;
                pathTracing = false;
                return true;
            }
        }

        return true;
    }

    @Override
    public int getVisitedCount(){
        return visited.size();
    }

    @Override
    public int getPathCount(){
        return pathCount;
    }

    @Override
    public boolean isComplete() {
        return isFinished && !pathTracing && pathStep == null;
    }

    @Override
    public long getExecutionTime(){
        return isFinished ? (endTime - startTime) : 0;
    }
}
