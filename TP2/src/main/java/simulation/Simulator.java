package simulation;

import cell.Cell;
import grid.impl.Grid2D;
import grid.impl.InitializationGrid2D;
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

    public static void simulate(SimulationOptions opt){
        State state = new State(opt.getDim(), opt.is3D());
        Rule rule = setRule(opt.getRule());

        InitializationGrid2D initializationGrid2D = new InitializationGrid2D(opt.getDim()/4, opt.getN(),100, 100);
        initializationGrid2D.initialize();

        Grid2D grid2D = new Grid2D(opt.getDim());
        grid2D.initialize(initializationGrid2D, state);

        // printState ? --> el inicial



        for(int i=0; i < opt.getIterations() /*TODO: && !exist_border_particle*/; i++){
            state = nextState(state, rule);
        }
    }

    /**
     * Given a state, calculates the following state given a determined rule
     * @param currentState
     * @return
     */
    public static State nextState(State currentState, Rule rule){ // public for testing
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

        logState(currentState);
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

    private static void logState(State state) {
        File file = new File("output.xyz");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
            append = true;
        } catch (FileNotFoundException e) {
            return;
        }
        PrintStream ps = new PrintStream(fos);
        ps.println(state.getAliveCells().size());
        ps.println();
        for (Cell cell : state.getAliveCells()) {
            ps.println(cell);
        }

        ps.close();
    }

}
