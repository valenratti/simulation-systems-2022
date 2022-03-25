public class Grid3D extends Grid{
    Cell[][][] cells;

    public Grid3D(int dimension, Cell[][][] cells) {
        super(dimension);
        this.cells = cells;
    }
}
