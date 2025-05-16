package utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.EdgeModel;
import model.GridModel;
import model.TileModel;

public class Kruskal {
    public static void generateMaze(GridModel grid){
        DisjointSet disjointSet = new DisjointSet();

        List<EdgeModel> edges = grid.getEdges();
        Collections.shuffle(edges);

        for (TileModel[] row : grid.tiles){
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
