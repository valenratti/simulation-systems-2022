import cell_index_method.CIMConfig;
import cell_index_method.CellIndexMethod;

public class Simulator {
    final private static int kn = (int) Math.pow(10, 5);   // 10^5 N/m
    final private static int kt = 2 * kn;

    public static void simulate(final CIMConfig config) {
        CellIndexMethod cellIndexMethod = new CellIndexMethod(config);
    }
}
