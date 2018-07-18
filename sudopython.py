import numpy as np
import random

def sudosolve(sudostring="090800300102000070080071002805000010000408000030000209900140050050000106008003040"):
    """
    Takes a 81-character-long String representing an unfinished Sudoku board (read from left to right, top to bottom), with 0's representing empty spaces
    
    the String param has a default value of an unfinished Sudoku puzzle, so you can test the method out without having to look up any Sudoku puzzles

    The whole method basically works by branching possibilities: it encounters a cell that has, for example, 3 possible numbers, it appends
    all these possible board states into the 3d array boards, and so on. The method always operates on the last (index -1 in Python) board,
    if it finds that the board is invalid (a cell has no possible numbers that could fit it), this board is deleted and the method operates on 
    the new last (previously second-last) board. This way it eventually gets to the correct board.
    """
    board = [[] for i in range(9)]
    for i in range(81):
        board[i//9].append(sudostring[i])
    boards = np.array([board],dtype="uint16")
    threshold = 1
    """
    The threshold variable is used as follows: it starts at 1, the whole board is scanned and if there are no cells that have only one possible solution,
    threshold increases by one, then the first cell that has an n of possibilities that is <= threshold, all of them are tried and threshold returns to 1
        
    This is done to speed up the program, and emulates a strategy for solving Sudoku in real life; first you check the board for cells that only 
    have one possibility, you fill those, recheck, then if there's nothing else you "lower your standards".
    The key idea here is the rechecking, and that's what the threshold variable is for. As soon as you fill in a cell, it goes back to one, so that first the
    "obvious" cells are filled in, making it easier to fill in others.
    """
    x,y = 0,0
    while True:
        if boards[-1,y,x] == 0:
            # the cell is empty
            possibles = [1,2,3,4,5,6,7,8,9]
            for n in boards[-1,y,:]:
                # checking the column
                if n in possibles:
                    possibles.remove(n)
            for n in boards[-1,:,x]:
                # checking the row
                if n in possibles:
                    possibles.remove(n)
            for row in boards[-1,(y//3)*3:(y//3)*3+3,(x//3)*3:(x//3)*3+3]:
                # checking the square
                for n in row:
                    if n in possibles:
                        possibles.remove(n)
            
            if len(possibles) == 0:
                # board is invalid
                boards = boards[:-1]
                x,y,threshold = -1,0,1
            elif len(possibles) == 1:
                boards[-1,y,x] = possibles[0]
                if np.sum(boards[-1]) == 405:
                    # checks if the board is solved, 405 because (1+2+3+4+5+6+7+8+9)*9 = 405
                    return boards[-1]
                x,y,threshold = -1,0,1
            elif len(possibles) <= threshold:
                boards[-1,y,x] = possibles[0]
                for p in possibles[1:]:
                    tmp = np.copy(boards[-1])
                    tmp[y,x] = p
                    boards = np.append(boards,[np.copy(tmp)],axis=0)
                x,y,threshold = -1,0,1

        x += 1
        if x > 8:
            x = 0
            y += 1
        if y > 8:
            threshold += 1
            x,y = 0,0          


def makesudoku():
    """
    Generates a 81-character-long String that represents the finished Sudoku board, as read from left to right, top to bottom.
    Basically it generates a random first row, and then passes it to the sudosolve method, which generates a finished sudoku board.
    """
    nums = ["1","2","3","4","5","6","7","8","9"]
    s = ""
    while len(nums) > 0:
        n = random.choice(nums)
        nums.remove(n)
        s += n
    return sudosolve(s+72*"0")