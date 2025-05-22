package model;

/**
 * Class that represent edge between two tiles in a maze
 */
public class EdgeModel {
    public TileModel tile1, tile2;

    /**
     * Construct a edge connecting two tiles
     * @param tile1
     * The first tile
     * @param tile2
     * The second tile
     */
    public EdgeModel(TileModel tile1, TileModel tile2){
        this.tile1 = tile1;
        this.tile2 = tile2;
    }
}
