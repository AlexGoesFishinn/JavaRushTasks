package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();


    }

    @Override
    public void onKeyPress(Key key) {
        if(isGameStopped){
            if(key == Key.SPACE){
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();

            }
        }
        else {
            if(!canUserMove()){gameOver();}
            else{
                if(key == Key.LEFT){moveLeft();
                    drawScene();}
                else if(key == Key.RIGHT){moveRight();
                    drawScene();}
                else if(key == Key.UP){moveUp();
                    drawScene();}
                else if(key == Key.DOWN){moveDown();
                    drawScene();}
            }
        }

    }

    private void createGame() {
        for(int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                gameField[i][j] = 0;
            }
        }
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048){win();}
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[x][y] != 0) {
            createNewNumber();
        }
        if (getRandomNumber(10) == 9) {
            gameField[x][y] = 4;
        } else gameField[x][y] = 2;
    }

    private Color getColorByValue(int value) {
        Color color = Color.WHITE;
        switch (value) {
            case 2:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.SIENNA;
                break;
            case 8:
                color = Color.ORANGE;
                break;
            case 16:
                color = Color.BISQUE;
                break;
            case 32:
                color = Color.IVORY;
                break;
            case 64:
                color = Color.INDIGO;
                break;
            case 128:
                color = Color.PEACHPUFF;
                break;
            case 256:
                color = Color.HONEYDEW;
                break;
            case 512:
                color = Color.HOTPINK;
                break;
            case 1024:
                color = Color.GOLDENROD;
                break;
            case 2048:
                color = Color.CRIMSON;
                break;
        }
        return color;


    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value == 0) {
            setCellValueEx(x, y, color, "");

        } else setCellValueEx(x, y, color, String.valueOf(value));
    }

    private boolean compressRow(int[] row){
        boolean result = false;
        int index = 0;
        for (int i = 0; i < row.length; i++){
            if(row[i] != 0){
                row[index] = row[i];
                if(index != i){result = true;}
                index++;
            }
        }
        for (int i = index; i < row.length; i++){row[i] = 0;}
        return result;

    }

    private boolean mergeRow(int[] row){
        boolean result = false;
        for (int i = 0; i < row.length - 1; i++){
            if (row[i] == row[i + 1] && row[i] != 0){
                row[i] = row[i] + row[i + 1];
                row[i + 1] = 0;
                score = score + row[i];
                setScore(score);
                result = true;
            }
        }
        return result;

    }
    private void moveLeft(){
        int index = 0;
        for (int j = 0; j < SIDE; j++){
            boolean a = compressRow(gameField[j]);
            boolean b = mergeRow(gameField[j]);
            if (b){compressRow(gameField[j]);}
            if(a || b){index++;}
        }
        if (index != 0){createNewNumber();}

    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp(){
        rotateClockwise();

        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();

        rotateClockwise();
    }

    private void rotateClockwise(){
        int[][] copy = new int[SIDE][SIDE];
        for(int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                copy[i][j] = gameField[SIDE - j - 1][i];
            }
        }
        gameField = copy;
    }

    private int getMaxTileValue(){
        int max = 0;
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                if(gameField[i][j] > max){max = gameField[i][j];}
            }
        }
        return max;
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GREEN,"WIN",Color.WHITE, 75);
    }

    private boolean canUserMove(){
        boolean move = false;
        int countZero = 0;
        int countSame = 0;
        for(int i = 0; i < SIDE; i++){
            for(int j = 0; j < SIDE; j++){
               if (gameField[i][j] == 0){countZero++;}
            }
        }
        if(countZero != 0){move = true;}
        else{
            for (int i = 0; i < SIDE; i++){
                for (int j = 0; j < SIDE - 1; j++){
                    if(gameField[i][j] == gameField[i][j + 1]){
                        countSame++;
                    }
                }
            }
            for (int i = 0; i < SIDE - 1; i++){
                for (int j = 0; j < SIDE; j++){
                    if(gameField[i][j] == gameField[i + 1][j]){
                        countSame++;
                    }
                }
            }
            if (countSame != 0){move = true;}
        }
        return move;
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "GAME OVER", Color.WHITE, 75);
    }


}
