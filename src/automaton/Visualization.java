package automaton;

import models.Grid;
import models.rules.RuleSet;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class Visualization {
    private final ResizableCanvas canvas;
    private Grid grid;
    private RuleSet rules;
    private boolean flag;
    private int steps;


    public void setRules(RuleSet rules) {
        this.rules = rules;
    }
    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public Visualization(ResizableCanvas canvas, Grid grid, RuleSet rules, int steps) {
        this.canvas = canvas;
        this.grid = grid;
        this.rules = rules;
        this.steps = steps;
    }


    public void start() throws InterruptedException {
        flag = true;
        while(flag){
            grid = grid.iterate(rules, steps);
            Thread.sleep(200);
            canvas.drawGrid(grid);
            System.out.println("redrawn");
        }
    }

    public void pause(){
        flag = false;
    }


    public void step(int steps) {
        grid = grid.iterate(rules, steps);
        canvas.drawGrid(grid);
    }
    public void stop(){
        flag = false;
    }

    
}
