package com.server.game.gameutils;

import com.server.player.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public class Print
{
    public static void initialGameRules(CopyOnWriteArrayList<Player> participants)
    {
        String instructions1 = "Game Board consist of 3*3 grid of cells.\nPlayer will take turns placing their symbol on empty cells";

        String instructions2 = "The first player to get three of their symbols in a row, column, or diagonal wins the game.\nIf all cells are filled and no player has achieved three in a row, the game is a draw.";

        String instructions3 = "Input Format: Enter the row and column numbers separated by a comm(x,y).Here X and Y are in range of (0-2)";

        participants.forEach(player -> {

            player.writer().println(instructions1);

            player.writer().println(instructions2);

            player.writer().println(instructions3);

            player.writer().flush();
        });

        participants.get(0).writer().println("Symbol X is assigned to you for your representation on board");

        participants.get(0).writer().flush();

        participants.get(1).writer().println("Symbol O is assigned to you for your representation on board");

        participants.get(1).writer().flush();
    }

    public static void endingInstructions(CopyOnWriteArrayList<Player> participants)
    {
        String instruction = "Game Over!!Hope You Enjoyed the game,Connect Again to Server for Starting new Game.";

        participants.forEach((participant) ->
        {
            participant.writer().println(instruction);

            participant.writer().flush();
        });
    }


    public static void printBoard(CopyOnWriteArrayList<Player> participants, Board board) throws InterruptedException
    {
        board.print(participants);
    }
}

