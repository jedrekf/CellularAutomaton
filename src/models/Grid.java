package models;

import models.rules.RuleSet;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jedrek on 05.05.16.
 */
public class Grid implements Serializable{
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private static int width, height;
    private HashMap<Point, Cell> grid = new HashMap<>();
    private HashMap<Point, Cell> oldGrid;
    private ArrayList gridHistory = new ArrayList<HashMap<Point, Cell>>();
    public Point[][] points;

    public int getState(Point p){
       return grid.get(p).getState();
    }

    /**
     * Changes a state of a cell to it's contradiction
     * @param p Point(x,y) where x, y represent place in a grid
     * @return new Cell state value
     */
    public int toggleState(Point p){
        Cell cell = grid.get(p);
        if(cell.getState()==0){
            cell.setState(1);
            grid.put(p, cell);
            return 1;
        }else{
            cell.setState(0);
            grid.put(p, cell);
            return 0;
        }
    }

    public Grid(int _width, int _height){
        width = _width;
        height = _height;
        points = new Point[width][height];
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                points[i][j] = new Point(i,j);
                grid.put(points[i][j], new Cell(0));
            }
        }
    }

    public void iterate(RuleSet rules){
        oldGrid = new HashMap<Point, Cell>(grid);

        Cell currCell;
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                //currCell.setNeighbours(findNeighbours(i,j));
                //currCell.setState(rules.apply(currCell)); // here the rules are applied to the cell of a grid
                currCell = oldGrid.get(new Point(i,j));
                currCell.setX(i);
                currCell.setY(j);
                currCell = rules.apply(oldGrid, currCell);
                grid.put(points[i][j], currCell);
            }
        }
    }

    public Grid iterate(RuleSet rules, int steps){
        for(int i=0;i<steps;i++){
            iterate(rules);
        }
        return this;
    }

    public Cell[][] findNeighbours(int x, int y){
        Cell[][] neighbours = new Cell[5][5];
        int v=0, w=0;

        for(int i=-2; i <= 2; i++){
            w = 0;
            for(int j=-2; j <= 2; j++){
                if(x+i >= width || y+j >= height || x+i < 0 || y+j < 0){
                    neighbours[v][w] = new Cell();
                }else {
                    if (oldGrid.containsKey(points[x + i][y + j])) {
                        neighbours[v][w] = oldGrid.get(points[x + i][y + j]);
                    } else {
                        neighbours[v][w] = new Cell();
                    }
                }
                w++;
            }
            v++;
        }

        return neighbours;
    }
}
