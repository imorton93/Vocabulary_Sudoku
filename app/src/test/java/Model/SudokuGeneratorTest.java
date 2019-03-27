package Model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {
    String[] e = {"a","b","c","d","e","f","g","h","i"};
    String[] s = {"A", "B", "C","D","E","F","G","H","I"};

    @Test
    public void generateGrid() {
        SudokuChecker sudoku_check = new SudokuChecker();
        SudokuGenerator generator = new SudokuGenerator();
        ArrayList<WordsPairs> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) { //Set up list
            list.add(new WordsPairs(e[i], s[i]));
        }
        String[][] sudoku = generator.generateGrid("ENG",list);
        assertTrue(sudoku_check.sudokuCheck(sudoku,list));
        sudoku = generator.generateGrid("SPAN",list);
        assertTrue(sudoku_check.sudokuCheck(sudoku,list));

        ArrayList<WordsPairs> list2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) { //Set up list
            list2.add(new WordsPairs(e[i], s[i]));
        }
        String[][] sudoku2 = generator.generateGrid("ENG",list2);
        assertTrue(sudoku_check.sudokuCheck(sudoku2,list2));
        sudoku2 = generator.generateGrid("SPAN",list2);
        assertTrue(sudoku_check.sudokuCheck(sudoku2,list2));

    }

    @Test
    public void nextGrid() {
        //Not needed, as nextGrid is called in generateGrid
    }
}