import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FileWriter {

    private static BufferedWriter simulationBufferedWriter;
    private final static String FILENAME = "output.txt";
    private final static String SIMULATION_FILENAME = "positions.xyz";

    public static void generateXYZFile(){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter(SIMULATION_FILENAME);
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printToFile(List<Particle> particles) throws IOException {
        simulationBufferedWriter.write(String.valueOf(particles.size()));
        simulationBufferedWriter.newLine();
        simulationBufferedWriter.newLine();
        particles.forEach(particle -> {
            try{
                simulationBufferedWriter.write(
                        particle.getId()
                                + " " + particle.getX()
                                + " " + particle.getY()
                                + " " + particle.getVx()
                                + " " + particle.getVy()
                                + " " + particle.getRadius()
                                + " " + particle.getMass());
                simulationBufferedWriter.newLine();
            }catch (IOException e){
                System.out.println(e);
            }
        });
        simulationBufferedWriter.flush();
    }

    public static void generateVelocitiesFile(String filename, List<Particle> particles){
        try{
            java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
            BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);

            statisticsBuffererWriter.write(
                    "particle,velocity"
            );
            statisticsBuffererWriter.newLine();
            particles.forEach(particle -> {try{
                statisticsBuffererWriter.write(
                        particle.getId() + "," + particle.getvModule()
                );
                statisticsBuffererWriter.newLine();
            }catch (IOException e){
                System.out.println(e);
            }});
            statisticsBuffererWriter.flush();

        }catch(IOException e){
            System.out.println(e);
        }
    }
}

