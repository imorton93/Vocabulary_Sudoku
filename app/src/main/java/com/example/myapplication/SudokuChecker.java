package com.example.myapplication;

import android.widget.Toast;

public class SudokuChecker {

    public SudokuChecker(){
    }


    public boolean sudokuCheck(String[][] Sudoku, String[] eng_list, String[] span_list){
      return (checkRow(Sudoku, eng_list, span_list) || checkCol(Sudoku, eng_list, span_list) || checkBox(Sudoku, eng_list, span_list) );
    }

    private boolean checkBox(String[][] Sudoku, String[] eng_list, String[] span_list) {
        for (int x = 0; x < 9; x+=3){
            for (int y = 0; y < 9; y+=3){
                //check 3*3 grid
                //row, column should be the start of 3*3 grid
                String eng = null;
                String span = null;
                for (int xBox = 0; xBox < 9; xBox++){
                    for (int yBox = xBox + 1 ; yBox < 9; yBox++){
                        for (int i = 0; i < 9; i++) {
                            if (Sudoku[x+xBox%3][y+xBox/3].equals(eng_list[i]) || Sudoku[x+xBox%3][y+xBox/3].equals(span_list[i])){
                                eng = eng_list[i];
                                span = span_list[i];
                            }
                        }
                        if (Sudoku[x+xBox%3][y+xBox/3].equals(Sudoku[x+yBox%3][y+yBox/3]) || Sudoku[x+yBox%3][y+yBox/3].equals(eng) || Sudoku[x+yBox%3][y+yBox/3].equals(span)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCol(String[][] Sudoku, String[] eng_list, String[] span_list) {
        for (int x = 0; x < 9; x++){
            for (int yPos = 0; yPos < 9; yPos++){
                if (Sudoku[x][yPos] == null || Sudoku[x][yPos].equals("")){
                    return false;
                }
                String eng = null;
                String span = null;
                for (int i = 0; i < 9; i++) {
                    if (Sudoku[x][yPos].equals(eng_list[i]) || Sudoku[x][yPos].equals(span_list[i])){
                        eng = eng_list[i];
                        span = span_list[i];
                    }
                }
                for (int y = yPos + 1; y < 9; y++){
                    if (Sudoku[x][yPos].equals(Sudoku[x][y])|| Sudoku[x][y].equals(eng) || Sudoku[x][y].equals(span)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkRow(String[][] Sudoku, String[] eng_list, String[] span_list) {
        for (int y = 0; y < 9; y++){
            for (int xPos = 0; xPos < 9; xPos++){
                if (Sudoku[xPos][y] == null || Sudoku[xPos][y].equals("")){

                    return false;
                }
                String eng = null;
                String span = null;
                for (int i = 0; i < 9; i++) {
                    if (Sudoku[xPos][y].equals(eng_list[i]) || Sudoku[xPos][y].equals(span_list[i])){
                        eng = eng_list[i];
                        span = span_list[i];
                    }
                }
                for (int x = xPos + 1; x < 9; x++){
                    if (Sudoku[xPos][y].equals(Sudoku[x][y]) || Sudoku[x][y].equals(eng) || Sudoku[x][y].equals(span)){

                        return false;
                    }
                }
            }
        }
        return true;
    }


}
