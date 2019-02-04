package com.example.myapplication;

public class SudokuChecker {

    public SudokuChecker(){

    }

    public boolean sudokuCheck(String[][] Sudoku){
      return (checkRow(Sudoku) && checkCol(Sudoku) && checkBox(Sudoku));
    }

    private boolean checkBox(String[][] Sudoku) {
        for (int x = 0; x < 9; x+=3){
            for (int y = 0; y < 9; y+=3){
                //check 3*3 grid
                //row, column should be the start of 3*3 grid
                for (int xBox = 0; xBox < 9; xBox++){
                    for (int yBox = xBox + 1 ; yBox < 9; yBox++){
                        if (Sudoku[x+xBox%3][y+xBox/3].equals(Sudoku[x+yBox%3][y+yBox/3])){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCol(String[][] Sudoku) {
        for (int x = 0; x < 9; x++){
            for (int yPos = 0; yPos < 9; yPos++){
                if (Sudoku[x][yPos] == null || Sudoku[x][yPos] == ""){
                    return false;
                }
                for (int y = yPos + 1; y < 9; y++){
                    if (Sudoku[x][yPos].equals(Sudoku[x][y])){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkRow(String[][] Sudoku) {
        for (int y = 0; y < 9; y++){
            for (int xPos = 0; xPos < 9; xPos++){
                if (Sudoku[xPos][y] == null || Sudoku[xPos][y] == ""){
                    return false;
                }
                for (int x = xPos + 1; x < 9; x++){
                    if (Sudoku[xPos][y].equals(Sudoku[x][y])){
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
