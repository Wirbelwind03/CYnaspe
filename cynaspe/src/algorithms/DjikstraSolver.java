package algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import enums.TileStatus;
import model.MazeModel;
import model.TileModel;

public class DjikstraSolver extends Solver implements ISolverAlgorithm {
    // The distance from the start to each tile,
    private Map<TileModel, Integer> distance;
    // Queue used to always expand the closest unvisited tile next
    private PriorityQueue<TileModel> queue;

    public DjikstraSolver(MazeModel maze) {
        this.maze = maze;

        distance = new HashMap<>();
        parentMap = new HashMap<>();
        visited = new HashSet<>();
        queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        for (int r = 0; r < maze.numRows; r++) {
            for (int c = 0; c < maze.numCols; c++) {
                TileModel tile = maze.tiles[r][c];
                distance.put(tile, Integer.MAX_VALUE);
            }
        }

        TileModel start = maze.getStartTile();
        distance.put(start, 0);
        queue.add(start);

        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean step(){
        

        if (isFinished && pathStep != null) {
            if (parentMap.containsKey(pathStep)) {
                pathStep.status = TileStatus.PATH;
                pathCount++;
                pathStep = parentMap.get(pathStep);
                return false; // Continue tracing
            } else {
                // Reached the start tile
                if (pathStep == maze.getStartTile()) {
                    pathStep.status = TileStatus.PATH;
                    pathCount++;
                }
                pathStep = null;
                return true;
            }
        }

        if (!isFinished) {
            if (!queue.isEmpty()) {
                // Get the tile with the shortest distance
                TileModel current = queue.poll();

                // If it has been already visited, skip it
                if (visited.contains(current)) return false;
    
                // Mark that this tile has been visited
                visited.add(current);
                current.status = TileStatus.VISITED;
    
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
                    // If the neighbor has already been visited, skip
                    if (visited.contains(neighbor)) continue;
    
                    // Calculate the distance to neighbor at the current tile
                    int alt = distance.get(current) + 1;

                    // If the path to neighbor is shorter, update the parent and distance
                    if (alt < distance.get(neighbor)) {
                        distance.put(neighbor, alt);
                        parentMap.put(neighbor, current);
                        // Add neigbhor so that it's explored later
                        queue.add(neighbor);
                    }
                }
                
                // Continue the algorithm
                return false;
            } else {
                // No more tiles to explore, and end not reached
                isFinished = true;
                endTime = System.currentTimeMillis();
                pathStep = maze.getEndTile();
                return false;
            }
        }
        // Algorithm and path tracing has been finished
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
        return isFinished && pathStep == null;
    }

    @Override
    public long getExecutionTime(){
        return isFinished ? (endTime - startTime) : 0;
    }
}
