/**
 * Copyright 2008 Alx Dark
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.    
 */
package us.asciiroth.client.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;

/**
 * A simple in-memory implementation of a store for testing purposes. 
 *
 */
public class MemoryStore implements Store {

    private Map<String, GameData> games;
    
    /**
     * Constructor.
     */
    public MemoryStore() {
        games = new HashMap<String, GameData>();
    }

    public void getSavedGames(NamesCallback callback) {
        List<String> list = new ArrayList<String>();
        for (String game : games.keySet()) {
            list.add(game);
        }
        Collections.sort(list);
        callback.execute(list);
    }
    
    public void saveGame(String saveUnderName, String currentName, String playerData, 
        Map<String, String> boards, Command callback) {
        
        GameData data = games.get(currentName);
        if (data == null) {
            data = new GameData();
            games.put(currentName, data);
        }
        data.setPlayerData(playerData);
        for (String url : boards.keySet()) {
            String mapData = boards.get(url);
            data.setMapData(url, mapData);
        }
        if (callback != null) {
            callback.execute();
        }
    }

    public void deleteGame(String name, Command callback) {
        games.remove(name);
        if (callback != null) {
            callback.execute();
        }
    }

    public void loadGame(String name, StoreLoadCallback callback) {
        GameData data = games.get(name);
        callback.load( (data == null) ? null : data.getPlayerData() );
    }

    public void loadBoard(String name, String url, StoreLoadCallback callback) {
        GameData data = games.get(name);
        callback.load( (data == null) ? null : data.getMapData(url) );
    }

    private class GameData {
        private String playerData;
        private Map<String, String> mapData;
        
        GameData() {
            mapData = new HashMap<String, String>();
        }
        String getPlayerData() {
            return playerData;
        }
        void setPlayerData(String playerData) {
            this.playerData = playerData;
        }
        String getMapData(String url) {
            return mapData.get(url);
        }
        void setMapData(String url, String data) {
            mapData.put(url, data);
        }
    }
    public void loadScenario(StoreLoadCallback callback) {
        throw new UnsupportedOperationException("Can't load scenarios using memory store");
    }
}
