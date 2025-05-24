package utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import enums.MazeType;
import model.EdgeModel;
import model.MazeModel;
import model.TileModel;

/**
 * Maze generator using Kruskal's algorithm.
 * <p>
 * This generator creates a maze by treating each tile as a node and
 * connecting them with edges, ensuring no cycles.
 * </p>
 */
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
     * @param seed
     * The seed used for the generation, can re-create a same maze
     * @param mazeType
     * The type of maze (PERFECT or IMPERFECT)
     */
    public  KruskalMazeGenerator(MazeModel maze, int seed, MazeType mazeType){
        // Check if the maze is correct
        if (maze == null) throw new IllegalArgumentException("Maze cannot be null");
        if (maze.getEdges() == null) throw new IllegalArgumentException("Maze edges cannot be null");
        if (maze.tiles == null) throw new IllegalArgumentException("Maze tiles cannot be null");

        // Set the maze type
        this.mazeType = mazeType;

        disjointSet = new DisjointSet();

        edges = maze.getEdges();
        // Shuffle the edges randomly for random maze structure
        // Seed for re-creating a maze
        Collections.shuffle(edges, new Random(seed));

        for (TileModel[] row : maze.tiles){
            if (row == null) throw new IllegalArgumentException("Row cannot be null");
            for (TileModel tile: row){
                if (tile == null) throw new IllegalArgumentException("Tile cannot be null");
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
     * {@code true} if the algorithm has finished, {@code false} if not
     */
    public boolean isComplete() {
        return currentIndex >= edges.size();
    }
}
