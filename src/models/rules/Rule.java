package models.rules;

import javafx.scene.control.ListCell;
import models.Cell;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jedrek on 05.05.16.
 * Interface for Rules, has a method to validate a cell neighbourhood against a Rule.
 */
public abstract class Rule extends ListCell<Rule> implements Serializable{

    /**
     * Evaluates cell neighbourhood agains a Rule.
     * @param grid
     * @return Returns set outcome if Rule is met, -1 otherwise
     */
    public abstract int evaluate(HashMap<Point, Cell> grid, Cell cell);

    /**
     * Get type of a rule ("advanced" / "simple")
     * @return "advanced" / "simple"
     */
    public abstract String type();

    /**
     * Get Exact Rules corresponding to the rule
     * @return array of Rules Exact
     */
    public abstract RuleExact[] getExacts();

    public abstract String toString();
}
