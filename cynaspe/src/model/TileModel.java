package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Cell;

import enums.WallDirection;

public class TileModel {
    public int row;
    public int column;
    public Map<WallDirection, Boolean> walls = new HashMap<>();
    
    List<TileModel> neighbors = new ArrayList<>();
    boolean visited = false;

    public TileModel(int row, int column){
        this.row = row;
        this.column = column;
        for (WallDirection dir : WallDirection.values()) {
            walls.put(dir, true);
        }
    }

    public void removeWall(TileModel neighbor) {
        int dx = neighbor.column - this.column;
        int dy = neighbor.row - this.row;

        if (dx == 1) { // neighbor is to the right
            this.walls.put(WallDirection.RIGHT, false);
            neighbor.walls.put(WallDirection.LEFT, false);
        } else if (dx == -1) { // neighbor is to the left
            this.walls.put(WallDirection.LEFT, false);
            neighbor.walls.put(WallDirection.RIGHT, false);
        } else if (dy == 1) { // neighbor is below
            this.walls.put(WallDirection.BOTTOM, false);
            neighbor.walls.put(WallDirection.TOP, false);
        } else if (dy == -1) { // neighbor is above
            this.walls.put(WallDirection.TOP, false);
            neighbor.walls.put(WallDirection.BOTTOM, false);
        }
    }

}
