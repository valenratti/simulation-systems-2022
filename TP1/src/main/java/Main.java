import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Config config = new Config(10.0, 50, null, 0.2, false, 0.1, null, null);

        Area area = AreaGenerator.initializeAreaWithConfig(config);

        CellIndexMethod cellIndexMethod = new CellIndexMethod(area);
        cellIndexMethod.calculateNeighbours(config.isPeriodicBorderCondition());
        cellIndexMethod.exportPositions("positions.txt");
        cellIndexMethod.exportNeighbours("neighbours.txt");

    }
}
