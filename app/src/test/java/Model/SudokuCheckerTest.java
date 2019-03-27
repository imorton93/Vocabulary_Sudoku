package Model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuCheckerTest {
//Only one private function needs to be tested
    @Test
    public void sudokuCheck() {
        SudokuChecker sudoku_check = new SudokuChecker();
        ArrayList<WordsPairs> list = new ArrayList<>();
        String[] e = {"a","b","c","d","e"};
        String[] s = {"A", "B", "C","D","E"};
        String [][] sudoku1 = {
                {"a","b", "c"},
                {"c", "d","e"},
                {"","","c"}};
        //A sudoku with incorrect (empty) inputs
        for (int i = 0; i < 3; i++) { //Set up list
            list.add(new WordsPairs(e[i], s[i]));
        }
        assertFalse( sudoku_check.sudokuCheck(sudoku1,list));
        String [][] sudoku2 = {
                {"a","b", "c"},
                {"c", "d","e"},
                {"b","a","c"}};
        //A sudoku with correct inputs
       // assertFalse( sudoku_check.sudokuCheck(sudoku2,list));
        assertTrue( sudoku_check.sudokuCheck(sudoku2,list));


        String [][] sudoku3 = {{"a","b"}, {"C", "D"}};
        //A sudoku with correct inputs
        ArrayList<WordsPairs> list2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) { //Set up list
            list2.add(new WordsPairs(e[i], s[i]));
        }
        assertTrue( sudoku_check.sudokuCheck(sudoku3,list2));
        String [][] sudoku4 = {{"a","b"}, {"A", "B"}};
      //  assertFalse( sudoku_check.sudokuCheck(sudoku4,list2));
        assertTrue( sudoku_check.sudokuCheck(sudoku4,list2));

        String [][] sudoku5 = {{"a"}};
        //A sudoku with correct inputs
        ArrayList<WordsPairs> list3 = new ArrayList<>();
        for (int i = 0; i < 1; i++) { //Set up list
            list3.add(new WordsPairs(e[i], s[i]));
        }
        assertTrue( sudoku_check.sudokuCheck(sudoku5,list3));

    }
}