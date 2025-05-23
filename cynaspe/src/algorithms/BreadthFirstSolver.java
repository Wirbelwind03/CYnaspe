package algorithms;

import model.MazeModel;
import model.TileModel;

import java.util.*;

import enums.TileStatus;

public class BreadthFirstSolver extends Solver implements ISolverAlgorithm {

    // Queue for the algorithm
    private Queue<TileModel> queue = new LinkedList<>();

    public BreadthFirstSolver(MazeModel maze) {
        this.maze = maze;

        // Get the start tile of the maze and add it to the queue so the BFS can start
        TileModel start = maze.getStartTile();
        queue.add(start);
        // Mark the start tile as visited
        visited.add(start);
        // Start the timer
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean step() {
        endTime = System.currentTimeMillis();

        // If the algorithm has finished, start tracing the path starting from the end tile
        if (isFinished && pathStep != null) {
            // Mark the current tile as being the path
            pathStep.status = TileStatus.PATH;
            pathCount++;
            // Move to the parent tile (previous step) in the path
            pathStep = parentMap.get(pathStep);
            // True if path tracing has finished (no more parents)
            return pathStep == null;
        }

        if (!isFinished) {
            // If there are tiles left to go
            if (!queue.isEmpty()) {
                // Dequeue the next tile to go
                TileModel current = queue.poll();

                // If the current tile is the end tile
                // The algoritm has finished
                if (current.equals(maze.getEndTile())) {
                    isFinished = true;
                    pathStep = current;
                    // return false because we have to do the path tracing
                    return false;
                }

                // Loop over the accessible neigbhors of the tile
                for (TileModel neighbor : maze.getAccessibleNeighbors(current)) {
                    // If the neighbor wasn't been visited
                    if (!visited.contains(neighbor)) {
                        // Add that the neighbor has been visited
                        visited.add(neighbor);
                        neighbor.status = TileStatus.VISITED;
                        // Add the neigbhor to be explored later
                        queue.add(neighbor);
                        // Track that we went to this neighbor with the current tile
                        parentMap.put(neighbor, current);
                    }
                }

                // Algorithm is still going
                return false;
            }

            // No path has been found
            isFinished = true;
            return true;
        }
        // End the algorithm
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
