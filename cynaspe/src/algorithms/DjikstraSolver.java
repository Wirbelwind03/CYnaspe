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

    public DjikstraSolver(MazeController mazeController) {
        this.mazeController = mazeController;
    }

    @Override
    public void solve() {
        // The distance from the start to each tile,
        Map<TileModel, Integer> distance = new HashMap<>();
        // The previous tiles with the shortest path
        Map<TileModel, TileModel> previous = new HashMap<>();
        // The tiles that have been visited
        Set<TileModel> visited = new HashSet<>();
        // Queue used to always expand the closest unvisited tile next
        PriorityQueue<TileModel> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Set up the distance for each tiles
        for (int r = 0; r < mazeController.maze.numRows; r++) {
            for (int c = 0; c < mazeController.maze.numCols; c++) {
                TileModel tile = mazeController.maze.tiles[r][c];
                distance.put(tile, Integer.MAX_VALUE);
            }
        }

        // Set the start tile with the value 0
        distance.put(mazeController.getStartTile(), 0);
        queue.add(mazeController.getStartTile());
        
        // Djikstra Algorimth
        while (!queue.isEmpty()) {
            // Get the tile with the smallest distance
            TileModel current = queue.poll();

            // Skip if the tile has been visited
            if (visited.contains(current)) continue;

            // Add the current tile to the visited list
            visited.add(current);
            current.status = TileStatus.VISITED;

            // If the current tile is the end, stop the algorithm
            if (current == mazeController.getEndTile()) break;

            // Check all the accesible neighbors
            for (TileModel neighbor : getAccessibleNeighbors(mazeController.maze, current)) {
                // If the neighbor has already been visited, skip it
                if (visited.contains(neighbor)) continue;

                // If the distance is shorter than what we had, update it
                int alt = distance.get(current) + 1;
                if (alt < distance.get(neighbor)) {
                    distance.put(neighbor, alt);
                    previous.put(neighbor, current); // Track the path
                    queue.add(neighbor); // Re-add for priority
                }
            }
        }

        // Reconstruct the shortest path
        TileModel step = mazeController.getEndTile();
        while (step != null && previous.containsKey(step)) {
            step.status = TileStatus.PATH;
            step = previous.get(step); // Move backward to the path
        }

        if (step == mazeController.getStartTile()) {
            mazeController.getStartTile().status = TileStatus.PATH;
        }
    }
    
    private static List<TileModel> getAccessibleNeighbors(MazeModel maze, TileModel tile) {
        List<TileModel> neighbors = new ArrayList<>();
        int r = tile.row;
        int c = tile.column;

        if (r > 0) {
            TileModel up = maze.tiles[r - 1][c];
            if (!tile.hasWallWith(up)) neighbors.add(up);
        }
        if (r < maze.numRows - 1) {
            TileModel down = maze.tiles[r + 1][c];
            if (!tile.hasWallWith(down)) neighbors.add(down);
        }
        if (c > 0) {
            TileModel left = maze.tiles[r][c - 1];
            if (!tile.hasWallWith(left)) neighbors.add(left);
        }
        if (c < maze.numCols - 1) {
            TileModel right = maze.tiles[r][c + 1];
            if (!tile.hasWallWith(right)) neighbors.add(right);
        }

        return neighbors;
    }
}
