package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double boost = 0.05;
    private double speedX = 0;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        List<int[][]> downFireList = new ArrayList<>();
        downFireList.add(ShapeMatrix.FIRE_DOWN_1);
        downFireList.add(ShapeMatrix.FIRE_DOWN_2);
        downFireList.add(ShapeMatrix.FIRE_DOWN_3);

        this.downFire = new RocketFire(downFireList);
        List<int[][]> sideFireList = new ArrayList<>();
        sideFireList.add(ShapeMatrix.FIRE_SIDE_1);
        sideFireList.add(ShapeMatrix.FIRE_SIDE_2);
        this.leftFire = new RocketFire(sideFireList);
        this.rightFire = new RocketFire(sideFireList);
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY = speedY - boost;
        }
        if (!isUpPressed) {
            speedY = speedY + boost;
        }
        this.y = this.y + speedY;
        if (isRightPressed) {
            speedX = speedX + boost;
        }
        if (isLeftPressed) {
            speedX = speedX - boost;
        }
        if(!isLeftPressed && !isRightPressed){
            if(speedX >= -slowdown && speedX <= slowdown ){speedX = 0;}
            if (speedX > slowdown){speedX = speedX - slowdown;}
            if (speedX < -slowdown){speedX = speedX + slowdown;}
        }
        this.x = this.x + speedX;
        /*if (isLeftPressed || isRightPressed) {}*/
        checkBorders();
        switchFire(isUpPressed, isLeftPressed, isRightPressed);

    }

    private void checkBorders() {
        if (this.x < 0) {
            this.x = 0;
            speedX = 0;
        }
        if(this.x + this.width > MoonLanderGame.WIDTH){
            this.x = MoonLanderGame.WIDTH - this.width;
            speedX = 0;
        }
        if(this.y < 0){
            this.y = 0;
            speedY = 0;}
    }
    public boolean isStopped(){
        if(speedY < 10 * boost){return  true;}
        else return false;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height)
                    continue;

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent)
                    return true;
            }
        }
        return false;
    }
    public void land(){
        this.y = this.y - 1;
    }
    public void crash(){
        this.matrix = ShapeMatrix.ROCKET_CRASH;
    }

    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed){
        if(isUpPressed){
            downFire.x = this.x + this.width / 2;
            downFire.y = this.y + this.height;
            downFire.show();
        } else {
            downFire.hide();
        }

        if(isLeftPressed){
            leftFire.x = this.x + this.width;
            leftFire.y = this.y + this.height;
            leftFire.show();
        } else{leftFire.hide();}
        if(isRightPressed){
            rightFire.x = this.x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = this.y + this.height;
            rightFire.show();
        } else{rightFire.hide();}

    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
}
