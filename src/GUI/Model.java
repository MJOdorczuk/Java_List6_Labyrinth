package GUI;

import MazeCore.Maze;
import MazeException.MazeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

public class Model {
    private int ID;
    private Maze maze;
    private MazeDisplay mazeDisplay;
    private final JFrame window;
    private final MoveListener moveListener = new MoveListener(this);

    public Model(int width, int height) {
        maze = new Maze();
        ID = new Random().nextInt();
        while(!maze.addSeeker(ID)) ID++;
        try {
            mazeDisplay = new MazeDisplay(width,height,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        window = new JFrame();
        window.setSize(width,height+30);
        window.setTitle("Labyrinth");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.add(mazeDisplay);
        window.addKeyListener(moveListener);
        window.setBackground(Color.BLACK);
    }

    public void moveUp() {
        try {
            if(maze.moveUP(ID))
            {
                restart();
            }
        } catch (MazeException e) {
            e.printStackTrace();
        }
        update();
    }

    private void restart() {
        maze.removeSeeker(ID);
        maze = new Maze();
        while(!maze.addSeeker(ID)) ID++;
    }

    public void moveDown() {
        try {
            if(maze.moveDOWN(ID))
                restart();
        } catch (MazeException e) {
            e.printStackTrace();
        }
        update();
    }

    public void moveLeft() {
        try {
            if(maze.moveLEFT(ID)) restart();
        } catch (MazeException e) {
            e.printStackTrace();
        }
        update();
    }

    public void moveRight() {
        try {
            if(maze.moveRIGHT(ID)) restart();
        } catch (MazeException e) {
            e.printStackTrace();
        }
        update();
    }

    public void update() {
        //window.repaint();
        //mazeDisplay.update(window.getGraphics());
        window.remove(mazeDisplay);
        window.add(mazeDisplay);
    }

    public void stop() {

        window.setVisible(false);
        window.dispose();
        maze.removeSeeker(ID);
    }

    public Maze Maze() {
        return this.maze;
    }

    public int ID(){return this.ID;}

    public void swap() {
        mazeDisplay.swap();
    }
}
