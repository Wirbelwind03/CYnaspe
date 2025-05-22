package algorithms;

public interface ISolverAlgorithm {
    public boolean step();
    public int getVisitedCount();
    public int getPathCount();
    public boolean isComplete();
    public long getExecutionTime();
}
