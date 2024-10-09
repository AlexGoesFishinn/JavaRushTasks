package com.javarush.games.snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.javarush.engine.cell.*;
import com.javarush.engine.cell.Color;


public class Snake {
    private List<GameObject> snakeParts = new ArrayList<GameObject>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        GameObject snakePart1 = new GameObject(x, y);
        snakeParts.add(snakePart1);
        GameObject snakePart2 = new GameObject(x + 1, y);
        snakeParts.add(snakePart2);
        GameObject snakePart3 = new GameObject(x + 2, y);
        snakeParts.add(snakePart3);

    }

    public void setDirection(Direction direction) {
        if(this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x){}
        else if(this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x){}
        else if(this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y){}
        else if(this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y){}
        else {if (direction == Direction.LEFT && this.direction != Direction.RIGHT) {
            this.direction = direction;
        } else if (direction == Direction.RIGHT && this.direction != Direction.LEFT) {
            this.direction = direction;
        } else if (direction == Direction.UP && this.direction != Direction.DOWN) {
            this.direction = direction;
        } else if (direction == Direction.DOWN && this.direction != Direction.UP) {
            this.direction = direction;
        }}

    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();

        if (newHead.x < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y < 0 || newHead.y >= SnakeGame.HEIGHT) {
            isAlive = false;
        } else {
            if (checkCollision(newHead)) {
                isAlive = false;

            } else {
                snakeParts.add(0, newHead);

                if (newHead.x != apple.x || newHead.y != apple.y) {
                    removeTail();
                } else apple.isAlive = false;
            }

        }


    }

    public void draw(Game game) {
        for (GameObject snakePart : snakeParts) {
            if (snakeParts.indexOf(snakePart) == 0) {
                if (isAlive) {
                    game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, HEAD_SIGN, Color.BLACK, 75);
                } else game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, HEAD_SIGN, Color.RED, 75);

            } else {
                if (isAlive) {
                    game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, BODY_SIGN, Color.BLACK, 75);
                } else game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, BODY_SIGN, Color.RED, 75);
            }
        }

    }

    public GameObject createNewHead() {
        int x;
        int y;
        if (direction == Direction.LEFT) {
            x = snakeParts.get(0).x - 1;
            y = snakeParts.get(0).y;
        } else if (direction == Direction.RIGHT) {
            x = snakeParts.get(0).x + 1;
            y = snakeParts.get(0).y;
        } else if (direction == Direction.UP) {
            x = snakeParts.get(0).x;
            y = snakeParts.get(0).y - 1;
        } else {
            x = snakeParts.get(0).x;
            y = snakeParts.get(0).y + 1;
        }
        GameObject newHead = new GameObject(x, y);
        return newHead;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject object) {
        boolean result = false;
        for (GameObject snakePart : snakeParts) {
            if (snakePart.x == object.x && snakePart.y == object.y) {
                result = true;
                break;
            }
        }
        return result;
    }

    public int getLength(){
        return snakeParts.size();
    }

}
