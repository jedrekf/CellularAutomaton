package models.rules;

import java.io.Serializable;

/**
 * Created by jedrek on 05.05.16.
 * A rule that RuleSimple consists of it's of type if exactly (#neighbours) then (outcome)
 */
public class RuleExact implements Serializable{
    private int aliveNeighbours;
    private int outcome;

    public int getAliveNeighbours() {
        return aliveNeighbours;
    }
    public int getOutcome() {
        return outcome;
    }

    /**
     * Exact rule
     * @param aliveNeighbours Alive neighbours for the rule to be applied
     * @param outcome Outcome for the rule
     */
    public RuleExact(int aliveNeighbours, int outcome) {
        this.aliveNeighbours = aliveNeighbours;
        this.outcome = outcome;
    }
}
