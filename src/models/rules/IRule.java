package models.rules;

import javafx.scene.control.ListCell;
import models.Cell;

/**
 * Created by jedrek on 05.05.16.
 * Interface for Rules, has a method to validate a cell neighbourhood against a Rule.
 */
public abstract class IRule extends ListCell<IRule>{

    /**
     * Evaluates cell neighbourhood agains a Rule.
     * @param cellNeighbours
     * @return Returns set outcome if Rule is met, -1 otherwise
     */
    public abstract int evaluate(Cell[][] cellNeighbours);

    /**
     * Get type of a rule ("advanced" / "simple")
     * @return
     */
    public abstract String type();

    /**
     * Get Exact Rules corresponding to the rule
     * @return array of Rules Exact
     */
    public abstract RuleExact[] getExacts();

    public abstract String toString();
}
