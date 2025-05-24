package controller;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import algorithms.BreadthFirstSolver;
import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import algorithms.RecursiveMazeSolver;
import enums.MazeType;
import io.MazeReader;
import io.MazeWriter;
import model.MazeModel;
import utils.KruskalMazeGenerator;

/**
 * Controller that manages interaction for the maze in the console.
 * <p>
 * Handles input and output through the command line.
 * </p>
 */
public class MazeConsoleController {
    /** Used for reading the user input in the console */
    private Scanner scanner;

    /**
     * Constructs a new {@code MazeConsoleController} that handle the maze in console version
     * @param scanner
     * The console where the user write/read
     */
    public MazeConsoleController(Scanner scanner){
        this.scanner = scanner;
    }

    /**
     * Run the maze console version
     */
    public void run(){
        // Used for the loop
        boolean choosingOptions = true;

        MazeModel maze = null;
        while (choosingOptions) {
            int options = askNumber( "---Options---\n1 pour un nouveau labyrinthe\n2 pour charger un labyrinthe existant\n", 1, 2, false);
            
            switch (options) {
                case 1:
                    // Ask the configuration of the maze
                    int numRows = askNumber("Enter nombre de lignes (2-16): ", 2, 16, false);
                    int numCols = askNumber( "Entrer nombre de colonnes (2-16): ", 2, 16, false);
                    int seed = askNumber( "Entrer graine (0, 2147483647), random if empty: ", 0, Integer.MAX_VALUE, true);
                    int mazeTypeInput = askNumber( "Choir le mode de génération\n1 pour PARFAIT\n2 pour IMPARFAIT\n", 1, 2, false);
                    MazeType mazeType = (mazeTypeInput == 2) ? MazeType.IMPERFECT : MazeType.PERFECT;
    
                    // Generate the maze and render it
                    maze = new MazeModel(numRows, numCols);
                    KruskalMazeGenerator kruskalMazeGenerator = new KruskalMazeGenerator(maze, seed, mazeType);
                    while (!kruskalMazeGenerator.isComplete()){
                        kruskalMazeGenerator.step();
                    }
                    // stop the loop
                    choosingOptions = false;
                    break;
            
                case 2:
                    System.out.print("Entrer le chemin du fichier .maze : ");
                    String filePath = scanner.nextLine();
    
                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        maze = MazeReader.read(file);
                        if (maze != null){
                            // stop the loop
                            choosingOptions = false;
                        } else {
                            System.out.println("Erreur chargement du fichier");
                        }
                    } else {
                        System.out.println("Chemin invalide");
                        int stopInput = askNumber( "Voulez vous arretez le programme ?\n1 pour OUI\n2 pour NON\n", 1, 2, false);
                        if (stopInput == 1){
                            System.exit(0);
                        }
                    }
                    break;
    
                default:
                    break;
            }    
        }

        maze.renderMazeConsole();

        int askSolveInput = askNumber( "Voulez vous résoudre ce labyrinthe ?\n1 pour OUI\n2 pour NON\n", 1, 2, false);
        if (askSolveInput == 1){
            solveMaze(maze);
        }

        // Sauvegarde du labyrinthe
        int saveOptions = askNumber( "Voulez vous sauvegarder ce labyrinthe ?\n1 pour OUI\n2 pour NON\n", 1, 2, false);
        if (saveOptions == 1){
            saveMaze(maze);
        }
    }

    /**
     * Solve the maze through the console
     * @param maze
     * The maze to solve
     */
    private void solveMaze(MazeModel maze){
        // Ask which solver algorithm to use
        int solverAlgoInput = askNumber( "Choisir l'algorithme de résolution\n1 pour DFS\n2 pour BFS\n3 pour Djikstra\n", 1, 3, false);

        ISolverAlgorithm solverAlgorithm;
        switch (solverAlgoInput) {
            case 1:
                solverAlgorithm = new RecursiveMazeSolver(maze);
                break;

            case 2:
                solverAlgorithm = new BreadthFirstSolver(maze);
                break;

            case 3:
                solverAlgorithm = new DjikstraSolver(maze);
                break;
            default:
                solverAlgorithm = new RecursiveMazeSolver(maze);
                break;
        }
        // Solve the maze and render it
        while (!solverAlgorithm.isComplete()){
            solverAlgorithm.step();
        }
        maze.renderMazeConsole();
    }

    /**
     * Save the maze through the console
     * @param maze
     * The maze to save
     */
    private void saveMaze(MazeModel maze){
        // Check if the "mazeOutputs" folder exist
        File outputDir = new File("mazeOutputs");
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                System.out.println("Échec de la création du dossier 'mazeOutputs'");
                System.exit(1);
            }
        } 

        // Enter the file name
        System.out.print("Entrer le nom du fichier : ");
        String fileName = scanner.nextLine();
        File saveFile = new File(outputDir, fileName);

        // Add the ".maze" extension
        if (!fileName.endsWith(".maze")) {
            saveFile = new File(outputDir, fileName + ".maze");
        }

        // Write the maze and save it
        MazeWriter.write(maze, saveFile);
        System.out.println("Labyrinthe sauvegardé dans : " + saveFile.getAbsolutePath());
    }
  
    /**
     * Ask a number in the console
     * @param prompt
     * The string to show in the console
     * @param min
     * The minimum value the user can enter
     * @param max
     * The maximum value the user can enter
     * @param canBeEmpty
     * If the input can be empty, if it is, return a random between mix and max
     * @return
     * A {@code int} that represent the value entered by the user in the console
     * Random value if the user entered nothing and the input can be empty
     */
    public int askNumber(String prompt, int min, int max, boolean canBeEmpty) {
        if (min > max) {
            throw new IllegalArgumentException("min doit être < max");
        }

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if (canBeEmpty){
                    Random random = new Random();
                    return random.nextInt(max-min) + min;
                }
                System.out.println("La saisie ne peut pas être vide.");
                continue;
            } 

            try {
                int num = Integer.parseInt(input);
                if (num >= min && num <= max) {
                    return num;
                } else {
                    System.out.println("Le nombre doit être entre " + min + " et " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Saisie invalid, Veuillez saisir un nombre de nouveau.");
            }
        }
    }
}
