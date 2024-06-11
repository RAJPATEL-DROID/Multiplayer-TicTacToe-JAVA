package com.server.game;

import com.server.game.gameutils.*;
import com.server.player.Player;
import com.server.utils.LoggingUtils;
import com.server.game.gameutils.State;


import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Game
{
    private static final Logger logger = LoggingUtils.getGameLogger();

    private int currentPlayerIndex;

    private final Board board;

    private State gameState;

    private final Lock gameLock = new ReentrantLock();

    private final Condition player1Turn = gameLock.newCondition();

    private final Condition player2Turn = gameLock.newCondition();

    private final AtomicBoolean instructionsSent = new AtomicBoolean(false);

    private final CopyOnWriteArrayList<Player> participants = new CopyOnWriteArrayList<>();

    private final AtomicReference<Player> currentPlayer = new AtomicReference<>();

    private String player1ThreadName;

    public Game()
    {
        this.currentPlayerIndex = 0;

        this.board = new Board();

        board.newGame();

        this.gameState = State.PLAYING;
    }


    public void addPlayerAndStartGaming(Player player)
    {
        try {
            participants.add(player);

            gameLock.lock();

            if (participants.size() == 1)
            {
                player1ThreadName = Thread.currentThread().getName();

                logger.info("Player 1 is on Thread : " + Thread.currentThread().getName());

                while (participants.size() < 2)
                {
                    player1Turn.await();
                }
            }
            else
            {
                logger.info("Player 2 is on Thread : " + Thread.currentThread().getName());

                player1Turn.signal();
            }

            gameLock.unlock();

            if (!Thread.currentThread().getName().equals(player1ThreadName) && instructionsSent.compareAndSet(false, true))
            {
                Print.initialGameRules(participants);

                Print.printBoard(participants, board);
            }

            start();

            if (gameState != State.STOPPED)
            {
                if (instructionsSent.compareAndSet(true, false))
                {
                    Print.endingInstructions(participants);
                }
                else
                {
                    Thread.sleep(2000);
                }
            }
        }
        catch (InterruptedException exception)
        {
            logger.info("Stopping the execution of play"  + exception);

            logger.info(exception.getMessage());
        }
        catch (IllegalMonitorStateException exception)
        {
            logger.info("Thread trying to notify/wait on object without acquiring lock : " + exception.getMessage());
        }
    }

    public void start() throws InterruptedException
    {
        while(GameEngine.isGameOver(gameState))
        {

            currentPlayer.set(GameEngine.getCurrentPlayer(currentPlayerIndex, participants));

            gameLock.lock();

            if(Thread.currentThread().getName().equals(player1ThreadName))
            {
                player2Turn.signal();

                player1Turn.await();
            }
            else
            {
                player1Turn.signal();

                player2Turn.await();
            }

            if(!gameState.equals(State.PLAYING))
            {
                return;
            }

            boolean validMove = false;

            while(!validMove)
            {
                try
                {
                    logger.info("Currently Thread : " + Thread.currentThread().getName() + "'s turn is to take input");

                    logger.info("It's turn of player with symbol of " + currentPlayer.get().symbol());

                    String move = GameEngine.receiveMove(currentPlayer.get());

                    int row = Integer.parseInt(move.split(",")[0]);

                    int col = Integer.parseInt(move.split(",")[1]);

                    if(GameEngine.isValidMove(board, row, col))
                    {
                        gameState = board.updateGameState(currentPlayer.get().symbol(), row, col);

                        validMove = true;

                        currentPlayer.get().writer().println("Valid Move");

                        currentPlayer.get().writer().flush();

                        Print.printBoard(participants, board);
                    }
                    else
                    {
                        currentPlayer.get().writer().println("Invalid Move,Please Try again with valid move.");

                        currentPlayer.get().writer().flush();
                    }
                } catch(NumberFormatException e)
                {
                    currentPlayer.get().writer().println("Invalid input format. Try again with row and column separated by a comma.");

                    currentPlayer.get().writer().flush();

                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    currentPlayer.get().writer().println("Invalid input. Try again with values in the range of Board.");

                    currentPlayer.get().writer().flush();
                }
                catch(IOException e)
                {
                    logger.info("Error reading player's move, Game is Ending...");

                    participants.remove(currentPlayer.get());

                    gameState = State.STOPPED;

                    break;
                }
                catch(NullPointerException exception)
                {
                    logger.info("Current player disconnected.");

                    gameState = State.STOPPED;

                    break;
                }
                catch(InterruptedException e)
                {
                    logger.info("Interruption while receiving moves" + e.getMessage());

                    gameState = State.STOPPED;

                    break;
                }
            }

            GameEngine.handleGameState(currentPlayerIndex, gameState, participants);

            if(gameState == State.PLAYING)
            {
                currentPlayerIndex = GameEngine.switchTurn(currentPlayerIndex);

                gameLock.unlock();

            }
            else
            {
                if(currentPlayerIndex == 0)
                {
                    player2Turn.signalAll();
                }
                else
                {
                    player1Turn.signalAll();
                }

                gameLock.unlock();

                break;
            }
        }
    }
}

