package automaton;

import models.Grid;
import models.rules.RuleSet;

import java.io.Serializable;

/**
 * Created by jedrek on 02.06.16.
 */
public class AutomatonState implements Serializable{
    public Grid grid ;
    public RuleSet rules;

    public AutomatonState(Grid grid, RuleSet rules) {
        this.grid = grid;
        this.rules = rules;
    }
}
