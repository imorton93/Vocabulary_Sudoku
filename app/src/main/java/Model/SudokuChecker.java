package Model;

import android.util.Log;

import java.util.ArrayList;

public class SudokuChecker {
    private int gridSize;

    public SudokuChecker(){
    }
    private static final String TAG = "CMPT276-1191E1-Delta";


    public boolean sudokuCheck(String[][] Sudoku, ArrayList<WordsPairs> mPairs){
        this.gridSize = mPairs.size();
    /*    String[] eng_list = new String[gridSize];
        String[] span_list = new String[gridSize];
        for (int i = 0; i < gridSize; i++) {
            eng_list[i] = mPairs.get(i).getENG();
            span_list[i] = mPairs.get(i).getSPAN();
        }*/


        return (checkBox(Sudoku,mPairs));
    }

    private boolean checkRow(String[][] Sudoku, ArrayList<WordsPairs> mPairs) {
        for (int y = 0; y < gridSize; y++){
            for (int xPos = 0; xPos < gridSize; xPos++){
                if (Sudoku[xPos][y] == null || Sudoku[xPos][y].equals("")){
                    return false;
                }
                String eng = null;
                String span = null;
                for (int i = 0; i < gridSize; i++) {
                    if (Sudoku[xPos][y].equals(mPairs.get(i).getENG()) || Sudoku[xPos][y].equals(mPairs.get(i).getSPAN())){
                        eng = mPairs.get(i).getENG();
                        span = mPairs.get(i).getSPAN();
                    }
                }
                for (int x = xPos + 1; x < gridSize; x++){
                    if (Sudoku[xPos][y].equals(Sudoku[x][y]) || Sudoku[x][y].equals(eng) || Sudoku[x][y].equals(span)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCol(String[][] Sudoku,  ArrayList<WordsPairs> mPairs) {
        for (int x = 0; x < gridSize; x++){
            for (int yPos = 0; yPos < gridSize; yPos++){
                if (Sudoku[x][yPos] == null || Sudoku[x][yPos].equals("")){
                    return false;
                }
                String eng = null;
                String span = null;
                for (int i = 0; i < gridSize; i++) {
                    if (Sudoku[x][yPos].equals(mPairs.get(i).getENG()) || Sudoku[x][yPos].equals(mPairs.get(i).getSPAN())){
                        eng = mPairs.get(i).getENG();
                        span = mPairs.get(i).getSPAN();
                    }
                }
                for (int y = yPos + 1; y < gridSize; y++){
                    if (Sudoku[x][yPos].equals(Sudoku[x][y])|| Sudoku[x][y].equals(eng) || Sudoku[x][y].equals(span)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkBox(String[][] Sudoku,  ArrayList<WordsPairs> mPairs) {
        int boxRow = (int)Math.sqrt(gridSize);
        int boxCol = gridSize/boxRow;

        for (int x = 0; x < gridSize; x+=boxRow){
            for (int y = 0; y < gridSize; y+=boxCol){
                //check 3*3 grid
                //row, column should be the start of 3*3 grid
                String eng = null;
                String span = null;
                for (int xBox = 0; xBox < gridSize; xBox++){
                    for (int yBox = xBox + 1 ; yBox < gridSize; yBox++){
                        for (int i = 0; i < gridSize; i++) {
                            if (Sudoku[x+xBox%boxRow][y+xBox/boxRow].equals("")){
                                return false;
                            }
                            if (Sudoku[x+xBox%boxRow][y+xBox/boxRow].equals(mPairs.get(i).getENG()) ||
                                    Sudoku[x+xBox%boxRow][y+xBox/boxRow].equals(mPairs.get(i).getSPAN())){
                                eng = mPairs.get(i).getENG();
                                span = mPairs.get(i).getSPAN();
                            }
                        }
                        if (Sudoku[x+yBox%boxRow][y+yBox/boxRow].equals(eng) || Sudoku[x+yBox%boxRow][y+yBox/boxRow].equals(span)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }
}
