package solver;

import model.MazeModel;
import model.TileModel;

public class WallFollowerSolver {

    private MazeModel maze;
    private TileModel current;
    private int direction;

    public WallFollowerSolver(MazeModel maze) {
        this.maze = maze;
        this.current = maze.getStartTile();
        this.direction = 1; // commence vers la droite (arbitraire)
    }

    public void solve() {
        while (!current.equals(maze.getEndTile())) {
            current.setAsPath(); // marquer le chemin visité

            // Tourner à droite
            int droite = (direction + 1) % 4;
            TileModel voisinDroite = getNeighbor(current, droite);
            if (voisinDroite != null && !current.hasWallWith(voisinDroite)) {
                current = voisinDroite;
                direction = droite;
                continue;
            }

            // Avancer
            TileModel voisinAvant = getNeighbor(current, direction);
            if (voisinAvant != null && !current.hasWallWith(voisinAvant)) {
                current = voisinAvant;
                continue;
            }

            // Tourner à gauche
            int gauche = (direction + 3) % 4;
            TileModel voisinGauche = getNeighbor(current, gauche);
            if (voisinGauche != null && !current.hasWallWith(voisinGauche)) {
                current = voisinGauche;
                direction = gauche;
                continue;
            }

            // Demi-tour
            direction = (direction + 2) % 4;
        }

        maze.getEndTile().setAsPath(); // marquer la fin aussi
    }

    // Retourne le voisin dans une direction (0=haut, 1=droite, 2=bas, 3=gauche)
    private TileModel getNeighbor(TileModel tile, int dir) {
        int x = tile.getX();
        int y = tile.getY();

        switch (dir) {
            case 0: return maze.getTile(x, y - 1); // haut
            case 1: return maze.getTile(x + 1, y); // droite
            case 2: return maze.getTile(x, y + 1); // bas
            case 3: return maze.getTile(x - 1, y); // gauche
            default: return null;
        }
    }
}
