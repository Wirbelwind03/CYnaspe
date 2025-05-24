package utils;

import java.util.HashMap;
import java.util.Map;

import model.TileModel;

/**
 * Disjoint-set (union-find) data structure for managing connected components
 * <p>
 * This class is used in maze generation algorithms
 * to efficiently track and merge disjoint sets of tiles. Each tile starts in its own set,
 * and sets can be merged using union operations while checking connectivity using find operations.
 * </p>
 */
public class DisjointSet {
    /**
     * Maps each tile to its parent in the disjoint-set.
     * If a tile is its own parent, it's the root of it own set.
     */
    private Map<TileModel, TileModel> parent = new HashMap<>();

    /**
     * Make a set between two TileModel
     * @param tile
     * The other TileModel to make a set with
     */
    public void makeSet(TileModel tile){
        parent.put(tile, tile);
    }

    /**
     * Find a TileModel in the HashMap
     * @param tile
     * The TileModel we are searching in the HashMap
     * @return
     * The TileModel found
     */
    public TileModel find(TileModel tile){
        if (parent.get(tile) != tile){
            parent.put(tile, find(parent.get(tile)));
        }
        return parent.get(tile);
    }

    /**
     * Connect two TileModel
     * @param tile1
     * The first tile
     * @param tile2
     * The second tile
     */
    public void union(TileModel tile1, TileModel tile2){
        TileModel root1 = find(tile1);
        TileModel root2 = find(tile2);
        if (root1 != root2){
            parent.put(root1, root2);
        }
    }

    /**
     * Check if two TileModel are connected
     * @param tile1
     * The first tile
     * @param tile2
     * The second tile
     * @return
     * True if the two tiles are connected, False if not
     */
    public boolean connected(TileModel tile1, TileModel tile2) {
        return find(tile1) == find(tile2);
    }
}
