package automaton;

import automaton.helper.InformBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Cell;
import models.Grid;
import models.rules.RuleSet;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class Visualization {
    private final ResizableCanvas canvas;
    private Grid grid;
    private RuleSet rules;
    private boolean lock = false;
    private final Timeline timeline = new Timeline();
    private KeyFrame keyFrame;
    private double delay;
    private boolean paused = false;
    //private ArrayList gridHistory = new ArrayList<Cell[][]>();
    private boolean oscilating = false;

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public void setRules(RuleSet rules) {
        this.rules = rules;
    }
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Visualization(ResizableCanvas canvas, Grid grid, RuleSet rules) {
        this.canvas = canvas;
        this.grid = grid;
        this.rules = rules;
        this.delay = 500;
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
               keyFrame = new KeyFrame(
                        Duration.millis(delay),
                        event -> {
                            try {
                                step(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                )
        );

    }


    public void start() throws InterruptedException {
        if(!lock) {
            if(!paused){
                //clear the grid history for the current visualzation
                //gridHistory.clear();
                //gridHistory.add(Grid.cloneArray(grid.getGrid()));
                oscilating = false;
                if(timeline.getKeyFrames().contains(keyFrame))
                    timeline.getKeyFrames().remove(keyFrame);

                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.getKeyFrames().add(
                        keyFrame = new KeyFrame(
                                Duration.millis(500),
                                event -> {
                                    try {
                                        step(1);
                                        //////////////////////CHECKING OSCILATION REMOVED //////////////////////
                                        /*if(gridHistory.contains(grid)) {
                                            oscilating = true;
                                            this.stop();
                                        }else{
                                            gridHistory.add(Grid.cloneArray(grid.getGrid()));
                                            if(gridHistory.size() > 3){
                                                gridHistory.remove(0);
                                            }
                                        }*/
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                        )
                );
            }
            timeline.play();
            System.out.println("Play.");
        }
    }

    public synchronized void pause(){
        timeline.pause();
        paused = true;
        System.out.println("Paused.");
    }


    public void step(int steps) throws InterruptedException {
        if(!lock) {
            grid = grid.iterate(rules, steps);
            canvas.drawGrid(grid);
        }
    }
    public synchronized void stop(){
        lock = true;
        timeline.stop();
        if(oscilating) {
            //InformBox.display("Oscilation","Oscilation detected the simulation will now stop.");
            System.out.println("oscilation detected, stopping now.");
        }
        else {
            System.out.println("Stopped. Can't play till you clear the grid");
            //InformBox.display("Visualization stopped.", "Stopped. Can't play till you clear the grid");
        }

    }

    public void unlock(){
        lock = false;
    }
}
