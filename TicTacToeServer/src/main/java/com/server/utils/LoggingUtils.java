package com.server.utils;
import com.server.clienthandler.ClientHandler;
import com.server.game.Game;
import com.server.gameroom.GameRoom;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingUtils {
    private static final Logger serverLogger = Logger.getLogger(LoggingUtils.class.getName());
    private static final Logger clientHandlerLogger = Logger.getLogger(ClientHandler.class.getName());
    private static final Logger gameRoomLogger = Logger.getLogger(GameRoom.class.getName());
    private static final Logger gameLogger = Logger.getLogger(Game.class.getName());

    static {
        try
        {
            configureLogger(serverLogger, "server.log");

            configureLogger(clientHandlerLogger, "clienthandler.log");

            configureLogger(gameRoomLogger, "gameroom.log");

            configureLogger(gameLogger, "game.log");

            // Remove the default console handlers from the root logger
            Logger rootLogger = LogManager.getLogManager().getLogger("");

            Handler[] handlers = rootLogger.getHandlers();
            for(Handler handler : handlers)
            {
                rootLogger.removeHandler(handler);
            }
        }
        catch (IOException e)
        {
            System.out.println("Error while configuring the logger");
        }
    }

    private static void configureLogger(Logger logger, String logFileName) throws IOException
    {
        FileHandler fileHandler = new FileHandler(logFileName, false);

        fileHandler.setFormatter(new SimpleFormatter());

        logger.addHandler(fileHandler);
    }

    public static Logger getServerLogger()
    {
        return serverLogger;
    }

    public static Logger getClientHandlerLogger()
    {
        return clientHandlerLogger;
    }

    public static Logger getGameRoomLogger()
    {
        return gameRoomLogger;
    }

    public static Logger getGameLogger()
    {
        return gameLogger;
    }
}