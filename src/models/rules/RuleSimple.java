package models.rules;

import models.Cell;
import models.Grid;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jedrek on 05.05.16.
 * Rule of format if (more||less) than (#aliveNeighbours) cells alive then (outcome)
 */
public class RuleSimple extends Rule implements Serializable{
    private int aliveNeighbours; // 0-24
    private String condition; // less ||  more
    private int outcome; // 0 dead, 1 alive
    private int currAliveCount;
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
    protected void updateItem(Rule t, boolean bln) {
        super.updateItem(t, bln);
        if (t != null) {
            setText(t.toString());
        }
    }
    @Override
    public String toString() {
        return "RuleSimple{" +
                "aliveNeighbours=" + aliveNeighbours +
                ", condition='" + condition + '\'' +
                ", outcome=" + outcome +
                '}';
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
        }else{
            localExacts = new RuleExact[24-alive];
            int i=0;
            for(int j=alive+1; j <= 24; j++){
                localExacts[i] = new RuleExact(j, outcome);
                i++;
            }
        }
        return localExacts;
    }
    /**
     * Evaluates an outcome for the set of neighbours
     * @param cellNeighbours
     * @return outcome for the given neighbourhood, -1 if the rule couldn't be applied
     */
    @Override
    public int evaluate(HashMap<Point, Cell> grid, Cell cell) {
        currAliveCount = countAlive(grid, cell);

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
     * @param grid
     * @param cell
     * @return  number of cells alive in the neighbourhood of a cell
     */
    private int countAlive(HashMap<Point, Cell> grid, Cell cell){
        int counter = 0;
        for(int i=-2; i<=2; i++){
            for(int j=-2; j<2; j++){
                if(cell.getX()+i >= Grid.getWidth() || cell.getY() + j >=  Grid.getHeight() || cell.getX()+i < 0 || cell.getY()+j < 0){
                }else {
                    if (!(i == 2 && j == 2)) { //we don't count the middle cell
                        if (grid.get(new Point(cell.getX() + i, cell.getY() + j)).getState() == 1)
                            counter++;
                    }
                }
            }
        }
        return counter;
    }
}
