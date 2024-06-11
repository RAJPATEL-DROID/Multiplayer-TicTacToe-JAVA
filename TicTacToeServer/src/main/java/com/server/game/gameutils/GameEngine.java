package com.server.game.gameutils;

import com.server.player.Player;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameEngine
{
    public static boolean isGameOver(State gameState)
    {
        return gameState == State.PLAYING;
    }

    public static Player getCurrentPlayer(int currentPlayerIndex, CopyOnWriteArrayList<Player> participants)
    {
        return currentPlayerIndex == 0 ? participants.get(0) : participants.get(1);
    }


    public static String receiveMove(Player currentPlayer) throws IOException,InterruptedException
    {
        String move;

        currentPlayer.writer().println("Player (" + currentPlayer.symbol().getIcon() + "), enter your move (row,col): ");

        currentPlayer.writer().flush();

        move = currentPlayer.reader().readLine();

        return move;
    }

    public static boolean isValidMove(Board board,int row, int col)
    {
        return row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS && board.cells[row][col].content == Symbol.NO_SYMBOL;
    }

    public static void handleGameState(int currentPlayerIndex, State gameState, CopyOnWriteArrayList<Player> participants)
    {
        switch(gameState)
        {
            case PLAYING:
                participants.forEach(participant ->
                {
                    participant.writer().println( participants.get((currentPlayerIndex+1)%2).symbol() + " PLAYING");

                    participant.writer().flush();
                });
                break;

            case DRAW:
                participants.forEach(participant ->
                {
                    participant.writer().println("DRAW");

                    participant.writer().flush();
                });

                break;

            case CROSS_WON:
                participants.forEach(participant ->
                {
                    participant.writer().println("CROSS_WON");

                    participant.writer().flush();
                });

                break;

            case ZERO_WON:
                participants.forEach(participant ->
                {
                    participant.writer().println("ZERO_WON");

                    participant.writer().flush();
                });

                break;

            case STOPPED:
                participants.remove(currentPlayerIndex);

                informOtherUser(participants);
        }
    }

    public static int switchTurn(int currentPlayerIndex)
    {
        return (currentPlayerIndex + 1) % 2;
    }

    public static void informOtherUser(CopyOnWriteArrayList<Player> participants)
    {
        participants.forEach((participant) ->
        {
            participant.writer().println("Game can't be continued.Connect back to server!!");

            participant.writer().flush();
        });
    }
}