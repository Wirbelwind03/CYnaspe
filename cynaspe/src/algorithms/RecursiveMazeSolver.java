import java.util.Stack;

import controller.MazeController;
import enums.TileStatus;
import model.TileModel;

public class RecursiveMazeSolver extends Solver implements ISolverAlgorithm {

    private Stack<TileModel> stack = new Stack<>();

    public RecursiveMazeSolver(MazeController mazeController) {
        this.mazeController = mazeController;

        TileModel start = mazeController.getStartTile();
        stack.push(start);
        visited.add(start);
    }

    @Override
    public boolean step() {
        if (isFinished && pathStep != null) {
            pathStep.status = TileStatus.PATH;
            pathCount++;
            pathStep = parentMap.get(pathStep);
            return pathStep == null;
        }

        if (!isFinished) {
            if (!stack.isEmpty()) {
                TileModel current = stack.pop();

                if (current.equals(mazeController.getEndTile())) {
                    isFinished = true;
                    pathStep = current;
                    return false;
                }

                for (TileModel neighbor : mazeController.maze.getAccessibleNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
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
