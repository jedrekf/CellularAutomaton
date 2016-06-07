package models.rules;

import models.Cell;
import models.Grid;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jedrek on 05.05.16.
 * Rule of format if (more||less||exactly) than (#aliveNeighbours) cells alive then (outcome)
 */
public class RuleSimple extends Rule implements Serializable{
    private int aliveNeighbours; // 0-24
    private String condition; // less ||  more || exactly
    private int outcome; // 0 dead, 1 alive
    private RuleExact exacts[];


    public int getAliveNeighbours() {
        return aliveNeighbours;
    }
    public String getCondition() {
        return condition;
    }
    public int getOutcome() {
        return outcome;
    }
    public RuleExact[] getExacts(){
        return exacts;
    }


    @Override
    public String toString() {
        String text;
        text = "If " + condition + " ";
        if(condition == "exactly"){
            text += aliveNeighbours + " neighbours then the cell is ";
        }else{
            text += "than " + aliveNeighbours + " neighbours then the cell is ";
        }
        if(outcome == 1){
            text += "alive.";
        }else{
            text += "dead.";
        }

        return text;
    }

    /**
     * Creates a Simple Rule
     * @param aliveNeighbours Number of alive neighbours needed for a rule
     * @param condition Condition to be met ("less" than / "more" than)
     * @param outcome Outcome for a Cell (alive = 1 / dead = 0)
     */
    public RuleSimple(int aliveNeighbours, String condition, int outcome) {
        this.aliveNeighbours = aliveNeighbours;
        this.condition = condition;
        this.outcome = outcome;
        exacts = generateExacts(aliveNeighbours);
    }
    /**
     * For generating exact rules as sets equivalent to Simple Rules
     * @param alive number of alive neighbours
     * @return Set of exact rules representing a Simple Rule
     */
    private RuleExact[] generateExacts(int alive){
        RuleExact localExacts[];
        if(condition.compareTo("less") == 0) {
            localExacts = new RuleExact[alive];
            for (int i = 0; i < alive; i++){
                localExacts[i] = new RuleExact(i, outcome);
            }
        }else if(condition.compareTo("more") == 0){
            localExacts = new RuleExact[24-alive];
            int i=0;
            for(int j=alive+1; j <= 24; j++){
                localExacts[i] = new RuleExact(j, outcome);
                i++;
            }
        }else{
            localExacts = new RuleExact[1];
            localExacts[0] = new RuleExact(alive, outcome);
        }
        return localExacts;
    }
    /**
     * Evaluates an outcome for the set of neighbours
     * @param cell
     * @return outcome for the given neighbourhood, -1 if the rule couldn't be applied
     */
    @Override
    public int evaluate(Cell cell) {

        if(condition.compareTo("less") == 0){
            if(cell.getAliveNeighboursCount() < aliveNeighbours)
                return outcome;
        }else if(condition.compareTo("more") == 0){
            if(cell.getAliveNeighboursCount() > aliveNeighbours)
                return outcome;
        }else{
            if(cell.getAliveNeighboursCount() == aliveNeighbours)
                return outcome;
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

}
