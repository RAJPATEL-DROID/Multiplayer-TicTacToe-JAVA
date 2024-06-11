package com.tictactoe.utils;


import com.tictactoe.ClientMain;
import com.tictactoe.GameHandler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingUtils {
    private static final Logger clientLogger = Logger.getLogger(ClientMain.class.getName());
    private static final Logger gameHandlerLogger = Logger.getLogger(GameHandler.class.getName());

    static {
        try
        {
            configureLogger(clientLogger, "ClientConnection.log");

            configureLogger(gameHandlerLogger  , "GameHandling.log");

            // Remove the default console handlers from the root logger
            Logger rootLogger = LogManager.getLogManager().getLogger("");

            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers)
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

    public static Logger getClientLogger()
    {
        return clientLogger;
    }

    public static Logger getGameHandlerLogger()
    {
        return gameHandlerLogger;
    }


}