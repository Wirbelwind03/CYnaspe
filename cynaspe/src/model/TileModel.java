package model;

import java.util.HashMap;
import java.util.Map;

import enums.TileStatus;
import enums.WallDirection;

/**
 * Represents a single tile in a maze.
 * <p>
 * Each tile has a position (row and column), walls on its four sides,
 * and status information used during maze generation and solving.
 * </p>
 */
public class TileModel {
    /**
     * The row where the tile is placed.
     */
    public int row;

    /**
     * The column where the tile is placed.
     */
    public int column;

    /**
     * The walls of the tile.
     * Represented like this:
     * {TOP: false, RIGHT: false, LEFT: false, BOTTOM: false}
     */
    public Map<WallDirection, Boolean> walls = new HashMap<>();

    /**
     * Indicates if the tile has been visited during generation.
     */
    public boolean isVisited = false;

    /**
     * The status of the tile during solving.
     */
    public TileStatus status = TileStatus.UNVISITED;


    /**
     * Construct a tile with the row and column given
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
     * Construct a case with the row, column and walls given
     * @param row
     * Row where the tile is placed
     * @param column
     * Column where the tile is placed
     * @param walls
     * The walls of the tile
     */
    public TileModel(int row, int column, Map<WallDirection,Boolean> walls){
        this.row = row;
        this.column = column;
        this.walls = walls;
    }

    /**
     * Remove the wall to get access to the neighbor
     * @param neighbor
     * The neighbor tile we want to access
     */
    public void removeWall(TileModel neighbor) {
        if (neighbor == null) {
            throw new IllegalArgumentException("neighbor cannot be null");
        }

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

        // Get the direction of the tile
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

    /**
     * Remove a wall from the tile
     * @param wallDirection
     * The direction of the wall we want to remove
     */
    public void removeWall(WallDirection wallDirection){
        // Check if the tile has a wall in the given direction
        if (isWallPresent(wallDirection)){
            // Update the walls Hashmap
            walls.put(wallDirection, false);
        }
    }

    /**
     * Add a wall from the tile
     * @param wallDirection
     * The direction of the wall we want to add
     */
    public void addWall(WallDirection wallDirection){
        // Check if the tile has a wall in the given direction
        if (!isWallPresent(wallDirection)){
            // Update the walls Hashmap
            walls.put(wallDirection, true);
        }
    }

    /**
     * Check if the tile has a wall with the neighbor
     * @param neighbor
     * The neighbor of the tile we want to see if there's a wall
     * @return
     * {@code true} if there is a wall, {@code false} if not
     */
    public boolean hasWallWith(TileModel neighbor) {
        int dx = neighbor.column - this.column;
        int dy = neighbor.row - this.row;

        // Get the direction of the neighbor
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

    /**
     * Check if the tile has a wall in the given direction
     * @param direction
     * The direction of the wall
     * @return
     * {@code true} if there's a wall, or {@code false} if there's not
     */
    public boolean isWallPresent(WallDirection direction) {
        return walls.getOrDefault(direction, false) == true;
    }

    /**
     * Return the walls of the tile in bits format
     * Order is TOP -> RIGHT -> BOTTOM -> LEFT
     * Example :
     * {TOP: false, RIGHT: true, LEFT: false, BOTTOM: false}
     * = 0100
     * @return
     * A {@code String} that reprent the wall in bits
     */
    public String getWallBits(){
        StringBuilder wallBits = new StringBuilder();
        // Write "1" if there's a wall, "0" if not
        wallBits.append(walls.get(WallDirection.TOP) ? "1" : "0"); // First bit
        wallBits.append(walls.get(WallDirection.RIGHT) ? "1" : "0"); // Second bit
        wallBits.append(walls.get(WallDirection.BOTTOM) ? "1" : "0"); // Third bit
        wallBits.append(walls.get(WallDirection.LEFT) ? "1" : "0"); // Fourth bit
        return wallBits.toString();
    }

    /**
     * Get the walls of the tile by reading the wallBits
     * @param wallBits
     * The bits of the wall, 
     * Order is TOP -> RIGHT -> BOTTOM -> LEFT
     * @return
     * {@code Map<WallDirection, Boolean>} of the actual walls
     * Example : 
     * 0100
     * = {TOP: false, RIGHT: true, LEFT: false, BOTTOM: false}
     */
    public static Map<WallDirection, Boolean> getWalls(String wallBits){
        if (wallBits == null || wallBits.length() != 4) {
            throw new IllegalArgumentException("wallBits must be a string of length 4");
        }

        Map<WallDirection, Boolean> walls = new HashMap<>();
        // Check the bits of the walls are correct
        for (int i = 0; i < 4; i++) {
            char c = wallBits.charAt(i);
            if (c != '0' && c != '1') {
                throw new IllegalArgumentException("wallBits must be only '0' or '1'");
            }
        }

        walls.put(WallDirection.TOP,    wallBits.charAt(0) == '1'); // First bit
        walls.put(WallDirection.RIGHT,  wallBits.charAt(1) == '1'); // Second bit
        walls.put(WallDirection.BOTTOM, wallBits.charAt(2) == '1'); // Third bit
        walls.put(WallDirection.LEFT,   wallBits.charAt(3) == '1'); // Fourth bit
    
        return walls;
    }
}
