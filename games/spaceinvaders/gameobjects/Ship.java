package com.javarush.games.spaceinvaders.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.javarush.engine.cell.Game;

public class Ship extends GameObject{
    public boolean isAlive = true;
    private List<int[][]> frames;
    private int frameIndex;
    private boolean loopAnimation = false;
    public Ship(double x, double y){
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame){
        setMatrix(viewFrame);
        frames = new ArrayList<int[][]>();
        frames.add(viewFrame);
        frameIndex = 0;
    }
    public void setAnimatedView(boolean isLoopAnimation,int[][]... viewFrames){
        setMatrix(viewFrames[0]);
        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
        loopAnimation = isLoopAnimation;
    }

    public void nextFrame(){

        frameIndex++;
        if(frameIndex < frames.size() && loopAnimation == false){

            matrix = frames.get(frameIndex);
        }
        if(frameIndex >= frames.size() && loopAnimation == true){
            frameIndex = 0;
        }

    }

    public Bullet fire(){
        return null;
    }

    public void kill(){
        isAlive = false;
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public boolean isVisible(){
        if(!isAlive && frameIndex >= frames.size()){return false;}
        return true;
    }
}
