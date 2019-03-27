package GUI;

import MazeCore.Maze;
import MazeCore.Seeker;
import MazeException.MazeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MazeDisplay extends Canvas {

    private Model model;
    private final int width, height;
    private BufferedImage selfPortrait;
    private BufferedImage opponentPortrait;
    private final ArrayList<Seeker> tracks = new ArrayList<>();

    public MazeDisplay(int width, int height, Model model) throws IOException {
        this.width = width;
        this.height = height;
        this.model = model;
        selfPortrait = ImageIO.read(new FileInputStream("D:\\Desktop\\studia\\Informatyka\\Java\\List6b\\src\\res\\He-Man.png"));
        opponentPortrait = ImageIO.read(new FileInputStream("D:\\Desktop\\studia\\Informatyka\\Java\\List6b\\src\\res\\Fisto.png"));

    }

    public void paint(Graphics g)
    {
        Maze maze = model.Maze();
        Graphics2D g2 = (Graphics2D) g;
        int h = maze.ROOMS_H, v = maze.ROOMS_V;
        int hStep = width/h;
        int vStep = height/v;
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,width,height);
        //g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(4));
        //for(int i = 0; i <= h; i++)
        //{
        //    g2.drawLine(i*hStep,0,i*hStep,height);
        //}
        //for(int i = 0; i <= v; i++)
        //{
        //    g2.drawLine(0,i*vStep,width,i*vStep);
        //}
        g2.setColor(Color.GREEN);
        for(int i = 0; i < h; i++)
        {
            for(int j = 0; j < v; j++)
            {
                if(!maze.isConnected(i,j, Maze.dir.down))
                {
                    g2.drawLine(i*hStep,(j+1)*vStep,(i+1)*hStep,(j+1)*vStep);
                }
                if(!maze.isConnected(i,j, Maze.dir.left))
                {
                    g2.drawLine(i*hStep,j*vStep,i*hStep,(j+1)*vStep);
                }
            }
        }
        g2.setColor(Color.RED);
        int gx = maze.GOAL_X, gy = maze.GOAL_Y;
        g2.fillRect(gx*hStep,gy*vStep,hStep,vStep);
        ArrayList<Integer> SeekersIDs = maze.getAllSeekers();
        int x;
        int y;
        try {
            x = maze.getSeekerX(model.ID());
            y = maze.getSeekerY(model.ID());
            g.setColor(Color.BLACK);
            for(Seeker track : tracks)
            {
                g.fillRect((int)track.x * hStep + 2,
                        (int)track.y * vStep + 2,
                        hStep - 4,
                        vStep - 4);
            }
            tracks.clear();
            g.drawImage(selfPortrait,x*hStep+4,y*vStep+2, null);
            tracks.add(new Seeker(x,y));
        } catch (MazeException e) {
            e.printStackTrace();
        }
        for(int id : SeekersIDs)
        {
            try {
                x = maze.getSeekerX(id);
                y = maze.getSeekerY(id);
                tracks.add(new Seeker(x,y));
                g.drawImage(opponentPortrait,x*hStep,y*vStep, null);
            } catch (MazeException e) {
                e.printStackTrace();
            }
        }

    }

    public void swap() {
        BufferedImage tmp = opponentPortrait;
        opponentPortrait = selfPortrait;
        selfPortrait = tmp;
    }
}
