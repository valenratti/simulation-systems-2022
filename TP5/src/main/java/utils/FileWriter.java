package utils;

import model.Particle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FileWriter {
    private static BufferedWriter simulationBufferedWriter;

    public static void generateXYZFile(){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter("positions-" + LocalDateTime.now() + ".csv");
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printPositions(List<Particle> particleList) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile();
        }

        simulationBufferedWriter.write(String.valueOf(particleList.size()));
        simulationBufferedWriter.newLine();
        simulationBufferedWriter.newLine();
        particleList.forEach(particle ->
        {
            try {
                simulationBufferedWriter.write(particle.getId()
                        + " " + particle.getX()
                        + " " + particle.getY()
                        + " " + particle.getVx()
                        + " " + particle.getVy()
                        + " " + particle.getRadius()
                        + " " + particle.getMass()
                        + " " + particle.getPressure());
                simulationBufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        simulationBufferedWriter.flush();
    }
}
