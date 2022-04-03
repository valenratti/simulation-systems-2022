package simulation;


import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public class SimulationOptions {

    @Option(name = "-N", usage = "Initial alive cells.", forbids={"-F"}) /*TODO: alive cells?*/
    private Integer n = 1000;

    @Option(name = "-D", usage = "Grid dimension.")
    private Integer dim = 100;

    @Option(name = "-I", usage = "Number of iterations.")
    private Integer iterations = 50;

    @Option(name = "-SD", usage = "Subgrid dimension")
    private Integer subDim = 50;

    @Option(name = "-ast", usage = "All spawn together")
    private Boolean allSpawnTogether = false;

    private boolean is3D;

    @Option(name = "-R", usage = "Rule to be applied. Choose from 1 to 6.")
    private int rule;

    @Option(name = "-F", usage = "Input file.", forbids={"-N"})
    private File inputFile;

    public SimulationOptions(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (inputFile != null && !inputFile.isFile()) {
                throw new IllegalArgumentException("-F is not a valid input file.");
            }
        } catch (CmdLineException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if(rule < 1 || rule > 6)
            throw new IllegalArgumentException("-R is not an existent rule.");

        this.is3D = rule >=4;
    }

    public SimulationOptions(Integer n, Integer dim, Integer subDim, Integer iterations, int rule, Boolean allSpawnTogether) {
        this.n = n;
        this.dim = dim;
        this.subDim = subDim;
        this.iterations = iterations;
        this.rule = rule;
        this.allSpawnTogether = allSpawnTogether;

        if(rule < 1 || rule > 6)
            throw new IllegalArgumentException("-R is not an existent rule.");

        this.is3D = rule >=4;
    }

    public Integer getN() {
        return n;
    }

    public Integer getDim() {
        return dim;
    }

    public Integer getIterations() {
        return iterations;
    }

    public boolean is3D() {
        return is3D;
    }

    public int getRule() {
        return rule;
    }

    public File getInputFile() {
        return inputFile;
    }

    public Integer getSubDim() {
        return subDim;
    }

    public Boolean getAllSpawnTogether() {
        return allSpawnTogether;
    }
}
