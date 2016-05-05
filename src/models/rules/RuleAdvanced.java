package models.rules;

import models.Cell;

/**
 * Created by jedrek on 05.05.16.
 */
public class RuleAdvanced implements IRule {
    private int cells[][] = new int[5][5];
    private int outcome;

    /**
     * Evaluates an outcome for the set of neighbours
     * @param cellNeighbours
     * @return outcome for the given neighbourhood, -1 if the rule couldn't be applied
     */
    @Override
    public int evaluate(Cell[][] cellNeighbours) {

        for(int i=0; i<cellNeighbours.length; i++){
            for(int j=0; j<cellNeighbours[0].length; j++){
                if(!(i ==2 && j == 2)) { //we don't count the middle cell
                    if (cellNeighbours[i][j].getState() != cells[i][j])
                        return -1;
                }
            }
        }
        return outcome;
    }

    /**
     * Type of a rule
     * @return Type of a rule
     */
    @Override
    public String type() {
        return "advanced";
    }
}
