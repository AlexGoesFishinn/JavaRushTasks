package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Bullet extends GameObject {
    private int dy;
    public boolean isAlive = true;
    public Bullet(double x, double y, Direction direction){
        super(x, y);
        setMatrix(ShapeMatrix.BULLET);
        if(direction == Direction.UP){dy = -1;}
        else {dy = 1;}
    }

    public void move(){
        y += dy;
    }
    public void kill(){
        isAlive = false;
    }
}
