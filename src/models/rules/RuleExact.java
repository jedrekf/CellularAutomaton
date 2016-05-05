package models.rules;

/**
 * Created by jedrek on 05.05.16.
 * A rule that RuleSimple consists of it's of type if exactly (#neighbours) then (outcome)
 */
public class RuleExact {
    private int aliveNeighbours;
    private int outcome;

    public int getAliveNeighbours() {
        return aliveNeighbours;
    }

    public int getOutcome() {
        return outcome;
    }

    public RuleExact(int aliveNeighbours, int outcome) {
        this.aliveNeighbours = aliveNeighbours;
        this.outcome = outcome;
    }
}
