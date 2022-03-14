public class Main {
    public static void main(String[] args) {
        Config config = new Config(100, 1000, 10, 0.2, false, 0.1, null, null);

        Area area = AreaGenerator.initializeAreaWithConfig(config);

        System.out.println("test");
    }
}
