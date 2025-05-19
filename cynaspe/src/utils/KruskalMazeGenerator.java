package utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import controller.MazeConfigurationController;
import model.EdgeModel;
import model.MazeModel;
import model.TileModel;

public class KruskalMazeGenerator {
    private int currentIndex = 0;
    private List<EdgeModel> edges;
    private DisjointSet disjointSet;

    public  KruskalMazeGenerator(MazeModel maze, MazeConfigurationController mazeConfigurationController){
        disjointSet = new DisjointSet();

        edges = maze.getEdges();
        Collections.shuffle(edges, new Random(mazeConfigurationController.getMazeSeed()));

        for (TileModel[] row : maze.tiles){
            for (TileModel tile: row){
                disjointSet.makeSet(tile);
            }
        }
    }

    /**
     * Move one step forward the algorithm
     * @return
     * A boolean that say if the algorithm has been finished or not
     */
    public boolean step(){
        // If it complete, there isn't any more steps
        if (isComplete()) return false;

        EdgeModel edge = edges.get(currentIndex++);
        // If the two tiles arent connected
        if (!disjointSet.connected(edge.tile1, edge.tile2)){
            // Connect the two tiles
            disjointSet.union(edge.tile1, edge.tile2);
            // Remove the wall between the two
            edge.tile1.removeWall(edge.tile2);

            // Tell that the two tiles are visited
            edge.tile1.isVisited = true;
            edge.tile2.isVisited = true;
        }

        // There are steps remaining
        return true;
    }

    /**
     * Check if the algorithm has been completed
     * @return
     * A boolean that tell if the algorithm has been completed
     */
    public boolean isComplete() {
        return currentIndex >= edges.size();
    }
}
