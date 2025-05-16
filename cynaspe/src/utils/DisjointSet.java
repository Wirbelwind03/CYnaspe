package utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.Cell;
import model.TileModel;

public class DisjointSet {
    private Map<TileModel, TileModel> parent = new HashMap<>();

    public void makeSet(TileModel tile){
        parent.put(tile, tile);
    }

    public TileModel find(TileModel tile){
        if (parent.get(tile) != tile){
            parent.put(tile, find(parent.get(tile)));
        }
        return parent.get(tile);
    }

    public void union(TileModel tile1, TileModel tile2){
        TileModel root1 = find(tile1);
        TileModel root2 = find(tile2);
        if (root1 != root2){
            parent.put(root1, root2);
        }
    }

    public boolean connected(TileModel tile1, TileModel tile2) {
        return find(tile1) == find(tile2);
    }
}
