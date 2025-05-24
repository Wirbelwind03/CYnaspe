package model;

/**
 * Represents an edge between two tiles in a maze.
 * <p>
 * This class models a connection or adjacency between two {@code TileModel} instances,
 * which can be used in maze generation or pathfinding algorithms.
 * </p>
 */
public class EdgeModel {
    /** The first tile connected by this edge */ 
    public TileModel tile1; 
    /** The second tile connected by this edge */ 
    public TileModel tile2;

    /**
     * Construct a edge connecting two tiles
     * @param tile1
     * The first tile
     * @param tile2
     * The second tile
     */
    public EdgeModel(TileModel tile1, TileModel tile2){
        if (tile1 == null || tile2 == null) {
            throw new IllegalArgumentException("Tiles cannot be null");
        }
        this.tile1 = tile1;
        this.tile2 = tile2;
    }
}
