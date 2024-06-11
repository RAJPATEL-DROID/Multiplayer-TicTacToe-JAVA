package com.server.clienthandler;

import com.server.gamemanager.GameManager;
import com.server.utils.ConfigReader;
import com.server.utils.LoggingUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class ClientHandler extends Thread
{
    private final ConfigReader configReader;

    private static final Logger logger = LoggingUtils.getClientHandlerLogger();

    private final Socket clientSocket;

    private final GameManager gameManager;

    private final String clientId;

    private BufferedReader reader;

    private PrintWriter writer;

    public ClientHandler(Socket clientsocket, GameManager gameManager, String clientId, ConfigReader configReader)
    {

        this.clientSocket = clientsocket;

        this.gameManager = gameManager;

        this.clientId = clientId;

        this.configReader = configReader;

        try
        {
            reader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

            writer = new PrintWriter(clientsocket.getOutputStream());
        }
        catch(IOException e)
        {
            logger.info("Error while reading and writing to user stream for client with id : " + clientsocket.getRemoteSocketAddress().toString().split(":")[1]);
        }
    }

    @Override
    public void run()
    {
        try
        {
            writer.println("Welcome to Multiplayer Tic-Tac-Toe!");

            sendInstruction();

            processInput();
        }
        finally
        {
            try
            {
                clientSocket.close();

                logger.info("Client with id : " + clientId + " disconnected");
            }
            catch(IOException exception)
            {
                logger.info("Error while closing socket for client with id : " + this.clientId);

                logger.info("Error : "  + exception.getMessage());
            }
        }
    }

    private void sendInstruction()
    {

        writer.println("To create a new game room, Enter " + configReader.get_CHOICE_FOR_NEW_GAME_ROOM());

        writer.println("If You want to join Existing GameRoom, Enter " + configReader.getJOIN_GAME_ROOM_CHOICE());

        writer.println("TERMINATE");

        writer.flush();

    }

    private void processInput()
    {
        String inputLine;

        try
        {
            while((inputLine = reader.readLine()) != null)
            {
                if(inputLine.equals(configReader.get_CHOICE_FOR_NEW_GAME_ROOM()) )
                {
                    List<String> ids = gameManager.createAndStartNewRoom();

                    writer.println("New Room created on Port:" + ids.get(0));

                    writer.println("Here is your sessionId:" + ids.get(1));

                    writer.flush();

                    logger.info("New Room created with session ID : " + ids.get(1)  + " on port " + ids.get(0));

                    break;
                }
                else if(inputLine.equals(configReader.getJOIN_GAME_ROOM_CHOICE()))
                {
                    writer.println("Enter Session ID : ");

                    writer.flush();

                    String sessionId = reader.readLine();
                    try
                    {
                        Integer portNo = gameManager.validateSessionAndSendPortNo(Long.valueOf(sessionId));

                        if(portNo == -1)
                        {
                            writer.println("No game room exist on this session Id!");

                            writer.flush();

                            sendInstruction();

                            logger.info("Instructions sent again for client Id : " + this.clientId);

                        }
                        else
                        {
                            writer.println("Port No of GameRoom :"+ portNo);

                            writer.flush();

                            break;
                        }
                    }
                    catch(NumberFormatException exception)
                    {

                        logger.info("Received null from client : " + this.clientSocket.getRemoteSocketAddress().toString().split(":")[1]);

                    }
                }
                else
                {
                    writer.println("Unknown Command!!");

                    writer.flush();

                    sendInstruction();

                    logger.info("Instructions sent again for client Id : " + this.clientId);
                }
            }
        }
        catch(IOException e)
        {
            writer.println("Error Reading the Input");

            System.out.println("Error taking input from the client Id : " + this.clientId);
        }
    }

}