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

    private Cell[][] grid;
    private ArrayList gridHistory = new ArrayList<HashMap<Point, Cell>>();

    public int getState(int x, int y){
        if(x>= getWidth() || y >= getHeight())
            return -1;
       return grid[x][y].getState();
    }

    public int toggleState(int x,int y){
        if(x>= getWidth() || y >= getHeight())
            return -1;

        Cell cell = grid[x][y];
        if(cell.getState()==0){
            cell.setState(1);
            grid[x][y] = cell;
            return 1;
        }else{
            cell.setState(0);
            grid[x][y] = cell;
            return 0;
        }
    }

    public Grid(int _width, int _height){
        width = _width;
        height = _height;
        grid = new Cell[width][height];
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                grid[i][j] = new Cell();
            }
        }
    }

    public void clear(){
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                grid[i][j] = new Cell();
            }
        }
    }

    public void iterate(RuleSet rules){

        //assign to each cell it's neighbours EFFICIENT???
        //updateNeighbours(grid);

        Cell[][] oldGrid = cloneArray(grid); //copy so that the values don't change (IT DOESN"T WORK)
        Cell currCell;
        int neighboursCount;
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                currCell = new Cell(oldGrid[i][j].getState());
                currCell.setNeighbours(findNeighbours(oldGrid, i,j));
                currCell = rules.apply(currCell);
                grid[i][j] = currCell;
            }
        }
    }

    private static Cell[][] cloneArray(Cell[][] src) {
        int length = src.length;
        Cell[][] target = new Cell[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    public Grid iterate(RuleSet rules, int steps){
        for(int i=0;i<steps;i++){
            iterate(rules);
        }
        return this;
    }

    public int[][] findNeighbours(Cell[][] oldGrid, int x, int y){
        int[][] neighbours = new int[5][5];
        int v=0, w=0;

        for(int i=-2; i <= 2; i++){
            w = 0;
            for(int j=-2; j <= 2; j++){
                if(x+i >= width || y+j >= height || x+i < 0 || y+j < 0){
                    neighbours[v][w] = 0;
                }else {
                    neighbours[v][w] = oldGrid[x + i][y + j].getState();
                }
                w++;
            }
            v++;
        }

        return neighbours;
    }
}
