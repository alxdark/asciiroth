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
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;

/**
 * It's nothing fancy and in particular, the lack off transactionality
 * is bad, but it works. 
 * 
 * TODO: Transaction for delete and save, the tables have no PK/FK relationship
 * or anything.
 *
 */
public class AirStore implements Store {

    private List<String> names;
    
    public AirStore() {
        names = new ArrayList<String>();
        createTables();
    }

    public void getSavedGames(NamesCallback callback) {
        names.clear();
        doGetSavedGames();
        callback.execute(names);
    }
    
    public void saveGame(final String newName, final String currentName, final String player,
            final Map<String, String> boards, final Command callback) {

        if (!newName.equals(currentName)) {
            deleteGame(newName, new Command() {
                public void execute() {
                    // copy all existing boards under old game
                    copyBoards(currentName, newName);
                    saveIncrementalUpdate(newName, player, boards, callback);
                }
            });
        } else {
            deletePlayer(currentName);
            saveIncrementalUpdate(newName, player, boards, callback);
        }
    }
    private void saveIncrementalUpdate(String newName, String player, Map<String, String> boards, Command callback) {
        doSavePlayer(newName, player);
        for (String boardID : boards.keySet()) {
            deleteBoard(newName, boardID);
            String boardJSON = boards.get(boardID);
            doSaveBoard(newName, boardID, boardJSON);
        }
        // Here it really needs to be done outside the native methods.
        if (callback != null) {
            callback.execute();
        }
    }
    private native void copyBoards(String currentName, String newName) /*-{
        try {
            if (!$wnd.copyBoards) {
                $wnd.copyBoards = new $wnd.air.SQLStatement();
                $wnd.copyBoards.sqlConnection = $wnd.conn;
                $wnd.copyBoards.text = "INSERT INTO boards (name, boardID, boardJSON) SELECT ?, boardID, boardJSON FROM boards WHERE name = ?;";
            }
            $wnd.conn.begin();
            $wnd.copyBoards.parameters[0] = newName;
            $wnd.copyBoards.parameters[1] = currentName;
            $wnd.copyBoards.execute();
            $wnd.conn.commit();
        } catch (e) {
            $wnd.air.trace("copyBoards error message:", e.message, e.details);
            $wnd.conn.rollback();
        }
    }-*/;
    
    private native void createTables() /*-{
        $wnd.conn = new $wnd.air.SQLConnection();
        $wnd.dbFile = $wnd.air.File.applicationStorageDirectory.resolvePath("asciiroth.db");
        try {
            $wnd.conn.open($wnd.dbFile);
            var statement = new $wnd.air.SQLStatement();
            statement.sqlConnection = $wnd.conn;
            statement.text = "CREATE TABLE IF NOT EXISTS player(name TEXT, playerJSON TEXT);";
            statement.execute();    
            statement.text = "CREATE TABLE IF NOT EXISTS boards(name TEXT, boardID TEXT, boardJSON TEXT);";
            statement.execute();
        } catch (e) {
            $wnd.air.trace("createTables error message:", e.message, e.details);
        }
    }-*/;
    public native void loadGame(String name, StoreLoadCallback callback) /*-{
        try {
            if (!$wnd.loadGame) {
                $wnd.loadGame = new $wnd.air.SQLStatement();
                $wnd.loadGame.sqlConnection = $wnd.conn;
                $wnd.loadGame.text = "SELECT playerJSON FROM player WHERE name = ?;";
            }
            $wnd.loadGame.parameters[0] = name;
            $wnd.loadGame.execute();
            var result = $wnd.loadGame.getResult();
            var json = null;
            if (result.data != null) {
                // But there should be only one...
                for (var i=0, len = result.data.length; i < len; i++) {
                    json = result.data[i].playerJSON;
                }
            }
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        } catch (e) {
            $wnd.air.trace("doLoadGame error message:", e.message, e.details);
        }
    }-*/;
    public native void loadBoard(String name, String boardID, StoreLoadCallback callback) /*-{
        try {
            if (!$wnd.loadBoard) {
                $wnd.loadBoard = new $wnd.air.SQLStatement();
                $wnd.loadBoard.sqlConnection = $wnd.conn;
                $wnd.loadBoard.text = "SELECT boardJSON FROM boards WHERE name = ? and boardID = ?;";
            }
            $wnd.loadBoard.parameters[0] = name;
            $wnd.loadBoard.parameters[1] = boardID;
            
            $wnd.loadBoard.execute();
            var result = $wnd.loadBoard.getResult();
            var json = null;
            if (result.data != null) {
                // But there should be only one...
                for (var i=0, len = result.data.length; i < len; i++) {
                    json = result.data[i].boardJSON;
                }
            }
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        } catch (e) {
            $wnd.air.trace("doLoadBoard error message:", e.message, e.details);
        }
    }-*/;
    
    // TODO: We've got ridiculous "transactions" going on that aren't transactionalizing
    // anything, because I'm not sure how to pass ugly 'ole Java maps into these methods.
    
