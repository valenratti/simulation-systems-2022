package grid;

public abstract class Grid {
    protected int dimension;

    public Grid(int dimension) {
        this.dimension = dimension;
    }

    public abstract void initialize(Grid initializationGrid);
}
