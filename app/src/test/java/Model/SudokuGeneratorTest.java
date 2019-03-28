package Model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {
    String[] e = {"a","b","c","d","e","f","g","h","i","j","k","l"};
    String[] s = {"A", "B", "C","D","E","F","G","H","I","J","K","L"};

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

        ArrayList<WordsPairs> list3 = new ArrayList<>();
        for (int i = 0; i < 9; i++) { //Set up list
            list3.add(new WordsPairs(e[i], s[i]));
        }
        String[][] sudoku3 = generator.generateGrid("ENG",list3);
        assertTrue(sudoku_check.sudokuCheck(sudoku3,list3));
        sudoku3 = generator.generateGrid("SPAN",list3);
        assertTrue(sudoku_check.sudokuCheck(sudoku3,list3));

        ArrayList<WordsPairs> list4 = new ArrayList<>();
        for (int i = 0; i < 12; i++) { //Set up list
            list4.add(new WordsPairs(e[i], s[i]));
        }
        String[][] sudoku4 = generator.generateGrid("ENG",list4);
        assertTrue(sudoku_check.sudokuCheck(sudoku4,list4));
        sudoku4 = generator.generateGrid("SPAN",list4);
        assertTrue(sudoku_check.sudokuCheck(sudoku4,list4));

    }

    @Test
    public void nextGrid() {
        //Not needed, as nextGrid is called in generateGrid
    }
}