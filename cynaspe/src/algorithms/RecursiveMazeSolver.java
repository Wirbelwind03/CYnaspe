package algorithms;

import java.util.Stack;

import enums.TileStatus;
import model.MazeModel;
import model.TileModel;

public class RecursiveMazeSolver extends Solver implements ISolverAlgorithm {

    private Stack<TileModel> stack = new Stack<>();

    public RecursiveMazeSolver(MazeModel maze) {
        this.maze = maze;

        TileModel start = maze.getStartTile();
        stack.push(start);
        visited.add(start);
    }

    @Override
    public boolean step() {
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
            if (!stack.isEmpty()) {
                // Pop the next tile to go
                TileModel current = stack.pop();

                // If the current tile is the end tile
                // The algoritm has finished
                if (current.equals(maze.getEndTile())) {
                    isFinished = true;
                    endTime = System.currentTimeMillis();
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
                        stack.push(neighbor);
                        // Track that we went to this neighbor with the current tile
                        parentMap.put(neighbor, current);
                    }
                }

                // Algorithm is still going
                return false;
            }

            // No path has been found
            isFinished = true;
            endTime = System.currentTimeMillis();
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