package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class GameObject {
    public double x;
    public double y;
    public int[][] matrix;
    public int width;
    public int height;

    public GameObject(double x, double y, int[][] matrix) {
        this.x = x;
        this.y = y;
        this.matrix = matrix;
        this.height = matrix.length;
        this.width = matrix[0].length;
    }

    public void draw(Game game) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++){
                if(matrix[i][j] != 0){game.setCellColor((int)this.x + j, (int)this.y + i,  Color.values()[matrix[i][j]]);}

            }
        }
    }
}
