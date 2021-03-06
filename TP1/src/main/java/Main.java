import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        for(int i =1 ; i<=20; i++){
            System.out.println("M provided " + i);
            execute(i);
        }

    }

    public static void execute(int M) throws FileNotFoundException {
        Config config = new Config(20.0, 1000, M, 1.0, true, 0.25, null, null);
        Area area = AreaGenerator.initializeAreaWithConfig(config);

        final long bruteStart = System.nanoTime();
        BruteForceMethod bruteForceMethod = new BruteForceMethod(area);
        bruteForceMethod.calculateNeighbours(config.isPeriodicBorderCondition());
        final long bruteEnd = System.nanoTime();

        final long cellStart = System.nanoTime();
        CellIndexMethod cellIndexMethod = new CellIndexMethod(area, config);
        cellIndexMethod.calculateNeighbours();
        final long cellEnd = System.nanoTime();

        cellIndexMethod.exportPositions("positions");
        cellIndexMethod.exportNeighbours("neighbours");

        System.out.println("Brute force method: " + (bruteEnd - bruteStart) / 1000000 + " ms");
        System.out.println("Cell Index method: " + (cellEnd - cellStart) / 1000000 + " ms" );
        System.out.println("Using M = " + cellIndexMethod.getCellsPerColumn());

    }
}
