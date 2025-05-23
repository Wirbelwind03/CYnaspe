package utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import enums.MazeType;
import model.EdgeModel;
import model.MazeModel;
import model.TileModel;

public class KruskalMazeGenerator {
    // The type of maze to generate
    private MazeType mazeType = MazeType.PERFECT;
    // Index used for looping through edges during the maze generation
    private int currentIndex = 0;
    // List of edges between tile in the maze
    private List<EdgeModel> edges;
    // Disjoint set to track connected tiles
    private DisjointSet disjointSet;

    /**
     * Constructor to initialize the generator
     * @param maze
     * The grid of tiles to generate the maze on
     * @param mazeConfigurationController
     * Controller managing the maze settings
     * @param mazeType
     * The type of maze (PERFECT or IMPERFECT)
     */
    public  KruskalMazeGenerator(MazeModel maze, int seed, MazeType mazeType){
        // Set the maze type
        this.mazeType = mazeType;

        disjointSet = new DisjointSet();

        edges = maze.getEdges();
        // Shuffle the edges randomly for random maze structure
        // Seed for re-creating a maze
        Collections.shuffle(edges, new Random(seed));

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
        } else if (mazeType == MazeType.IMPERFECT){
            if (Math.random() < 0.50) {
                edge.tile1.removeWall(edge.tile2);
            }
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
