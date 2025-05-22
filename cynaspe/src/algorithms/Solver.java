package algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import controller.MazeController;
import model.TileModel;

public abstract class Solver {
    protected MazeController mazeController;

    // Stores the parent of each visited tile for the reconstruction of the path
    protected Map<TileModel, TileModel> parentMap = new HashMap<>();
    // The tiles that have been visited
    protected Set<TileModel> visited = new HashSet<>();

    // Indicate that the algorithm has finished
    protected boolean isFinished = false;
    // Tile to trace the path once the algorithm has finished
    protected TileModel pathStep = null;
    // How many path tile there are
    protected int pathCount = 0;

    protected long startTime = 0;
    protected long endTime = 0;
}
