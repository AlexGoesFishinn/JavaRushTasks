package com.javarush.games.racer;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.*;

public class RacerGame extends Game {
    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    public final static int CENTER_X = WIDTH / 2;
    public final static int ROADSIDE_WIDTH = 14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private  FinishLine finishLine;
    private final static int RACE_GOAL_CARS_COUNT = 40;
    private ProgressBar progressBar;
    private int score;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame(){
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        isGameStopped = false;
        score = 3500;

        drawScene();
        setTurnTimer(40);
    }
    private void drawScene(){
        drawField();

        roadMarking.draw(this);
        roadManager.draw(this);
        player.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
    }
    private void drawField(){

        for(int x = ROADSIDE_WIDTH; x < WIDTH - ROADSIDE_WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                setCellColor(x, y, Color.DIMGREY);
            }
        }
        for(int x = 0; x < ROADSIDE_WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                setCellColor(x, y, Color.GREEN);
            }
        }
        for(int x = WIDTH - ROADSIDE_WIDTH; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                setCellColor(x, y, Color.GREEN);
            }
        }
        for(int y = 0; y < HEIGHT; y++){
            setCellColor(CENTER_X, y, Color.WHITE);
        }


    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT){super.setCellColor(x, y, color);}

    }
    private void moveAll(){
        roadMarking.move(player.speed);
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        player.move();
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onTurn(int step) {
        if(finishLine.isCrossed(player)){
            win();
            drawScene();
        }
        else if(roadManager.checkCrush(player)){
            gameOver();
            drawScene();
        } else{
            roadManager.generateNewRoadObjects(this);
            if(roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT){finishLine.show();}
            moveAll();
            score -= 5;
            setScore(score);
            drawScene();
        }

    }

    @Override
    public void onKeyPress(Key key) {
        if(key == Key.RIGHT){player.setDirection(Direction.RIGHT);}
        if(key == Key.LEFT){player.setDirection(Direction.LEFT);}
        if(key == Key.UP){player.speed = 2;}
        if(isGameStopped && key == Key.SPACE){createGame();}

    }

    @Override
    public void onKeyReleased(Key key) {
        if(key == Key.RIGHT && player.getDirection() == Direction.RIGHT){player.setDirection(Direction.NONE);}
        if(key == Key.LEFT && player.getDirection() == Direction.LEFT){player.setDirection(Direction.NONE);}
        if(key == Key.UP){player.speed = 1;}
    }

    private void gameOver(){
        isGameStopped = true;
        player.stop();
        showMessageDialog(Color.RED,"GAME OVER", Color.WHITE, 75);
        stopTurnTimer();
    }

    private void win(){
        showMessageDialog(Color.GREEN,"WIN",Color.WHITE, 75);
        isGameStopped = true;
        stopTurnTimer();
    }
}
