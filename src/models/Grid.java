package models;

import models.rules.RuleSet;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jedrek on 05.05.16.
 */
public class Grid implements Serializable{

    public Grid(Cell[][] grid) {
        this.grid = grid;
    }
    public static int getWidth() {
        return width;
    }
    public static int getHeight() {
        return height;
    }
    private static int width, height;
    private Cell[][] grid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grid grid1 = (Grid) o;

        return Arrays.deepEquals(grid, grid1.grid);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int getState(int x, int y){
        if(x>= getWidth() || y >= getHeight())
            return -1;
       return grid[x][y].getState();
    }

    /**
     * Toggle state for a cell at X Y
     * @param x col of cell
     * @param y row of cell
     * @return
     */
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

    /**
     * Create a grid
     * @param _width grid width
     * @param _height grod height
     */
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

    /**
     * Clear the grid
     */
    public void clear(){
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Iterate the grid over the rule set
     * @param rules rule set to iterate over
     */
    public void iterate(RuleSet rules){

        Cell[][] oldGrid = cloneArray(grid);
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

    /**
     * Clone Array
     * @param src Source Array to clone
     * @return iterated Grid
     */
    public static Cell[][] cloneArray(Cell[][] src) {
        int length = src.length;
        Cell[][] target = new Cell[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    /**
     * Iterate Grid number of steps
     * @param rules Rules to iterate over
     * @param steps NUmber of steps after which we get the grid
     * @return new iterated Grid
     */
    public Grid iterate(RuleSet rules, int steps){
        for(int i=0;i<steps;i++){
            iterate(rules);
        }
        return this;
    }

    /**
     * Find neighbours of cell
     * @param oldGrid Grid
     * @param x col
     * @param y row
     * @return Get array of neighbours states
     */
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
