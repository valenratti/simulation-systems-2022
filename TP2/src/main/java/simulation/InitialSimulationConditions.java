package simulation;

import rule.Rule;

public class InitialSimulationConditions {
    private State state;
    private Rule rule;

    public InitialSimulationConditions(State state, Rule rule) {
        this.state = state;
        this.rule = rule;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }


}
