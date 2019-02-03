package com.example.myapplication;


import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {

    public SudokuGenerator() {

    }

    private ArrayList<ArrayList<String>> Words = new ArrayList<ArrayList<String>>();

    String[] sudokuWords = new String[9];

    private Random rand = new Random();

    public String[][] generateGrid(String[] wordsList,char lan) {
        String[][] Sudoku = new String[9][9];

        for (int i = 0; i < 9; i++){
            sudokuWords[i] = wordsList[i];
        }

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
                        Words.get(x).add(sudokuWords[i]);
                    }
                }
            }

            if (Words.get(currentPos).size() > 0 ) {
                int i = rand.nextInt(Words.get(currentPos).size());
                String word;
                word = sudokuWords[i];


                int xPos = currentPos % 9;
                int yPos = currentPos / 9;

                if (!isConflict(Sudoku, xPos, yPos, word)) {
                    Sudoku[xPos][yPos] = word;
                    Words.get(currentPos).remove(i);
                    currentPos++;
                } else {
                    Words.get(currentPos).remove(i);
                }

            } else {
                for (int i = 0; i < 9; i++) {
                    Words.get(currentPos).add(sudokuWords[i]);
                }
                currentPos--;
            }
        }

        return Sudoku;
    }


    private boolean isConflict(String[][] Sudoku, int xPos, int yPos, final String word) {
        if (isRowlConflict(Sudoku, xPos, yPos, word) || isColConflict(Sudoku, xPos, yPos, word) || isBoxConflict(Sudoku, xPos, yPos, word)) {
            return true;
        }
        return false;
    }

    /**
     * Return true if there is a conflict
     *inRow
     * inColumn
     * in3*3Box
     */
    private boolean isRowlConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        for (int x = 0; x < xPos; x++) {
            if (word.equals(Sudoku[x][yPos])) {
                return true;
            }
        }
        return false;
    }

    private boolean isColConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        for (int y = 0; y < yPos; y++) {
            if (word.equals(Sudoku[xPos][y])) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoxConflict(final String[][] Sudoku, final int xPos, final int yPos, final String word) {
        int xBox = xPos - xPos % 3;
        int yBox = yPos -  yPos % 3;

        for (int x = xBox ; x < xBox + 3; x++) {
            for (int y = yBox ; y < yBox + 3; y++) {
                if ((x != xPos || y != yPos) && word.equals(Sudoku[x][y])) {
                    return true;
                }
            }
        }
        return false;
    }

}

