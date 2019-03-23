package Model;

import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    private int gridSize;

    public SudokuGenerator() {

    }

    public String[][] generateGrid(String msg, ArrayList<WordsPairs> mPairs){
        this.gridSize = mPairs.size();
        int i = 0;
        String[] wordsList = new String[gridSize];
        switch (msg) {
            case "SPAN":
                for (i = 0; i < mPairs.size(); i++) {
                    wordsList[i] = mPairs.get(i).getENG();
                }
                break;
            case "ENG":
                for (i = 0; i < mPairs.size(); i++) {
                    wordsList[i] = mPairs.get(i).getSPAN();
                }
                break;
        }


        String[][] Sudoku = new String[gridSize][gridSize];
        nextGrid(Sudoku,0,0, wordsList);
        return Sudoku;
    }


    /*
     * Recursive method that place String in the grid.
     *
     * x value of the current cell
     * y value of the current cell
     *return  true if the grid is complete without conflicts
     */
    public boolean nextGrid(String[][] Sudoku, int xPos, int yPos, String[] wordList)
    {
        int X = xPos;  //x value of current cell
        int Y = yPos;  //y value of the current cell
        Random rand = new Random();
        String tmp = null;
        int current = 0;

        for(int i= wordList.length - 1;i > 0;i--) {
            current = rand.nextInt(i);
            tmp = wordList[current];
            wordList[current] = wordList[i];
            wordList[i] = tmp;
        }

        for(int i = 0;i < wordList.length;i++) {
            if(!isConflict(Sudoku,xPos, yPos, wordList[i])) {
                Sudoku[xPos][yPos] = wordList[i];
                if(xPos == gridSize-1) {
                    if(yPos == gridSize-1)
                        return true; //all done
                    else {
                        X = 0;
                        Y = yPos + 1;   // change a row
                    }
                }
                else {
                    X = xPos + 1;   //change a column
                }
                if(nextGrid(Sudoku,X, Y,wordList))
                    return true;
            }
        }
        Sudoku[xPos][yPos] = null;
        return false;
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
        int boxRow = (int)Math.sqrt(gridSize);
        int boxCol = gridSize/boxRow;
        int xBox = xPos - xPos % boxRow;
        int yBox = yPos -  yPos % boxCol;

        for (int x = xBox ; x < xBox + boxRow; x++) {
            for (int y = yBox ; y < yBox + boxCol; y++) {
                if ((x != xPos || y != yPos) && word.equals(Sudoku[x][y])) {
                    return true;
                }
            }
        }
        return false;
    }


}

