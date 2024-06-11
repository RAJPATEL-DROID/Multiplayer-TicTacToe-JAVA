package com.tictactoe.utils;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;

public class ConfigReader
{
    private String SERVER_IP_ADDRESS;

    private int SERVER_PORT;

    private String CHOICE_OF_NEW_GAME_ROOM;

    private String JOIN_GAME_ROOM_CHOICE;


    public ConfigReader(String filePath)
    {
        try
        {
            FileReader fileReader = new FileReader(filePath);

            JSONTokener jsonTokener = new JSONTokener(fileReader);

            JSONObject jsonObject = new JSONObject(jsonTokener);

            SERVER_PORT = jsonObject.getInt("SERVER_PORT");

            SERVER_IP_ADDRESS = jsonObject.getString("SERVER_IP_ADDRESS");

            CHOICE_OF_NEW_GAME_ROOM = jsonObject.getString("NEW_GAME_ROOM_CHOICE");

            JOIN_GAME_ROOM_CHOICE = jsonObject.getString("JOIN_GAME_ROOM_CHOICE");

        } catch(Exception exception)
        {
            System.out.println("Error reading configuration");
            System.out.println(exception.getMessage());
        }

    }

    public int get_SERVER_PORT()
    {
        return SERVER_PORT;
    }

    public String get_SERVER_IP_ADDRESS(){
        return SERVER_IP_ADDRESS;
    }

    public String get_CHOICE_FOR_NEW_GAME_ROOM()
    {
        return CHOICE_OF_NEW_GAME_ROOM;
    }

    public String get_JOIN_GAME_ROOM_CHOICE()
    {
        return JOIN_GAME_ROOM_CHOICE;
    }

}