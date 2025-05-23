package controller;

import java.util.Random;
import java.util.Scanner;

import algorithms.BreadthFirstSolver;
import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import algorithms.RecursiveMazeSolver;
import enums.MazeType;
import model.MazeModel;
import utils.KruskalMazeGenerator;

public class MazeConsoleController {
    private Scanner scanner;

    /**
     * Controller that handle the maze in console version
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
        // Ask the configuration of the maze
        int numRows = askNumber("Enter nombre de lignes (2-16): ", 2, 16, false);
        int numCols = askNumber( "Entrer nombre de colonnes (2-16): ", 2, 16, false);
        int seed = askNumber( "Entrer graine (0, 2147483647), random if empty: ", 0, Integer.MAX_VALUE, true);
        int mazeTypeInput = askNumber( "Choir le mode de génération\n1 pour PARFAIT\n2 pour IMPARFAIT\n", 1, 2, false);
        MazeType mazeType = (mazeTypeInput == 2) ? MazeType.IMPERFECT : MazeType.PERFECT;

        // Generate the maze and render it
        MazeModel maze = new MazeModel(numRows, numCols);
        KruskalMazeGenerator kruskalMazeGenerator = new KruskalMazeGenerator(maze, seed, mazeType);
        while (!kruskalMazeGenerator.isComplete()){
            kruskalMazeGenerator.step();
        }
        maze.renderMazeConsole();

        // Ask which solver algorithm to use
        int solverAlgoInput = askNumber( "Choir l'algo de résolution\n1 pour DFS\n2 pour BFS\n3 pour Djikstra\n", 1, 3, false);

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
     * The value entered by the user in the console
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
