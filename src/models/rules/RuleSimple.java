package models.rules;

import models.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jedrek on 05.05.16.
 * Rule of format if (more||less) than (#aliveNeighbours) cells alive then (outcome)
 */
public class RuleSimple implements IRule {
    private int aliveNeighbours; // 0-24
    private String condition; // less ||  more
    private int outcome; // 0 dead, 1 alive
    private int currAliveCount;
    private RuleExact exacts[];

    public RuleExact[] getExacts(){
        return exacts;
    }

    public RuleSimple(int aliveNeighbours, String condition, int outcome) {
        this.aliveNeighbours = aliveNeighbours;
        this.condition = condition;
        this.outcome = outcome;
        exacts = generateExacts();
    }

    private RuleExact[] generateExacts(){
        RuleExact localExacts[] = new RuleExact[aliveNeighbours];
        if(condition.compareTo("less") == 0) {
            for (int i = 0; i < aliveNeighbours; i++){
                localExacts[i] = new RuleExact(i, outcome);
            }
        }else{
            for(int j=aliveNeighbours; j < 24; j++){}

        }
        return null;
    }

    public int getAliveNeighbours() {
        return aliveNeighbours;
    }

    public String getCondition() {
        return condition;
    }

    public int getOutcome() {
        return outcome;
    }

    /**
     * Evaluates an outcome for the set of neighbours
     * @param cellNeighbours
     * @return outcome for the given neighbourhood, -1 if the rule couldn't be applied
     */
    @Override
    public int evaluate(Cell[][] cellNeighbours) {
        currAliveCount = countAlive(cellNeighbours);

        if(condition.compareTo("less") == 0){
            if(currAliveCount < aliveNeighbours)
                return outcome;
        }else if(condition.compareTo("more") == 0){
            if(currAliveCount > aliveNeighbours)
                return outcome;
        }else{
            System.out.println("undefined rule");
        }

        return -1; // the rule wasn't applied
    }

    /**
     * Type of a rule
     * @return Type of a rule
     */
    @Override
    public String type() {
        return "simple";
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
}
