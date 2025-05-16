package utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.EdgeModel;
import model.MazeModel;
import model.TileModel;

public class KruskalMazeGenerator {
    private int currentIndex = 0;
    private List<EdgeModel> edges;
    private DisjointSet disjointSet;

    public  KruskalMazeGenerator(MazeModel maze){
        disjointSet = new DisjointSet();

        edges = maze.getEdges();
        Collections.shuffle(edges);

        for (TileModel[] row : maze.tiles){
            for (TileModel tile: row){
                disjointSet.makeSet(tile);
            }
        }
    }

    public boolean step(){
        if (currentIndex >= edges.size()) return false;

        EdgeModel edge = edges.get(currentIndex++);
        if (!disjointSet.connected(edge.tile1, edge.tile2)){
            disjointSet.union(edge.tile1, edge.tile2);
            edge.tile1.removeWall(edge.tile2);

            edge.tile1.isVisited = true;
            edge.tile2.isVisited = true;
        }

        return true;
    }

    public boolean isComplete() {
        return currentIndex >= edges.size();
    }
}
