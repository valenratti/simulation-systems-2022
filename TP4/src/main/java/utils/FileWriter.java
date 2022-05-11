package utils;

import model.Particle;
import system.particlepropagation.ParticlePropagation;
import system.particlepropagation.State;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileWriter {

    private static BufferedWriter simulationBufferedWriter;
    private static boolean logPositionsAppend = false;

    public static void generateXYZFile(double initialV){
        try{
            java.io.FileWriter fileWriter = new java.io.FileWriter("positions-" + LocalDateTime.now()  + "-" + initialV + ".csv");
            simulationBufferedWriter = new BufferedWriter(fileWriter);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void reset(){
        simulationBufferedWriter = null;
    }

    public static void printParticlePropagation(ParticlePropagation particlePropagation, double initialV) throws IOException {
        if(simulationBufferedWriter == null){
            generateXYZFile(initialV);
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

    public static void printEnergyDiffVsTime(Map<Double, Double> values, double dt) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter("energydiff-" + LocalDateTime.now() + "-dt-" + dt + ".csv");
        BufferedWriter energyDiffVsTimeBufferedWriter = new BufferedWriter(fileWriter);
        energyDiffVsTimeBufferedWriter.write("t, energy_diff");
        energyDiffVsTimeBufferedWriter.newLine();
        for(double key : values.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList())){
            energyDiffVsTimeBufferedWriter.write(key + "," + values.get(key));
            energyDiffVsTimeBufferedWriter.newLine();
        }
        energyDiffVsTimeBufferedWriter.flush();
    }

    public static void printParticleLengthTrajectory(List<Double> averages, List<Double> std, double dt, double vm) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter("lengthtrajectory-" + LocalDateTime.now() + "-vm-" + vm + ".csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("t, average_length, std_length");
        bufferedWriter.newLine();
        for(int i=0; i< averages.size(); i++){
            bufferedWriter.write((double) i * dt + "," + averages.get(i) + "," + std.get(i));
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
    }

    public static void printEndStateResultsByV0(Map<Double, Map<State,Integer>> endStateResults) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter("endState-" + LocalDateTime.now() + ".csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//        bufferedWriter.write("t, average_length, std_length");
        bufferedWriter.newLine();
    }

    public static void printDtVsMse(List<Double> dtList, List<List<Double>> values) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter("./outputs/system1/dt-mse" + ".csv");
        BufferedWriter bw = new BufferedWriter(fileWriter);

        bw.write("dt,beeman,verlet,gpc");
        bw.newLine();

        for(int i = 0; i < dtList.size(); i++){
            bw.write(Utils.fromDoubleListToCsvLine(dtList.get(i), values.get(i), null /* o sino: "%.3e" */));
            bw.newLine();
        }

        bw.flush();
    }

    public static void logPositions(double time, List<Double> positions) {
        File file = new File("./outputs/system1/positions.csv");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file, logPositionsAppend);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        PrintStream ps = new PrintStream(fos);

        if(!logPositionsAppend) {
            logPositionsAppend = true;
            ps.println("t,beeman,verlet,gpc,analytical_solution");
        }

        ps.println(Utils.fromDoubleListToCsvLine(time, positions, null /* o sino: "%.3e" */));

        ps.close();
    }
}
