package algorithms;

/**
 * Interface for maze solver algorithms.
 * <p>
 * Classes implementing this interface must provide methods to solve
 * a maze, like pathfinding.
 * </p>
 */
public interface ISolverAlgorithm {
    /**
     * Advance through the algorithm
     * @return
     * {@code true} if the algorithm finished
     * {@code false} if not
     */
    public boolean step();
    /**
     * The number of tiles that has been visited
     * @return
     * A {@code int} count of visited tiles
     */
    public int getVisitedCount();
    /**
     * The number of tiles that is the path
     * @return
     * A {@code int} count of path tiles
     */
    public int getPathCount();
    /**
     * Tell if the algorithm has finished or not
     * @return
     * {@code true} if it finished
     * {@code false} if not
     */
    public boolean isComplete();
    /**
     * Get the execution time of the algorithm
     * @return
     * A {@code long} that represent the execution time
     */
    public long getExecutionTime();
}
