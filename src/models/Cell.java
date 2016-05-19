package models;

/**
 * Created by jedrek on 05.05.16.
 */
public class Cell {
    private int state; //0 = dead, 1 = alive
    Cell[][] neighbours;


    public Cell(){
        state =0;
    }
    public Cell(int _state){
        state = _state;
    }

    public Cell[][] getNeighbours(){
        return neighbours;
    }
    public void setNeighbours(Cell[][] neighbours) {
        this.neighbours = neighbours;
    }
    public int getState(){
        return state;
    }
    public void setState(int _state){
        state = _state;
    }

}
