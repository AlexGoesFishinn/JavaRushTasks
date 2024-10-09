package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyFleet {
    private final static int ROWS_COUNT = 3;
    private final static int COLUMNS_COUNT = 10;
    private final static int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet(){
        createShips();
    }

    private void createShips(){
        ships = new ArrayList<EnemyShip>();
        for(int x = 0; x < COLUMNS_COUNT; x++){
            for(int y = 0; y < ROWS_COUNT; y++){
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        Boss boss = new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5);
        ships.add(boss);
    }
    public void draw(Game game){
        for(EnemyShip ship: ships){
            ship.draw(game);
        }
    }
    private double getLeftBorder(){
        double result = SpaceInvadersGame.WIDTH;
        for (EnemyShip ship: ships){
            if(ship.x < result){result = ship.x;}
        }
        return result;
    }

    private double getRightBorder(){
        double result = 0.;
        for (EnemyShip ship: ships){
            if(ship.x + ship.width > result){result = ship.x + ship.width;}
        }
        return result;
    }

    private double getSpeed(){
        return 2.0 < (3.0 / ships.size()) ? 2.0 : (3.0 / ships.size());
    }

    public void move(){
        if(ships.size() > 0){
            if(direction == Direction.LEFT && getLeftBorder() < 0){
                direction = Direction.RIGHT;
                for(EnemyShip ship: ships){
                    ship.move(Direction.DOWN, getSpeed());
                }
            }
            if(direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH){
                direction = Direction.LEFT;
                for(EnemyShip ship: ships){
                    ship.move(Direction.DOWN, getSpeed());
                }
            } else {for(EnemyShip ship: ships){
                ship.move(direction, getSpeed());
            }

            }

        }
    }

    public Bullet fire(Game game){
        if(ships.size() == 0){return null;}
        else{
            if(game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0){
                return null;
            } else{
                return ships.get(game.getRandomNumber(ships.size())).fire();
            }

        }
    }

    public int verifyHit(List<Bullet> bullets){
        if(bullets.size() == 0){return 0;}
        int score = 0;
        for(EnemyShip ship:ships){
            for (Bullet bullet: bullets){
                if(ship.isCollision(bullet)){
                    if(ship.isAlive && bullet.isAlive){
                        ship.kill();
                        bullet.kill();
                        score = score + ship.score;
                    }
                }
            }
        }
        return score;
    }
    public void deleteHiddenShips(){
        Iterator<EnemyShip> shipIterator = ships.iterator();
        while (shipIterator.hasNext()){
            EnemyShip ship = shipIterator.next();
            if(!ship.isVisible()){
                shipIterator.remove();
            }
        }

    }

    public double getBottomBorder(){
        double max = 0.;
        for(EnemyShip ship: ships){
            if(ship.y + ship.height > max){
                max = ship.y + ship.height;
            }
        }
        return  max;
    }

    public int getShipsCount(){
        return ships.size();
    }
}
