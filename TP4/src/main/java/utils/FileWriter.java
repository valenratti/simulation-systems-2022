package utils;

import system.particlepropagation.ParticlePropagation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileWriter {

    private static BufferedWriter simulationBufferedWriter;

    public static void generateXYZFile(ParticlePropagation particlePropagation){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter("positions-" + LocalDateTime.now() + ".csv");
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printParticlePropagation(ParticlePropagation particlePropagation) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile(particlePropagation);
        }
        int i=0;
        for(Pair position : particlePropagation.getParticlePositions()) {
            if(i++ % 10 == 0) {
                simulationBufferedWriter.write(String.valueOf(particlePropagation.getCrystalParticles().size() + 1));
                simulationBufferedWriter.newLine();
                simulationBufferedWriter.newLine();
                particlePropagation.getCrystalParticles().forEach((particle) -> {
                    try {
                        simulationBufferedWriter.write(particle.getX() + " " + particle.getY() + " 1.0E-9 " + ((particle.getCharge() > 0) ? "1 0 0" : "0 0 1"));
                        simulationBufferedWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                simulationBufferedWriter.write(position.getX() + " " + position.getY() + " 1.0E-9 0 1 0");
                simulationBufferedWriter.newLine();
                simulationBufferedWriter.flush();
            }
        }


    }

}
