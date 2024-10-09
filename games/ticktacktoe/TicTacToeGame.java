package com.javarush.games.ticktacktoe;

import com.javarush.engine.cell.*;

public class TicTacToeGame extends Game {

    private int[][] model = new int[3][3];
    private int currentPlayer;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(3, 3);
        startGame();
        updateView();
    }

    public void startGame(){
        currentPlayer = 1;
        for(int i = 0; i < model.length; i++){
            for (int j = 0; j < model[0].length; j++){
                model[i][j] = 0;
            }
        }
        isGameStopped = false;
    }

    public void updateCellView(int x, int y, int value){
        String text = new String();
        Color color = Color.NONE;
        if(value == 0){text = " ";}
        if(value == 1){
            text = "X";
            color = Color.RED;
        }
        if(value == 2){
            text = "O";
            color = Color.BLUE;
        }

        setCellValueEx(x, y, Color.WHITE, text, color);
    }

    public void updateView(){
        for(int i = 0; i < model.length; i++){
            for (int j = 0; j < model[0].length; j++){
                updateCellView(i, j, model[i][j]);
            }
        }
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if(model[x][y] == 0 && isGameStopped == false){
            model[x][y] = currentPlayer;
            updateView();
            currentPlayer = 3 - currentPlayer;
        } else return;

    }

    public boolean checkWin(int x, int y, int n){
        if(model[x][0] == model[x][1] && model[x][1] == model[x][2] && model[x][2] == n){return true;}
        else if(model[0][y] == model[1][y] && model[1][y] == model[2][y] && model[2][y] == n){return true;}
        else return false;
        /*if(x == 1 && y == 1){
            if(model[0][0] == model[1][1] && model[1][1] == model[2][2]){return true;}
            if(model[0][2] == model[1][1] && model[2][0] == model[2][2]){return true;}
        } else {
            if(model[x][0] == model[x][1] && model[x][1] == model[x][2]){return true;}
            if(model[0][y] == model[1][y] && model[1][y] == model[2][y]){return true;}
        }*/
        //return false;
    }
}