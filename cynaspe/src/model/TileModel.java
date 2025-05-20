package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import enums.TileStatus;
import enums.WallDirection;

/**
 * Model that represent the case in a grid
 */
public class TileModel {
    public int row; // The row where the tile is placed
    public int column; // The column where the tile is placed
    // The walls of the tile
    // Represented like this
    // {TOP: false, RIGHT: false, LEFT: false, BOTTOM: false}
    public Map<WallDirection, Boolean> walls = new HashMap<>(); 
    public boolean isVisited = false; // If the tile has been visited

    public TileStatus status = TileStatus.UNVISITED;

    /**
     * Construct a case with the row and column given
     * @param row
     * Row where the tile is placed
     * @param column
     * Column where the tile is placed
     */
    public TileModel(int row, int column){
        this.row = row;
        this.column = column;
        // Put walls in all direction of the tile
        for (WallDirection dir : WallDirection.values()) {
            walls.put(dir, true);
        }
    }

    /**
     * Remove the wall to get access to the neighbor
     * @param neighbor
     * The tile we want to access
     */
    public void removeWall(TileModel neighbor) {
        // See if the tile is a neighbor by calculating the distance difference between the two
        // For example : A case with (2,0) and the neighbor is (3,0)
        // dx = 0 - 0 = 0
        // dy = 3 - 2 = 1
        // -> The neighbor is at the right
        // Another example : A case (2,0) and the neighbor is (5,1)
        // dx = 1 - 0 = 1
        // dy = 5 - 2 = 3
        // -> The neighbor is below
        int dx = neighbor.column - this.column;
        int dy = neighbor.row - this.row;

        /**
         * Schema of what the number represent for the neighbor
         * With "o" being the tile
         *     -1 
         *      |
         * -1 --o-- 1
         *      |
         *      1
         */

        if (dx == 1) { // Neighbor is at the right
            walls.put(WallDirection.RIGHT, false);
            neighbor.walls.put(WallDirection.LEFT, false);
        } else if (dx == -1) { // Neighbor is at the left
            walls.put(WallDirection.LEFT, false);
            neighbor.walls.put(WallDirection.RIGHT, false);
        } else if (dy == 1) { // Neighbor is below
            walls.put(WallDirection.BOTTOM, false);
            neighbor.walls.put(WallDirection.TOP, false);
        } else if (dy == -1) { // Neighbor is above
            walls.put(WallDirection.TOP, false);
            neighbor.walls.put(WallDirection.BOTTOM, false);
        }
    }

    public boolean hasWallWith(TileModel neighbor) {
        int dx = neighbor.column - this.column;
        int dy = neighbor.row - this.row;

        if (dx == 1) { // Neighbor is at the right
            return this.walls.getOrDefault(WallDirection.RIGHT, true);
        } else if (dx == -1) { // Neighbor is at the left
            return this.walls.getOrDefault(WallDirection.LEFT, true);
        } else if (dy == 1) { // Neighbor is below
            return this.walls.getOrDefault(WallDirection.BOTTOM, true);
        } else if (dy == -1) { // Neighbor is above
            return this.walls.getOrDefault(WallDirection.TOP, true);
        }

        return false;
    }
}
