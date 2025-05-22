package algorithms;

public interface ISolverAlgorithm {
    /**
     * Advance through the algorithm
     * @return
     * True if the algorithm finished
     * False if not
     */
    public boolean step();
    /**
     * The number of tiles that has been visited
     * @return
     * The count of visited tiles
     */
    public int getVisitedCount();
    /**
     * The number of tiles that is the path
     * @return
     * The count of path tiles
     */
    public int getPathCount();
    /**
     * Tell if the algorithm has finished or not
     * @return
     * True if it finished
     * False if not
     */
    public boolean isComplete();
    public long getExecutionTime();
}
