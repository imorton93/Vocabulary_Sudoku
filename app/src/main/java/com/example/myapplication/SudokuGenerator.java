package com.example.myapplication;

import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {

    private static SudokuGenerator initialGame;

    private ArrayList<ArrayList<String>> Words = new ArrayList<ArrayList<String>>();

    String[] wordsEng = {"cat", "dog", "girl", "boy", "parrot", "sad", "happy", "father", "mother"};
    String[] wordsSpa = {"gato", "perro", "niña", "niño", "loro", "triste", "feliz", "padre", "madre"};

    private Random rand = new Random();

    private SudokuGenerator() {
    }

    public static SudokuGenerator getInitialGame() {
        if (initialGame == null) {
            initialGame = new SudokuGenerator();
        }
        return initialGame;
    }

    public String[][] generateGrid() {
        String[][] Sudoku = new String[9][9];

        int currentPos = 0;

        while (currentPos < 81) {
            if (currentPos == 0) {
                Words.clear();

                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        Sudoku[x][y] = null;
                    }
                }

                for (int x = 0; x < 81; x++) {
                    Words.add(new ArrayList<String>());
                    for (int i = 0; i < 9; i++) {
                        Words.get(x).add(wordsEng[i]);
                    }
                }
            }

            if (Words.get(currentPos).size() > 0 ) {
                int i = rand.nextInt(Words.get(currentPos).size());
                String word = wordsEng[i];

                if (!isConflict(Sudoku, currentPos, word)) {
                    int xPos = currentPos % 9;
                    int yPos = currentPos / 9;

                    Sudoku[xPos][yPos] = word;

                    Words.get(currentPos).remove(i);

                    currentPos++;
                } else {
                    Words.get(currentPos).remove(i);
                }

            } else {
                for (int i = 0; i < 9; i++) {
                    Words.get(currentPos).add(wordsEng[i]);
                }
                currentPos--;
            }
        }

        return Sudoku;
    }


    private boolean isConflict(String[][] Sudoku, int currentPos, final String word) {
        int xPos = currentPos % 9;
        int yPos = currentPos / 9;

        if (isRowlConflict(Sudoku, xPos, yPos, word) || isColConflict(Sudoku, xPos, yPos, word) || isBlockConflict(Sudoku, xPos, yPos, word)) {
            return true;
        }

        return false;
    }

    /**
     * Return true if there is a conflict
     *
     */
    private boolean isRowlConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        for (int x = xPos - 1; x >= 0; x--) {
            if (word == Sudoku[x][yPos]) {
                return true;
            }
        }

        return false;
    }

    private boolean isColConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        for (int y = yPos - 1; y >= 0; y--) {
            if (word == Sudoku[xPos][y]) {
                return true;
            }
        }

        return false;
    }

    private boolean isBlockConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        int xBlock = xPos / 3;
        int yBlock = yPos / 3;

        for (int x = xBlock * 3; x < xBlock * 3 + 3; x++) {
            for (int y = yBlock * 3; y < yBlock * 3 + 3; y++) {
                if ((x != xPos || y != yPos) && word == Sudoku[x][y]) {
                    return true;
                }
            }
        }

        return false;
    }

}