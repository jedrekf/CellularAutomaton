package models.rules;

import models.Cell;

/**
 * Created by jedrek on 05.05.16.
 */
public class RuleAdvanced implements IRule {
    private int cells[][] = new int[5][5];
    private int outcome;
    private RuleExact exact[];

    public int[][] getCells() {
        return cells;
    }
    public int getOutcome() {
        return outcome;
    }
    public RuleExact[] getExacts(){
        return exact;
    }


    /**
     * Creates an Advanced Rule
     * @param alive_neighbours Array of Cells for a rule
     * @param _outcome Outcome of a rule ("alive = 1" / "dead = 0")
     */
    public RuleAdvanced(Cell[][] alive_neighbours, int _outcome) {

        for(int i=0; i<alive_neighbours.length; i++ ){
            for(int j=0; j<alive_neighbours[0].length; j++){
                if(alive_neighbours[i][j].getState() == 1 && (!(i == 2 && j == 2))){ //we don't count the middle cell(current)
                   cells[i][j] = 1;
                }else{
                    cells[i][j] = 0;

                }
            }
        }
        outcome = _outcome;

        exact = generateExact(alive_neighbours);
    }

    /**
     * Generates Exact Rule equivalent for Advanced Rule
     * @param neighbours array of Cell neighbours
     * @return  Exact Rule being the equivalent
     */
    private RuleExact[] generateExact(Cell[][] neighbours){
        RuleExact[] tempexact = new RuleExact[1];
        tempexact[0] = new RuleExact(countAlive(neighbours), outcome);
        return tempexact;
    }

    /**
     * Counts Alive cells excluding the current(middle) cell
     * @param cellNeighbours
     * @return  number of cells alive in the neighbourhood of a cell
     */
    private int countAlive(Cell[][] cellNeighbours){
        int counter = 0;
        for(int i=0; i<cellNeighbours.length; i++ ){
            for(int j=0; j<cellNeighbours[0].length; j++){
                if(cellNeighbours[i][j].getState() == 1 && (!(i == 2 && j == 2))){ //we don't count the middle cell(current)
                    counter++;
                }
            }
        }
        return counter;
    }

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
