import java.util.ArrayList;
import java.util.Random;

public class sudojava{

    public static void main(String[] args){
        
    }

    private static String makesudoku(){
        /*
        Generates a 81-character-long String that represents the finished Sudoku board, as read from left to right, top to bottom.
        Basically it generates a random first row, and then passes it to the sudosolve method, which generates a finished sudoku board.
        */
        ArrayList<String> nums = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i < 10; i++){
            nums.add(String.valueOf(i));
        }
        String result = "";
        for (int i = 0; i < 9; i++){
            int c = rand.nextInt(nums.size());
            result += nums.get(c);
            nums.remove(c);
        }
        for (int i = 0; i < 72; i++){
            result += "0";
        }
        return sudosolve(result);
    }

    private static Boolean iscomplete(int[][] board){
        /*
        Helper function, takes a 2d array representing a sudoku board as input, determines if the board is solved or not
        the 405 sum (1+2+3+4+5+6+7+8+9)*9 is used for simplicity, because the sudosolve method won't make solving mistakes so no 
        additional checks are necessary
        */
        int sum = 0;
        for (int y =0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                sum += board[y][x];
            }
        }
        if (sum == 405){
            return true;
        }
        else {
            return false;
        }
    }

    private static String sudosolve(String s){
        /*
        Takes a 81-character-long String representing an unfinished Sudoku board (read from left to right, top to bottom), with 0's representing empty spaces
        
        The whole method basically works by branching possibilities: it encounters a cell that has, for example, 3 possible numbers, it appends
        all these possible board states into the 3d array boards, and so on. The method always operates on the last (index -1 in Python) board,
        if it finds that the board is invalid (a cell has no possible numbers that could fit it), this board is deleted and the method operates on 
        the new last (previously second-last) board. This way it eventually gets to the correct board.
        */
        int[][] board = new int[9][9];
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                board[y][x] = s.charAt(y*9+x) - '0';
                
            }
        }
        ArrayList<int[][]> boards = new ArrayList<>();
        boards.add(board);
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                System.out.print(board[y][x]);
            }
            System.out.print("\n");
        }
        System.out.println("\n\n");
        int threshold = 1;
        /*
        The threshold variable is used as follows: it starts at 1, the whole board is scanned and if there are no cells that have only one possible solution,
        threshold increases by one, then the first cell that has an n of possibilities that is <= threshold, all of them are tried and threshold returns to 1
        
        This is done to speed up the program, and emulates a strategy for solving Sudoku in real life; first you check the board for cells that only 
        have one possibility, you fill those, recheck, then if there's nothing else you "lower your standards".
        The key idea here is the rechecking, and that's what the threshold variable is for. As soon as you fill in a cell, it goes back to one, so that first the
        "obvious" cells are filled in, making it easier to fill in others.
        */
        int x = 0;
        int y = 0;
        while (true){
            if (boards.get(boards.size()-1)[y][x] == 0){
                int[] possibles = {0,1,2,3,4,5,6,7,8,9};
                int lastindex = boards.size()-1;
                for (int y2 = 0; y2 < 9; y2++){
                    possibles[boards.get(lastindex)[y2][x]] = 0;
                }
                for (int x2 = 0; x2 < 9; x2++){
                    possibles[boards.get(lastindex)[y][x2]] = 0;
                }
                for (int y2 = (y/3)*3; y2 < (y/3)*3+3; y2++){
                    for (int x2 = (x/3)*3; x2 < (x/3)*3+3; x2++){
                        possibles[boards.get(lastindex)[y2][x2]] = 0;
                    }
                }
                ArrayList<Integer> pos = new ArrayList<>();
                for (int i = 1; i < 10; i++){
                    if (possibles[i] != 0){
                        pos.add(i);
                    }
                }

                if (pos.size() == 0){
                    boards.remove(lastindex);
                    x = -1;
                    y = 0;
                    threshold = 1;
                }
                else if (pos.size() == 1){
                    boards.get(lastindex)[y][x] = pos.get(0);
                    if (iscomplete(boards.get(lastindex))){
                        // it's done
                        String result = "";
                        for (int yy = 0; yy < 9; yy++){
                            for (int xx = 0; xx < 9; xx++){
                                result += String.valueOf(boards.get(lastindex)[yy][xx]);
                            }
                        }
                        return result;
                    }
                    x = -1;
                    y = 0;
                    threshold = 1;
                }
                else if (pos.size() <= threshold){
                    boards.get(lastindex)[y][x] = pos.get(0);
                    for (int p = 1; p < pos.size(); p++){
                        int[][] copy = new int[9][9];
                        for (int yy = 0; yy < 9; yy++){
                            for (int xx = 0; xx < 9; xx++){
                                copy[yy][xx] = boards.get(lastindex)[yy][xx];
                            }
                        }
                        copy[y][x] = pos.get(p);
                        boards.add(copy);
                    }
                    x = -1;
                    y = 0;
                    threshold = 1;
                }
            }
            x++;
            if (x > 8){
                x = 0;
                y++;
            }
            if (y > 8){
                y = 0;
                x = 0;
                threshold++;
            }
        }
        
    }
}