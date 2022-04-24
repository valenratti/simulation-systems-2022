import utils.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileWriter {

    private static BufferedWriter simulationBufferedWriter;
    private static BufferedWriter velocityBufferedWriter;

    public static void reset(){
        simulationBufferedWriter = null;
        velocityBufferedWriter = null;
    }

    public static void generateXYZFile(int n){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter("positions-" + LocalDateTime.now()+ "-n-" + n + ".csv");
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

    public static void printToFile(List<Particle> particles, int n) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile(n);
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
            statisticsBuffererWriter.write(bin + "-" + (bin+1) + "," + String.valueOf(value));
            statisticsBuffererWriter.newLine();
        }catch (IOException e){
            System.out.println(e);
        }});
        statisticsBuffererWriter.flush();
    }

    public static void generateVelocityFile(String filename, Map<BigDecimal, Double> velocities) throws IOException {
        java.io.FileWriter statisticsFileWriter = new java.io.FileWriter(filename);
        BufferedWriter statisticsBuffererWriter = new BufferedWriter(statisticsFileWriter);
        statisticsBuffererWriter.write("interval");
        statisticsBuffererWriter.newLine();
        for(BigDecimal key : velocities.keySet().stream().sorted().collect(Collectors.toList()) ) {
            try{
                statisticsBuffererWriter.write(key + "-" + (key.add(BigDecimal.valueOf(0.1)) + "," + velocities.get(key)));
                statisticsBuffererWriter.newLine();
            }catch (IOException e){
                System.out.println(e);
            }
        };
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

