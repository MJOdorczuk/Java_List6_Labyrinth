package MazeCore;

import MazeException.MazeException;
import MazeException.SeekerNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Maze {

    private class Room {
        public boolean up = false, down = false, left = false, right = false;
        public boolean setFlag = false;
    }

    private class Pair<A,B>
    {
        public A x;
        public B y;
        public Pair(A x, B y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public enum dir{up, down, right, left}

    private static class RandomEnum<E extends Enum<E>>
    {
        private static final Random rnd = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token){
            values = token.getEnumConstants();
        }

        public E random(){return values[rnd.nextInt(values.length)];}
    }

    private static final RandomEnum<dir> r =
            new RandomEnum<>(dir.class);

    public final int ROOMS_H = 19;
    public final int ROOMS_V = 19;
    private final int START_X = 0, START_Y = 0;
    public final int GOAL_X, GOAL_Y;
    private final Room[][] map = new Room[ROOMS_H][ROOMS_V];
    private final HashMap<Integer, Seeker> seekers = new HashMap<>();

    public Maze()
    {
        int x = START_X, y = START_Y;
        Random rnd = new Random();
        for(int i = 0; i < ROOMS_H; i++)
        {
            for(int j = 0; j < ROOMS_V; j++)
            {
                Room r = new Room();
                r.down = false;
                r.up = false;
                r.left = false;
                r.right = false;
                map[i][j] = new Room();
            }
        }
        while(!trapped(x,y))
        {
            map[x][y].setFlag = true;
            ArrayList<dir> ds = new ArrayList<>();
            if(x > 0 && !map[x -1][y].setFlag) ds.add(dir.left);
            if(y > 0 && !map[x][y -1].setFlag) ds.add(dir.up);
            if(x < ROOMS_H - 1 && !map[x + 1][y].setFlag) ds.add(dir.right);
            if(y < ROOMS_V - 1 && !map[x][y + 1].setFlag) ds.add(dir.down);
            dir d = ds.get(rnd.nextInt(ds.size()));
            connect(x,y,d,true);
            switch (d) {
                case up:
                    y--;
                    break;
                case down:
                    y++;
                    break;
                case right:
                    x++;
                    break;
                case left:
                    x--;
                    break;
            }
        }
        GOAL_Y = y;
        GOAL_X = x;
        ArrayList<Pair<Integer,Integer>> left = new ArrayList<>();
        for(int i = 0; i < ROOMS_H; i++)
            for (int j = 0; j < ROOMS_V; j++) if (!map[i][j].setFlag) left.add(new Pair<>(i, j));
        while (left.size() > 0)
        {
            Pair<Integer,Integer> v = left.get(rnd.nextInt(left.size()));
            x = v.x;
            y = v.y;
            while (!map[x][y].setFlag)
            {
                map[x][y].setFlag = true;
                ArrayList<dir> ds = new ArrayList<>();
                if(x > 0) ds.add(dir.left);
                if(x < ROOMS_H - 1) ds.add(dir.right);
                if(y > 0) ds.add(dir.up);
                if(y < ROOMS_V - 1) ds.add(dir.down);
                dir d = ds.get(rnd.nextInt(ds.size()));
                connect(x,y,d,true);
                switch (d) {
                    case up:
                        y--;
                        break;
                    case down:
                        y++;
                        break;
                    case right:
                        x++;
                        break;
                    case left:
                        x--;
                        break;
                }
            }
            left.removeIf(p -> map[p.x][p.y].setFlag);
        }
    }

    private boolean trapped(int x, int y) {
        boolean left, right, up, down;
        left = x <= 0 ? true : map[x - 1][y].setFlag;
        right = x >= ROOMS_H - 1 ? true : map[x + 1][y].setFlag;
        up = y <= 0 ? true : map[x][y - 1].setFlag;
        down = y >= ROOMS_V - 1 ? true : map[x][y + 1].setFlag;
        return left && right && up && down;
    }

    private boolean connect(int x, int y, dir d, boolean connect)
    {
        switch (d) {
            case up:
                if(y > 0)
                {
                    map[x][y].up = connect;
                    map[x][y-1].down = connect;
                }
                else return false;
                break;
            case down:
                if(y < ROOMS_V - 1)
                {
                    map[x][y].down = connect;
                    map[x][y+1].up = connect;
                }
                else return false;
                break;
            case right:
                if(x < ROOMS_H - 1)
                {
                    map[x][y].right = connect;
                    map[x+1][y].left = connect;
                }
                else return false;
                break;
            case left:
                if(x > 0)
                {
                    map[x][y].left = connect;
                    map[x-1][y].right = connect;
                }
                else return false;
                break;
        }
        return true;
    }

    public boolean addSeeker(int ID)
    {
        if(seekers.containsKey(ID)) return false;
        seekers.put(ID, new Seeker(START_X,START_Y));
        return true;
    }

    public boolean removeSeeker(int ID)
    {
        return seekers.remove(ID) != null;
    }

    private boolean move(int ID, dir d) throws MazeException {
        Seeker seeker = seekers.get(ID);
        if(seeker == null) throw new SeekerNotFoundException();
        if(!isConnected(seeker.x,seeker.y,d)) return false;
        switch (d) {
            case up:
                if(seeker.y > 0)
                    seeker.y--;
                break;
            case down:
                if(seeker.y < ROOMS_V - 1)
                    seeker.y++;
                break;
            case right:
                if(seeker.x < ROOMS_H - 1)
                    seeker.x++;
                break;
            case left:
                if(seeker.x > 0)
                    seeker.x--;
                break;
        }
        return seeker.x == GOAL_X && seeker.y == GOAL_Y;
    }

    public boolean moveUP(int ID) throws MazeException {
        return move(ID,dir.up);
    }

    public boolean moveDOWN(int ID) throws MazeException {
        return move(ID,dir.down);
    }

    public boolean moveRIGHT(int ID) throws MazeException {
        return move(ID,dir.right);
    }

    public boolean moveLEFT(int ID) throws MazeException {
        return move(ID,dir.left);
    }

    public boolean isConnected(int x, int y, dir d)
    {
        switch (d) {
            case up:
                return map[x][y].up;
            case down:
                return map[x][y].down;
            case right:
                return map[x][y].right;
            case left:
                return map[x][y].left;
            default:
                return false;
        }
    }

    public ArrayList<Integer> getAllSeekers()
    {
        ArrayList<Integer> ret = new ArrayList<>();
        for(Object id : seekers.values())
        {
            if(id instanceof Integer)
            {
                ret.add((Integer) id);
            }
        }
        return ret;
    }

    public int getSeekerX(int ID) throws MazeException {
        if(seekers.containsKey(ID))
        {
            return seekers.get(ID).x;
        }
        else throw new SeekerNotFoundException();
    }

    public int getSeekerY(int ID) throws MazeException{
        if(seekers.containsKey(ID))
        {
            return seekers.get(ID).y;
        }
        else throw new SeekerNotFoundException();
    }
}
