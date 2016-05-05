package models;

/**
 * Created by jedrek on 05.05.16.
 */
public class Cell {
    private int state = 0; //0 = dead, 1 = alive
    Cell[][] neighbours;


    public int getState(){
        return state;
    }
    public void setState(int _state){
        state = _state;
    }
    public Cell[][] getNeighbours(){
        return neighbours;
    }
}
