package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private final static int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;


    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        }else if(type == RoadObjectType.DRUNK_CAR){return new MovingCar(x, y);}
        else {return new Car(type, x, y);}
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject obj = createRoadObject(type, x, y);
        if (obj != null && isRoadSpaceFree(obj)) {
            items.add(obj);
        }
    }

    public void draw(Game game) {
        for (RoadObject item : items) {
            item.draw(game);
        }
    }

    public void move(int boost) {
        for (RoadObject item : items) {
            item.move(item.speed + boost, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists() {
        boolean isThornExist = false;
        for (RoadObject item : items) {
            if (item.type == RoadObjectType.THORN) {
                isThornExist = true;
            }
        }
        return isThornExist;
    }

    private void generateThorn(Game game) {
        if (game.getRandomNumber(100) < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {
        Iterator<RoadObject> itemsIterator = items.iterator();
        while (itemsIterator.hasNext()) {
            RoadObject item = itemsIterator.next();
            if (item.y >= RacerGame.HEIGHT) {
                if(item.type != RoadObjectType.THORN){passedCarsCount++;}
                itemsIterator.remove();
            }

        }
    }

    public boolean checkCrush(PlayerCar playerCar) {

        for (RoadObject item : items) {
            if (item.isCollision(playerCar)) {
                return true;
            }
        }
        return false;
    }


    private void generateRegularCar(Game game){

        int carTypeNumber = game.getRandomNumber(4);
        if(game.getRandomNumber(100) < 30){addRoadObject(RoadObjectType.values()[carTypeNumber], game);}
    }

    private boolean isRoadSpaceFree(RoadObject object){
        for (RoadObject item: items){
            if(item.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)){return false;}
        }
        return true;
    }

    private boolean isMovingCarExists(){
        for (RoadObject item: items){
            if(item.type == RoadObjectType.DRUNK_CAR){return true;}
        }
        return false;
    }
    private void generateMovingCar(Game game){
        if(game.getRandomNumber(100) < 10 && !isMovingCarExists()){
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    public int getPassedCarsCount(){
        return passedCarsCount;
    }

}
