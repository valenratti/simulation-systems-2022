import utils.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileWriter {

    private static BufferedWriter simulationBufferedWriter;
    private static BufferedWriter velocityBufferedWriter;

    private static BufferedWriter bigParticleMSDBufferedWriter;
    private static BufferedWriter smallParticleMSDBufferedWriter;
    private final static String SIMULATION_FILENAME = "positions.xyz";

    public static void generateXYZFile(){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter(SIMULATION_FILENAME);
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void generateVelocitiesFile(String filename){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter(filename);
            velocityBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printToFile(List<Particle> particles) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile();
        }

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

    public static void generateCollisionTimesFile(String filename, Map<Integer, Double> collisionTimes) throws IOException {
        java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
        BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);
        statisticsBuffererWriter.write("interval, probability");
        statisticsBuffererWriter.newLine();
        collisionTimes.forEach((bin, value) -> {try{
            statisticsBuffererWriter.write(bin + "-" + bin+1 + "," + String.valueOf(value));
            statisticsBuffererWriter.newLine();
        }catch (IOException e){
            System.out.println(e);
        }});
        statisticsBuffererWriter.flush();
    }

    public static void generateVelocityFile(String filename, List<Double> velocities) throws IOException {
        java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
        BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);
        statisticsBuffererWriter.write("velocities");
        statisticsBuffererWriter.newLine();
        velocities.forEach(velocity -> {try{
            statisticsBuffererWriter.write(String.valueOf(velocity));
            statisticsBuffererWriter.newLine();
        }catch (IOException e){
            System.out.println(e);
        }});
        statisticsBuffererWriter.flush();
    }

    public static void generateFrecuencyCollisionsTimeFile(String filename, double value) throws IOException {
        java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
        BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);
        statisticsBuffererWriter.write("Frecuencia de colisiones");
        statisticsBuffererWriter.newLine();
        statisticsBuffererWriter.write(String.valueOf(value));
        statisticsBuffererWriter.flush();
    }

    public static void generateBigParticlePositionFile(String filename, List<Pair> bigParticlePositions) {
        try{
            java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
            BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);

            statisticsBuffererWriter.write(
                    "x,y"
            );
            statisticsBuffererWriter.newLine();
            bigParticlePositions.forEach(particle -> {try{
                statisticsBuffererWriter.write(
                        particle.getX() + "," + particle.getY()
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

    public static void generateVelocitiesFileDuringSimulation(String filename, List<Particle> particles) throws IOException {
        if(velocityBufferedWriter == null){
            generateVelocitiesFile(filename);
        }
        particles.forEach(particle -> {
            try{
                if(!particle.isBig()) {
                    velocityBufferedWriter.write(String.valueOf(particle.getvModule()));
                    velocityBufferedWriter.newLine();
                }
            }catch (IOException e){
                System.out.println(e);
            }
        });
        velocityBufferedWriter.flush();
    }

    public static void generateParticleMSD(String filename, List<Pair> bigParticleMSD) {
        try{
            java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
            BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);

            statisticsBuffererWriter.write(
                    "time,msd"
            );
            statisticsBuffererWriter.newLine();
            bigParticleMSD.forEach(msd -> {try{
                statisticsBuffererWriter.write(
                        msd.getX() + "," + msd.getY()
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

