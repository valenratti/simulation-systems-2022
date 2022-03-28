package simulation;

import rule.Rule;
import rule.impl.*;

public class Simulator {

    public static void simulate(SimulationOptions opt){
        State state = new State(opt.getDim(), opt.is3D()); // FIXME
        Rule rule = setRule(opt.getRule());

        // printState ? --> el inicial

        for(int i=0; i < opt.getIterations() /*&& !exist_border_particle*/; i++){
            // TODO: nextState and printState
        }
    }

    /**
     * Given a state, calculates the following state given a determined rule
     * @param currentState
     * @return
     */
    private State nextState(State currentState){
        // TODO. Creo que deberia ser void. Si creamos un nuevo estado por cada iteracion, vamos a estar creando una nueva grid.
    }

    private static Rule setRule(int rule) {
        switch (rule){
            case 1:
                return new RuleA2D(1, 2, 3, 3, 3);
            case 2:
                return new RuleB2D(2, 4, 9, 6, 6);
            case 3:
                return new RuleC2D(1, 3, 3, 2, 3);
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
