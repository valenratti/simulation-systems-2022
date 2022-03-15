public class Main {
    public static void main(String[] args) {
        Config config = new Config(10.0, 10000, null, 0.2, false, 0.1, null, null);

        Area area = AreaGenerator.initializeAreaWithConfig(config);

        CellIndexMethod cellIndexMethod = new CellIndexMethod(area);
        cellIndexMethod.calculateNeighbours();

    }
}