    private native void deletePlayer(String name) /*-{
        try {
            if (!$wnd.deletePlayer) {
                $wnd.deletePlayer = new $wnd.air.SQLStatement();
                $wnd.deletePlayer.sqlConnection = $wnd.conn;
                $wnd.deletePlayer.text = "DELETE FROM player WHERE name = ?;";
            }
            $wnd.conn.begin();
            $wnd.deletePlayer.parameters[0] = name;
            $wnd.deletePlayer.execute();
            $wnd.conn.commit();
        } catch (e) {
            $wnd.air.trace("deletePlayer error message:", e.message, e.details);
            $wnd.conn.rollback();
        }
    }-*/;

    private native void deleteBoard(String name, String boardID) /*-{
        try {
            if (!$wnd.deleteBoard) {
                $wnd.deleteBoard = new $wnd.air.SQLStatement();
                $wnd.deleteBoard.sqlConnection = $wnd.conn;
                $wnd.deleteBoard.text = "DELETE FROM boards WHERE name = ? and boardID = ?;";
            }
            $wnd.conn.begin();
            $wnd.deleteBoard.parameters[0] = name;
            $wnd.deleteBoard.parameters[1] = boardID;
            $wnd.deleteBoard.execute();
            $wnd.conn.commit();
        } catch (e) {
            $wnd.air.trace("deleteBoard error message:", e.message, e.details);
            $wnd.conn.rollback();
        }
    }-*/;
    
    public native void deleteGame(String name, Command callback) /*-{
        try {
            if (!$wnd.deleteGame) {
                $wnd.deleteGame = new $wnd.air.SQLStatement();
                $wnd.deleteGame.sqlConnection = $wnd.conn;
                $wnd.deleteGame.text = "DELETE FROM player WHERE name = ?;";
                $wnd.deleteGame2 = new $wnd.air.SQLStatement();
                $wnd.deleteGame2.sqlConnection = $wnd.conn;
                $wnd.deleteGame2.text = "DELETE FROM boards WHERE name = ?;";
            }
            $wnd.conn.begin();
            $wnd.deleteGame.parameters[0] = name;
            $wnd.deleteGame.execute();
            $wnd.deleteGame2.parameters[0] = name;
            $wnd.deleteGame2.execute();
            $wnd.conn.commit();
            if (callback != null) {
                callback.@com.google.gwt.user.client.Command::execute()();
            }
        } catch (e) {
            $wnd.air.trace("deleteGame error message:", e.message, e.details);
            $wnd.conn.rollback();
        }
    }-*/;
    
    private native void doSavePlayer(String name, String playerJSON) /*-{
        try {
            if (!$wnd.saveGame) {
                $wnd.saveGame = new $wnd.air.SQLStatement();
                $wnd.saveGame.sqlConnection = $wnd.conn;
                $wnd.saveGame.text = "INSERT INTO player(name, playerJSON) VALUES (?, ?);";
            }
            $wnd.saveGame.parameters[0] = name;
            $wnd.saveGame.parameters[1] = playerJSON;
            $wnd.saveGame.execute();    
        } catch (e) {
            $wnd.air.trace("doSavePlayer error message:", e.message, e.details);
        }
    }-*/;
    
    private native void doSaveBoard(String name, String boardID, String boardJSON) /*-{
        try {
            if (!$wnd.saveBoard) {
                $wnd.saveBoard = new $wnd.air.SQLStatement();
                $wnd.saveBoard.sqlConnection = $wnd.conn;
                $wnd.saveBoard.text = "INSERT INTO boards(name, boardID, boardJSON) VALUES (?, ?, ?);";
            }
            $wnd.saveBoard.parameters[0] = name;
            $wnd.saveBoard.parameters[1] = boardID;
            $wnd.saveBoard.parameters[2] = boardJSON;
            $wnd.saveBoard.execute();    
        } catch (e) {
            $wnd.air.trace("doSaveBoard error message:", e.message, e.details);
        }
    }-*/;    
    
    private native void doGetSavedGames() /*-{
        try {
            if (!$wnd.getSavedGames) {
                $wnd.getSavedGames = new $wnd.air.SQLStatement();
                $wnd.getSavedGames.sqlConnection = $wnd.conn;
                $wnd.getSavedGames.text = "SELECT name FROM player ORDER BY name;";
            }
            $wnd.getSavedGames.execute();
                
            var result = $wnd.getSavedGames.getResult();
            if (result.data != null) {
                for (var i=0, len = result.data.length; i < len; i++) {
                    var name = result.data[i].name;
                    
                    var names = this.@us.asciiroth.client.store.AirStore::names;
                    names.@java.util.List::add(Ljava/lang/Object;)(name);
                }
            }
        } catch (e) {
            $wnd.air.trace("doGetSavedGames error message:", e.message, e.details);
        }
    }-*/;
    public native void loadScenario(StoreLoadCallback callback) /*-{
        var file = $wnd.air.File.documentsDirectory;
        file.addEventListener($wnd.air.Event.SELECT, dirSelected);
        file.browseForDirectory("Select the scenario's directory");
        function dirSelected(event) {
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(file.nativePath);
        }        
    }-*/;
}
