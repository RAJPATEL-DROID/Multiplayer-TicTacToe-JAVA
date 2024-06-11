package com.server.game.gameutils;

public class Cell {
        public Symbol content;
        int row, col;

        public Cell(int row, int col)
        {
            this.row = row;

            this.col = col;

            this.content = Symbol.NO_SYMBOL;
        }

        public void newGame()
        {
            this.content = Symbol.NO_SYMBOL;
        }
}

