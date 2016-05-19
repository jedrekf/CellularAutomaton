package models;

import models.rules.RuleSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jedrek on 05.05.16.
 */
public class Grid {
    private int width, height;
    private HashMap<Point, Cell> grid = new HashMap<>();
    private HashMap<Point, Cell> oldGrid;
    private ArrayList gridHistory = new ArrayList<HashMap<Point, Cell>>();
    public Point[][] points;

    public Grid(int _width, int _height){
        width = _width;
        height = _height;

        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                points[i][j] = new Point(i,j);
                grid.put(points[i][j], new Cell());
            }
        }
    }

    public void iterate(RuleSet rules){
        oldGrid = grid;
        Cell currCell = new Cell();
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                currCell.setNeighbours(findNeighbours(i,j));
                currCell.setState(rules.apply(currCell)); // here the rules are applied to the cell of a grid
                grid.put(points[i][j], currCell);
            }
        }
    }

    public void iterate(RuleSet rules, int steps){
        for(int i=0;i<steps;i++){
            iterate(rules);
        }
    }

    public Cell[][] findNeighbours(int x, int y){
        Cell[][] neighbours = new Cell[5][5];
        int v=0, w=0;

        for(int i=-2; i <= 2; i++){
            for(int j=-2; j <= 2; j++){
                if(oldGrid.containsKey(points[x+i][y+j])) {
                    neighbours[v][w] = oldGrid.get(points[x + i][y + j]);
                }else{
                    neighbours[v][w] = new Cell();
                }
                w++;
            }
            v++;
        }

        return neighbours;
    }
}
