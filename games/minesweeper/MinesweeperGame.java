package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles= SIDE * SIDE;
    private int score;


    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();



    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if(isGameStopped){restart();}
        else openTile(x, y);
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }


    private void createGame() {
        countMinesOnField = 0;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (getRandomNumber(10) == 9) {
                    gameField[y][x] = new GameObject(x, y, true);
                    countMinesOnField++;
                } else {
                    gameField[y][x] = new GameObject(x, y, false);
                }

            }
        }
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;


    }

    private void countMineNeighbors() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (gameField[y][x].isMine == false) {
                    gameField[y][x].countMineNeighbors = 0;
                    for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                        if (gameObject.isMine) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }

                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        ArrayList<GameObject> neibors = new ArrayList<>();
        int y = gameObject.y;
        int x = gameObject.x;
        try {
            neibors.add(gameField[y - 1][x - 1]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y - 1][x]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y - 1][x + 1]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y][x + 1]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y + 1][x + 1]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y + 1][x]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y + 1][x - 1]);
        } catch (Exception e) {
        }
        ;
        try {
            neibors.add(gameField[y][x - 1]);
        } catch (Exception e) {
        }
        ;


        return neibors;
    }

    private void openTile(int x, int y) {

        if(gameField[y][x].isOpen){return;}
        else if(isGameStopped){return;}
        else if(gameField[y][x].isFlag){return;}
        else {gameField[y][x].isOpen = true;
            countClosedTiles--;
        setCellColor(x, y, Color.GREEN);
        if (gameField[y][x].isMine == true) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();

        } else if (gameField[y][x].countMineNeighbors == 0) {
            setCellValue(x, y, "");
            score = score + 5;
            for (GameObject gameObject : getNeighbors(gameField[y][x])) {
                if (gameObject.isOpen == false) {
                    openTile(gameObject.x, gameObject.y);
                }
            }

        } else{
            setCellNumber(x, y, gameField[y][x].countMineNeighbors);
            score = score + 5;
        }

        }
        if (!gameField[y][x].isMine && countClosedTiles == countMinesOnField){win();}
        setScore(score);
        //System.out.println(score);
    }

    private void markTile(int x, int y) {
        if (!isGameStopped) {
            if (gameField[y][x].isOpen) {
                return;
            } else if (countFlags == 0 && gameField[y][x].isFlag == false) {
                return;
            } else if (gameField[y][x].isFlag == false) {
                gameField[y][x].isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.YELLOW);
            } else if (gameField[y][x].isFlag == true) {
                gameField[y][x].isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.ORANGE);
            }
        }
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Вы проиграли", Color.RED, 50);
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Вы выиграли", Color.GREEN, 50);
    }

    private void restart(){
        isGameStopped = false;
        countClosedTiles= SIDE * SIDE;
        setScore(score);
        score = 0;
        countMinesOnField = 0;
        countClosedTiles = SIDE * SIDE;
        createGame();
    }

}
