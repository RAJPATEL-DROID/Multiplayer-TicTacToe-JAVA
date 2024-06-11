package com.server;

import com.server.gamemanager.GameManager;
import com.server.clienthandler.ClientHandler;
import com.server.utils.ConfigReader;
import com.server.utils.LoggingUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ServerClass
{
    static ConfigReader configReader = new ConfigReader("src/main/java/com/server/utils/config.json");
    private static final int SERVER_PORT = configReader.getSERVER_PORT();

    private static final Logger logger = LoggingUtils.getLogger();

    private static final ExecutorService clientHandlerThreads = Executors.newCachedThreadPool();


    public static void main(String[] args)
    {
        try(ServerSocket socket = new ServerSocket(SERVER_PORT))
        {
            System.out.println("Server listening on port : " + SERVER_PORT);

            GameManager gameManager = new GameManager(configReader);

            while(true)
            {
                Socket userSocket = socket.accept();

                var clientId = userSocket.getRemoteSocketAddress().toString().split(":")[1];

                logger.info("Client with id : " + clientId + " connected.");

                ClientHandler clientHandler = new ClientHandler(userSocket, gameManager, clientId);

                clientHandlerThreads.execute(clientHandler);
            }
        }
        catch(IOException exception)
        {
            logger.warning("Server having problem on waiting for connection"  + exception.getMessage());
        }
    }
}