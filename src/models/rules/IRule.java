package models.rules;

import models.Cell;

/**
 * Created by jedrek on 05.05.16.
 * Interface for Rules, has a method to validate a cell neighbourhood against a Rule.
 */
public interface IRule {

    /**
     * Evaluates cell neighbourhood agains a Rule.
     * @param cellNeighbours
     * @return Returns set outcome if Rule is met, -1 otherwise
     */
    public int evaluate(Cell[][] cellNeighbours);
    public String type();

}
