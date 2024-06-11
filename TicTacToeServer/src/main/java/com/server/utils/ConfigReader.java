package com.server.utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.util.logging.Logger;

public class ConfigReader {
    private int SERVER_PORT;

    private int FIRST_PORT_OF_GAME_ROOM;

    private int LAST_PORT_OF_GAMEROOM;

    private String CHOICE_OF_NEW_GAME_ROOM;

    private String JOIN_GAME_ROOM_CHOICE;

    private static final Logger logger = LoggingUtils.getServerLogger();

    public ConfigReader()
    {
        try
        {
            FileReader fileReader = new FileReader("src/main/java/com/server/utils/config.json");

            JSONTokener jsonTokener = new JSONTokener(fileReader);

            JSONObject jsonObject = new JSONObject(jsonTokener);

            SERVER_PORT = jsonObject.getInt("SERVER_PORT");

            FIRST_PORT_OF_GAME_ROOM = jsonObject.getInt("FIRST_PORT_OF_GAME_ROOM");

            LAST_PORT_OF_GAMEROOM = jsonObject.getInt("LAST_PORT_OF_GAMEROOM");
            
            CHOICE_OF_NEW_GAME_ROOM = jsonObject.getString("NEW_GAME_ROOM_CHOICE");

            JOIN_GAME_ROOM_CHOICE = jsonObject.getString("JOIN_GAME_ROOM_CHOICE");

            logger.info("Config file imported successfully");

            fileReader.close();
        }
        catch (Exception exception)
        {
            logger.info("Error reading the configuration");
            logger.info(exception.getMessage());
        }
    }

    // Getters
    public int get_SERVER_PORT()
    {
        return SERVER_PORT;
    }

    public int get_FIRST_PORT_OF_GAME_ROOM()
    {
        return FIRST_PORT_OF_GAME_ROOM;
    }

    public int get_LAST_PORT_OF_GAMEROOM()
    {
        return LAST_PORT_OF_GAMEROOM;
    }
    
    public String get_CHOICE_FOR_NEW_GAME_ROOM()
    {
        return CHOICE_OF_NEW_GAME_ROOM;
    }

    public String getJOIN_GAME_ROOM_CHOICE()
    {
        return JOIN_GAME_ROOM_CHOICE;
    }

}