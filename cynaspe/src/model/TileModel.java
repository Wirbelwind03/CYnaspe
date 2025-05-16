package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Cell;

import enums.WallDirection;

/**
 * Répresente une case
 */
public class TileModel {
    public int row; // La ligne où se trouve la case
    public int column; // La colonne où se trouve la case
    public Map<WallDirection, Boolean> walls = new HashMap<>(); // Les murs de la case
    public boolean isVisited = false; // Si la case a été visité

    /**
     * Construit une case a mettre dans une ligne et colonne
     * @param row
     * La ligne où se trouve la case 
     * @param column
     * La colonne où se trouve la case
     */
    public TileModel(int row, int column){
        this.row = row;
        this.column = column;
        // Mettre des murs dans toutes les directions
        for (WallDirection dir : WallDirection.values()) {
            walls.put(dir, true);
        }
    }

    /**
     * Remove le mur de cette case pour accéder au voisin
     * @param neighbor
     * La case voisine dans lequel on veut accéder
     */
    public void removeWall(TileModel neighbor) {
        // Voir si la case est un voisin
        int dx = neighbor.column - this.column;
        int dy = neighbor.row - this.row;

        /**
         * Répresentation de comment on voit que c'est un voisin
         *     -1 
         *      |
         * -1 --o-- 1
         *      |
         *      1
         */

        if (dx == 1) { // Le voisin est à droite
            walls.put(WallDirection.RIGHT, false);
            neighbor.walls.put(WallDirection.LEFT, false);
        } else if (dx == -1) { // Le voision est à gauche
            walls.put(WallDirection.LEFT, false);
            neighbor.walls.put(WallDirection.RIGHT, false);
        } else if (dy == 1) { // Le voisin est en dessous
            walls.put(WallDirection.BOTTOM, false);
            neighbor.walls.put(WallDirection.TOP, false);
        } else if (dy == -1) { // Le voisin est au dessus
            walls.put(WallDirection.TOP, false);
            neighbor.walls.put(WallDirection.BOTTOM, false);
        }
    }

}
