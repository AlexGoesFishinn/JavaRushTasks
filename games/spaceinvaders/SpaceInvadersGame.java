package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public final static int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private final static int PLAYER_BULLETS_MAX = 1;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame(){
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        score = 0;
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene(){

        drawField();
        playerShip.draw(this);
        enemyFleet.draw(this);
        for(Bullet bullet: enemyBullets){
            bullet.draw(this);
        }
        for(Bullet bullet: playerBullets){
            bullet.draw(this);
        }

    }
    private void drawField(){
        for(int x = 0 ; x < HEIGHT; x++){
            for (int y = 0; y < WIDTH; y++){
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for (Star star:stars){
            star.draw(this);
        }

    }

    private void createStars(){
        stars = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            stars.add(new Star(getRandomNumber(64),getRandomNumber(64)));
        }



    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        setScore(score);
        Bullet bullet = enemyFleet.fire(this);
        if(bullet != null){enemyBullets.add(bullet);}
        drawScene();
    }

    private void moveSpaceObjects(){
        enemyFleet.move();
        playerShip.move();
        for(Bullet bullet: enemyBullets){
            bullet.move();
        }
        for(Bullet bullet: playerBullets){
            bullet.move();
        }
    }
    private void removeDeadBullets(){
        Iterator<Bullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()){
            Bullet bullet = bulletIterator.next();
            if(bullet.y >= HEIGHT - 1 || !bullet.isAlive){
                bulletIterator.remove();
            }
        }
        Iterator<Bullet> playerBulletIterator = playerBullets.iterator();
        while (playerBulletIterator.hasNext()){
            Bullet bullet = playerBulletIterator.next();
            if(!bullet.isAlive || bullet.y + bullet.height < 0 ){playerBulletIterator.remove();}
        }
    }

    private void check(){
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        if(enemyFleet.getBottomBorder() >= playerShip.y){
            playerShip.kill();
        }
        if(enemyFleet.getShipsCount() == 0){
            playerShip.win();
            stopGameWithDelay();
        }

        removeDeadBullets();
        if(!playerShip.isAlive){stopGameWithDelay();}
        score = score + enemyFleet.verifyHit(playerBullets);
    }

    private void stopGame(boolean isWin){
        isGameStopped = true;
        stopTurnTimer();
        if(isWin){showMessageDialog(Color.WHITE,"WIN", Color.GREEN, 75);}
        else {showMessageDialog(Color.WHITE,"Game Over", Color.RED, 75);}
    }

    private void stopGameWithDelay(){
        animationsCount++;
        if(animationsCount >= 10){stopGame(playerShip.isAlive);}
    }

    @Override
    public void onKeyPress(Key key) {
        if(isGameStopped && key == Key.SPACE){createGame();}
        if(key == Key.SPACE){
            Bullet bullet = playerShip.fire();
            if(bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX){
                playerBullets.add(bullet);
            }
        }
        if(key == Key.LEFT){playerShip.setDirection(Direction.LEFT);}
        if (key == Key.RIGHT){playerShip.setDirection(Direction.RIGHT);}
    }

    @Override
    public void onKeyReleased(Key key) {
        if(playerShip.getDirection() == Direction.LEFT && key == Key.LEFT){playerShip.setDirection(Direction.UP);}
        if(playerShip.getDirection() == Direction.RIGHT && key == Key.RIGHT){playerShip.setDirection(Direction.UP);}
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT){
            super.setCellValueEx(x, y, cellColor, value);
        }
    }
}
