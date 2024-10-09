package com.javarush.games.racer;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.Arrays;

public class ProgressBar {
    private static final String[] TEXT = {"С", "т", "а", "р", "т","Ф", "и", "н", "и", "ш"};
    private GameObject progressBar;
    private GameObject progressBarField;
    private int maxValue, textLength = 5;

    public ProgressBar(int maxValue) {
        this.maxValue = maxValue;
        int[][] fieldMatrix = createColoredMatrix(1, maxValue, Color.BLACK);
        int[][] indicatorMatrix = createColoredMatrix(1, 1, Color.WHITE);

        int x = RacerGame.WIDTH - 5;
        int y = RacerGame.HEIGHT / 2 - maxValue / 2;
        progressBarField = new GameObject(x, y, fieldMatrix);

        progressBar = new GameObject(x, y + maxValue, indicatorMatrix);
    }

    public void draw(Game game) {
        progressBarField.draw(game);
        progressBar.draw(game);
        drawText(game);
    }
    private void drawText(Game game){
        int xText = progressBarField.x - textLength / 2;
        for(int i = 0; i < textLength; i++){
            game.setCellValueEx(xText + i, progressBarField.y - 1, Color.YELLOW, TEXT[i + textLength], Color.BLACK, 200);
            game.setCellValueEx(xText + i, progressBarField.y + 1 + maxValue, Color.YELLOW, TEXT[i], Color.BLACK, 200);
        }
    }

    public void move(int currentValue) {
        int dy = currentValue < maxValue - 1 ? currentValue : maxValue - 1;
        progressBar.y = progressBarField.y + progressBarField.height - dy - 1;
    }

    private int[][] createColoredMatrix(int width, int height, Color color) {
        int[] line = new int[width];
        Arrays.fill(line, color.ordinal());

        int[][] matrix = new int[height][width];
        Arrays.fill(matrix, line);

        return matrix;
    }
}