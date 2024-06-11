package com.server.gamemanager;

import com.server.gameroom.GameRoom;
import com.server.utils.ConfigReader;
import com.server.utils.LoggingUtils;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameManager
{
    private static final Logger logger = LoggingUtils.getClientHandlerLogger();

    private static final Map<Long, AbstractMap.SimpleEntry<Integer, GameRoom>> sessionIdToGameRoomMap = new HashMap<>();

    private static Integer arrayIndex =-1;

    private static final AtomicLong ssid = new AtomicLong(1000);

    private static final ExecutorService poolOfGameRooms = Executors.newCachedThreadPool();

    private final int FIRST_PORT_OF_GAME_ROOM;

    private final int LAST_PORT_OF_GAMEROOM;

    private final List<Integer> ports = new ArrayList<>();

    public GameManager(ConfigReader configReader){

        FIRST_PORT_OF_GAME_ROOM = configReader.get_FIRST_PORT_OF_GAME_ROOM();

        LAST_PORT_OF_GAMEROOM = configReader.get_LAST_PORT_OF_GAMEROOM();

        for(int index = 0; index < (LAST_PORT_OF_GAMEROOM - FIRST_PORT_OF_GAME_ROOM); index++)
        {
            ports.add(FIRST_PORT_OF_GAME_ROOM + index);
        }
    }

    public synchronized List<String > createAndStartNewRoom()
    {
        Integer portNo = allocatePort();

        Long sessionId = ssid.getAndAdd(10);

        String roomId = sessionId + "-" + portNo.toString();

        GameRoom gameRoom = new GameRoom(roomId);

        sessionIdToGameRoomMap.put(sessionId, new AbstractMap.SimpleEntry<>(portNo, gameRoom));

        poolOfGameRooms.execute(gameRoom);

        return List.of(portNo.toString(),sessionId.toString());
    }

    public Integer allocatePort(){

        arrayIndex = (arrayIndex+1)%(LAST_PORT_OF_GAMEROOM - FIRST_PORT_OF_GAME_ROOM);

        return ports.get(arrayIndex);
    }

    public Integer validateSessionAndSendPortNo(Long sessionId) {

        AbstractMap.SimpleEntry<Integer, GameRoom> entry = sessionIdToGameRoomMap.get(sessionId);

        return entry != null ? entry.getKey() : -1;
    }

    public static void removeGameRoom(String sessionId)
    {
        sessionIdToGameRoomMap.remove(Long.valueOf(sessionId)) ;

        logger.info("Game room with id " + sessionId + " is removed from map");
    }

}