package models;

import models.rules.RuleSet;

import java.io.Serializable;

/**
 * Created by jedrek on 05.05.16.
 */
public class Cell implements Serializable{
    private int state; //0 = dead, 1 = alive
    private int[][] neighbours;
    private int aliveNeighbours;
    private int x,y;

    public Cell(){
        state =0;
    }
    public Cell(int _state){
        state = _state;
    }
    public int getAliveNeighboursCount() {
        return aliveNeighbours;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int[][] getNeighbours(){
        return neighbours;
    }
    public void setNeighbours(int[][] neighbours) {
        this.neighbours = neighbours;
        aliveNeighbours = 0;
        for(int i=0; i<neighbours.length; i++){
            for (int j = 0; j < neighbours[0].length; j++) {
                if(neighbours[i][j] == 1)
                    if(!(i == 2 && j == 2))
                        aliveNeighbours ++;
            }
        }
    }
    public int getState(){
        return state;
    }
    public void setState(int _state){
        state = _state;
    }

}
