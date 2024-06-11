package com.server.game.gameutils;

import com.server.player.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public class Board
{
    public static final int ROWS = 3;

    public static final int COLS = 3;

    public Cell[][] cells;

    public Board()
    {
        initGame();
    }

    public void initGame()
    {
        cells = new Cell[ROWS][COLS];  // allocate the array

        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                cells[row][col] = new Cell(row, col);
            }
        }
    }
    public void newGame() {
        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                cells[row][col].newGame();
            }
        }
    }


    public State updateGameState(Symbol player, int selectedRow, int selectedCol) {

        cells[selectedRow][selectedCol].content = player;

        if (hasWon(player, selectedRow, selectedCol)) {
            return (player == Symbol.CROSS) ? State.CROSS_WON : State.ZERO_WON;
        } else if (isBoardFull()) {
            return State.DRAW;
        } else {
            return State.PLAYING;
        }
    }

    private boolean hasWon(Symbol player, int selectedRow, int selectedCol) {
        return (cells[selectedRow][0].content == player &&
                cells[selectedRow][1].content == player &&
                cells[selectedRow][2].content == player) ||
                (cells[0][selectedCol].content == player &&
                        cells[1][selectedCol].content == player &&
                        cells[2][selectedCol].content == player) ||
                (selectedRow == selectedCol &&
                        cells[0][0].content == player &&
                        cells[1][1].content == player &&
                        cells[2][2].content == player) ||
                (selectedRow + selectedCol == 2 &&
                        cells[0][2].content == player &&
                        cells[1][1].content == player &&
                        cells[2][0].content == player);
    }

    private boolean isBoardFull() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Symbol.NO_SYMBOL) {
                    return false;
                }
            }
        }
        return true;
    }

    public void print(CopyOnWriteArrayList<Player> participants) throws InterruptedException
    {
        StringBuilder boardRepresentation = new StringBuilder();

        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                boardRepresentation.append(" ");

                boardRepresentation.append(cells[row][col].content.getIcon());

                boardRepresentation.append(" ");

                if (col < COLS - 1) boardRepresentation.append("|");
            }

            boardRepresentation.append("\n");

            if (row < ROWS - 1)
            {
                boardRepresentation.append("-----------\n");
            }
        };

        String boardString = boardRepresentation.toString();

        participants.forEach(
                (participant) ->
                {
                    participant.writer().println(boardString);

                    participant.writer().flush();
                }
        );
    }
}
