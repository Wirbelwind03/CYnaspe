package utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.EdgeModel;
import model.MazeModel;
import model.TileModel;

public class Kruskal {
    private MazeModel maze;
    private DisjointSet disjointSet = new DisjointSet();

    public Kruskal(MazeModel maze){
        this.maze = maze;
        generateMaze();
    }

    public void generateMaze(){
        List<EdgeModel> edges = maze.getEdges();
        Collections.shuffle(edges);

        for (TileModel[] row : maze.tiles){
            for (TileModel tile: row){
                disjointSet.makeSet(tile);
            }
        }

        for (EdgeModel edge : edges){
            if (!disjointSet.connected(edge.tile1, edge.tile2)){
                disjointSet.union(edge.tile1, edge.tile2);
                edge.tile1.removeWall(edge.tile2);
            }
        }
    }
}
