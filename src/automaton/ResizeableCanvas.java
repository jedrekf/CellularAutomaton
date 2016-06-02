package automaton;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Grid;

import java.awt.*;

/**
 * Created by Jedrek on 2016-06-01.
 */
class ResizableCanvas extends Canvas {
    private Grid grid;
    private GraphicsContext g;
    private double cellSize=20;
    private double padding = 1;
    public Color dead = new Color(0.9, 0.9, 0.9, 1), alive = Color.BLACK;

    public Grid getGrid() {
        return grid;
    }

    public ResizableCanvas(Grid _grid) {
        grid = _grid;

        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void zoomCanvas(double multiplierX) {
        cellSize+= 4*multiplierX;
        drawGrid(this.grid);
    }

    /**
     * Handle which cell was clicked and change the state
     * @param mouseX Coordinants X of a mouse (inside canvas)
     * @param mouseY Coordinants Y of a mouse (inside canvas)
     * @return updated Grid.
     */
    public Grid clickCell(double mouseX, double mouseY) {
        int x, y;

        x = (int) mouseX / (int) (cellSize + padding);
        y = (int) mouseY / (int) (cellSize + padding);
        if(grid.toggleState(new Point(x,y)) == 0) {
            g.setFill(dead);
        }else{
            g.setFill(alive);
        }
        g.fillRect(x*(cellSize+padding), y*(cellSize+padding), cellSize, cellSize);

        return grid;
    }

    /**
     * Draw initial all dead cells canvas
     */
    private void draw() {
        double width = getWidth();
        double height = getHeight();

        g = getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        g.setFill(dead);
        //g.fillRoundRect(0, 0, width, height, 30, 30);
        for(int x=0; x<width; x+=cellSize + padding){
            for(int y=0; y<height; y+=cellSize + padding){
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
    }

    /**
     * Draw canvas from a grid
     * @param _grid Grid to draw canvas from
     */
    public void drawGrid(Grid _grid){
        grid = _grid;
        double width = getWidth();
        double height = getHeight();

        g = getGraphicsContext2D();
        g.clearRect(0, 0, width, height);

        int i=0, j=0;
        for(int x=0; x<width; x+=cellSize + padding){
            for(int y=0; y<height; y+=cellSize + padding){
                if(grid.getState(new Point(i,j)) == 0){
                    g.setFill(dead);
                }else{
                    g.setFill(alive);
                }

                g.fillRect(x, y, cellSize, cellSize);
                j++;
            }
            i++;
        }
    }


    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }


}