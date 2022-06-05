package utils;

import model.Particle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FileWriter {
    private static BufferedWriter simulationBufferedWriter;

    public static void generateXYZFile(double exitWidth){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter("positions-" + exitWidth + ".csv");
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void printPositions(double exitWidth, List<Particle> particleList) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile(exitWidth);
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

    public static void printCaudal(Double exitWidth, List<Vector> caudalList, double dt) throws IOException {
        BufferedWriter simulationBufferedWriter;
        java.io.FileWriter fileWriter = new java.io.FileWriter("caudal-" + exitWidth + ".csv");
        simulationBufferedWriter = new BufferedWriter(fileWriter);
        simulationBufferedWriter.write("time, caudal");
        simulationBufferedWriter.newLine();
        int i=1;
        for(Vector vector : caudalList){
            simulationBufferedWriter.write(vector.getX() + "," + vector.getY());
            simulationBufferedWriter.newLine();
            i++;
        }
        simulationBufferedWriter.flush();
    }

    public static void printEnergies(Double exitWidth, List<Vector> energies, double kt) throws IOException {
        BufferedWriter simulationBufferedWriter;
        java.io.FileWriter fileWriter = new java.io.FileWriter("energies-" + exitWidth + "-" + kt + ".csv");
        simulationBufferedWriter = new BufferedWriter(fileWriter);
        simulationBufferedWriter.write("time, energy");
        simulationBufferedWriter.newLine();
        int i=1;
        for(Vector vector : energies){
            simulationBufferedWriter.write(vector.getX() + "," + vector.getY());
            simulationBufferedWriter.newLine();
            i++;
        }
        simulationBufferedWriter.flush();
    }

    public static void reset(){
        simulationBufferedWriter = null;
    }
}
