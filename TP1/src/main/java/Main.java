import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Config config = new Config(20.0, 1000, null, 1.0, true, 0.25, null, null);
        Area area = AreaGenerator.initializeAreaWithConfig(config);
        CellIndexMethod cellIndexMethod = new CellIndexMethod(area, config);
        cellIndexMethod.calculateNeighbours(config.isPeriodicBorderCondition());
        cellIndexMethod.exportPositions("positions");
        cellIndexMethod.exportNeighbours("neighbours");

    }
}
