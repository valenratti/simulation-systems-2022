package simulation;

import cell.Cell;
import grid.impl.Grid2D;
import grid.impl.Grid3D;
import grid.impl.InitializationGrid2D;
import grid.impl.InitializationGrid3D;
import rule.Rule;
import rule.impl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Simulator {

    static boolean append = false;
    static double currentAmountPercentage;
    static boolean appendInputVsObservable = false;

    public static void simulate(SimulationOptions opt, double amountPercentage ) throws FileNotFoundException {
        InitialSimulationConditions initialSimulationConditions = initialize(opt);
        currentAmountPercentage = amountPercentage;
        State currentState = initialSimulationConditions.getState();
        File alivePercentageFile = new File( "alivepercentage-" + amountPercentage  +".csv");
        FileOutputStream fos = new FileOutputStream(alivePercentageFile);
        PrintStream ps = new PrintStream(fos);
        ps.println("rule,iteration,alivePercentage");
        ps.println(initialSimulationConditions.getRule().getName() + ",0," + initialSimulationConditions.getState().getAlivePercentage());

        //Initial State Logging
        logState(initialSimulationConditions.getState(), initialSimulationConditions.getRule().getName());
        System.out.println(currentState.getAlivePercentage());
        int i=0;
        for( ; i < opt.getIterations() && currentState.getAliveCount() > 0 && !currentState.stopCriteria(); i++){
            System.out.printf("Iteration n: %s%n", String.valueOf(i));
            currentState = nextState(currentState, initialSimulationConditions.getRule());
            ps.println(initialSimulationConditions.getRule().getName() + "," + String.valueOf(i+1) + "," + currentState.getAlivePercentage());
        }

        writeInputVsObservable(initialSimulationConditions.getRule().getName(), amountPercentage, i);
        System.out.printf("Iterations till end: %s%n", String.valueOf(i));
    }

    /**
     * Given simulation options, initializes the State
     * and Rule to start simulating process
     * @param opt
     * @return
     */
    public static InitialSimulationConditions initialize(SimulationOptions opt){
        State state = new State(opt.getDim(), opt.is3D());
        Rule rule = setRule(opt.getRule());

        if(opt.is3D()){
            int minDim = (int) Math.ceil(Math.cbrt(opt.getN()));
            InitializationGrid3D initializationGrid;

            if(opt.getAllSpawnTogether()){
                //If all spawn together, we will create a block with all particles spawning next to each other
                int a = (opt.getDim() - minDim) / 2;
                initializationGrid = new InitializationGrid3D(minDim, opt.getN(), a, a, a);
                initializationGrid.initialize();
            }else {
                //Otherwise,
                if (opt.getSubDim() == null) {
                    //If the client does not provide a particular sub-dim, we'll
                    //calculate ourselves a dimension which can satisfy the initial particles
                    if (minDim * 2 < opt.getDim()) {
                        minDim = (int) Math.floor(minDim * 2);
                        int a = (opt.getDim() - minDim) / 2;
                        initializationGrid = new InitializationGrid3D(minDim, opt.getN(), a, a, a);
                    } else {
                        //This case spawns all together because there's no more space
                        minDim = (int) Math.floor(opt.getDim() * 0.9);
                        int a = (opt.getDim() - minDim) / 2;
                        initializationGrid = new InitializationGrid3D(minDim, opt.getN(), a, a, a);
                    }
                } else {
                    //Here we use the subdim provided by the user
                    minDim = opt.getSubDim();
                    int a = (opt.getDim() - minDim) / 2;
                    initializationGrid = new InitializationGrid3D(minDim, opt.getN(), a, a, a);
                }
                initializationGrid.initializeRandom();
            }

            Grid3D grid3D = new Grid3D(opt.getDim());
            grid3D.initialize(initializationGrid, state);
        }
        else{
            InitializationGrid2D initializationGrid;
            int minDim = (int) Math.floor(Math.sqrt(opt.getN()));

            if(minDim * 1.5 < opt.getDim()){
                minDim = (int) Math.floor(minDim * 1.5);
                int a = (opt.getDim() - minDim) / 2;
                initializationGrid = new InitializationGrid2D(minDim, opt.getN(), a, a);
                initializationGrid.initializeRandom();
            }else {
                minDim = (int) Math.floor(opt.getDim() * 0.9);
                int a = (opt.getDim() - minDim) / 2;
                initializationGrid = new InitializationGrid2D(minDim, opt.getN(), a, a);
                initializationGrid.initializeRandom();
            }

            Grid2D grid2D = new Grid2D(opt.getDim());
            grid2D.initialize(initializationGrid, state);
        }
        state.setAliveCount(state.getAliveCells().size());
        return new InitialSimulationConditions(state, rule);
    }

    /**
     * Given a state, calculates the following state given a determined rule
     * @param currentState
     * @return
     */
    public static State nextState(State currentState, Rule rule) throws FileNotFoundException { // public for testing
        Collection<Cell> toCheckCells;
        if(currentState.getLastModified().isEmpty()){
            toCheckCells = currentState.getAliveCells();
        }else{
            toCheckCells = currentState.getLastModified();
        }

        List<Cell> modifiedCells = new ArrayList<>();

        for(Cell cell : toCheckCells){
            List<Cell> neighbours = rule.getNeighbours(currentState, cell);

            //Check current cell
            if(!currentState.getCheckedCells().getOrDefault(cell, false)){
                if(rule.applyRule(currentState, cell)){
                    modifiedCells.add(cell);
                }
                currentState.getCheckedCells().put(cell, true);
            }
            //Check neighbours
            for(Cell neighbourCell : neighbours){
                if(!currentState.getCheckedCells().getOrDefault(neighbourCell, false)){
                    if(rule.applyRule(currentState, neighbourCell)){
                        modifiedCells.add(neighbourCell);
                    }
                    currentState.getCheckedCells().put(neighbourCell, true);
                }
            }
        }
        currentState.applyChanges(modifiedCells);

        logState(currentState, rule.getName());
        return currentState;
    }

    private static Rule setRule(int rule) {
        switch (rule){
            case 1:
                return new RuleA2D();
            case 2:
                return new RuleB2D();
            case 3:
                return new RuleC2D();
            case 4:
                return new RuleA3D();
            case 5:
                return new RuleB3D();
            case 6:
                return new RuleC3D();
            default: // shouldn't happen
                throw new IllegalArgumentException("Rule parameter cause an error.");
        }
    }

    private static void logState(State state, String outputFileName) throws FileNotFoundException {
        File file = new File(outputFileName + '-' + currentAmountPercentage + '%' + ".xyz");
        FileOutputStream fos;

        fos = new FileOutputStream(file, append);
        append = true;


        PrintStream ps = new PrintStream(fos);

        ps.println(state.getAliveCells().size());
        ps.println("R: " + state.getPatternRadius());
        for (Cell cell : state.getAliveCells())
            ps.println(cell);
        ps.close();
    }

    private static void writeInputVsObservable(String ruleName, double initialPercentage, int iterationsCount) throws FileNotFoundException {
        File file = new File( "inputvsobservable.csv");
        FileOutputStream fos;
        if(!appendInputVsObservable) {
            fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            ps.println("rule,initial%,iterations");
            ps.println(ruleName + "," + initialPercentage + "," + iterationsCount);
            appendInputVsObservable = true;
            ps.close();
        }else {
            fos = new FileOutputStream(file, true);
            PrintStream ps = new PrintStream(fos);
            ps.println(ruleName + "," + initialPercentage + "," + iterationsCount);
            ps.close();
        }
    }

    private static void writeAlivePercentage(String ruleName, double initialPercentage, int iterationsCount) throws FileNotFoundException {
        File file = new File( "inputvsobservable.csv");
        FileOutputStream fos;
        if(!appendInputVsObservable) {
            fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            ps.println("rule,iteration,alivePercentage");
            ps.println(ruleName + "," + initialPercentage + "," + iterationsCount);
            appendInputVsObservable = true;
            ps.close();
        }else {
            fos = new FileOutputStream(file, true);
            PrintStream ps = new PrintStream(fos);
            ps.println(ruleName + "," + initialPercentage + "," + iterationsCount);
            ps.close();
        }
    }

}
