package simulation;

import cell.Cell;
import grid.impl.Grid2D;
import grid.impl.InitializationGrid2D;
import rule.Rule;
import rule.impl.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Simulator {

    public static void simulate(SimulationOptions opt){
        State state = new State(opt.getDim(), opt.is3D());
        Rule rule = setRule(opt.getRule());

        InitializationGrid2D initializationGrid2D = new InitializationGrid2D(opt.getDim()/2, opt.getN(),100, 100);
        initializationGrid2D.initialize();

        Grid2D grid2D = new Grid2D(opt.getDim());
        grid2D.initialize(initializationGrid2D, state);

        // printState ? --> el inicial



        for(int i=0; i < opt.getIterations() /*&& !exist_border_particle*/; i++){
            state = nextState(state, rule);
        }
    }

    /**
     * Given a state, calculates the following state given a determined rule
     * @param currentState
     * @return
     */
    private static State nextState(State currentState, Rule rule){
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
        System.out.println(currentState.getAliveCells().size());
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
                return new RuleA3D(1, 2, 3, 3, 3); // FIXME: Check 3D rules
            case 5:
                return new RuleB3D(2, 4, 9, 6, 6); // FIXME: Check 3D rules
            case 6:
                return new RuleC3D(1, 3, 3, 2, 3); // FIXME: Check 3D rules
            default: // shouldn't happen
                throw new IllegalArgumentException("Rule parameter cause an error.");
        }
    }

}
