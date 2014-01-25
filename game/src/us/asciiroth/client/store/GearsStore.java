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
 * It's nothing fancy but it works. 
 */
public class GearsStore implements Store {

    private List<String> names;
    
    public GearsStore() {
        createTables();
    }

    public void getSavedGames(NamesCallback callback) {
        if (names == null) {
            names = new ArrayList<String>();
        }
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
            var result = $wnd.db.execute(
                "INSERT INTO boards (name, boardID, boardJSON) SELECT ?, boardID, boardJSON FROM boards WHERE name = ?;",
                [newName, currentName]);
        } catch (e) {
            // $wnd.alert("copyBoards error message:" + e.message);
        }
    }-*/;
    private native void createTables() /*-{
        try {
            $wnd.db = $wnd.google.gears.factory.create('beta.database');
            $wnd.db.open('asciiroth');
            $wnd.db.execute("CREATE TABLE IF NOT EXISTS player(name TEXT, playerJSON TEXT)");
            $wnd.db.execute("CREATE TABLE IF NOT EXISTS boards(name TEXT, boardID TEXT, boardJSON TEXT)");
        } catch (e) {
            // $wnd.alert("createTables error message:" + e.message);
        }
        // Try not closing it.
    }-*/;
    public native void loadGame(String name, StoreLoadCallback callback) /*-{
        try {
            var result = $wnd.db.execute(
                "SELECT playerJSON FROM player WHERE name = ?;",
                [name]);
            var json = null;
            if (result.isValidRow()) {
                json = result.field(0);
            }
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        } catch (e) {
            // $wnd.alert("doLoadGame error message:" + e.message);
        }
    }-*/;
    public native void loadBoard(String name, String boardID, StoreLoadCallback callback) /*-{
        try {
            var result = $wnd.db.execute(
                "SELECT boardJSON FROM boards WHERE name = ? and boardID = ?;",
                [name, boardID]);
            
            var json = null;
            if (result.isValidRow()) {
                json = result.field(0);
            }
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        } catch (e) {
            $wnd.alert("doLoadBoard error message:" + e.message);
        }
    }-*/;
    
    public native void deletePlayer(String name) /*-{
        try {
            $wnd.db.execute("DELETE FROM player WHERE name = ?;",[name]);
        } catch (e) {
            $wnd.alert("deletePlayer error message:" + e.message);
        }
    }-*/;
    
    public native void deleteBoard(String name, String boardID) /*-{
        try {
            $wnd.db.execute("DELETE FROM boards WHERE name = ? and boardID = ?;",[name, boardID]);
        } catch (e) {
            $wnd.alert("deleteBoard error message:" + e.message);
        }
    }-*/;
    
    public native void deleteGame(String name, Command callback) /*-{
        try {
            $wnd.db.execute("DELETE FROM player WHERE name = ?;",[name]);
            $wnd.db.execute("DELETE FROM boards WHERE name = ?;",[name]);
            if (callback != null) {
                callback.@com.google.gwt.user.client.Command::execute()();
            }
        } catch (e) {
            $wnd.alert("deleteGame error message:" + e.message);
        }
    }-*/;
    
    private native void doSavePlayer(String name, String playerJSON) /*-{
        try {
            $wnd.db.execute(
                "INSERT INTO player(name, playerJSON) VALUES (?, ?);",
                [name, playerJSON]);
        } catch (e) {
            $wnd.alert("doSavePlayer error message:" + e.message);
        }
    }-*/;
    
    private native void doSaveBoard(String name, String boardID, String boardJSON) /*-{
        try {
            $wnd.db.execute(
                "INSERT INTO boards(name, boardID, boardJSON) VALUES (?, ?, ?);",
                [name, boardID, boardJSON]);
        } catch (e) {
            $wnd.alert("doSaveBoard error message:" + e.message);
        }
    }-*/;
    private native void doGetSavedGames() /*-{
        try {
            var result = $wnd.db.execute("SELECT name FROM player ORDER BY name;");
            var names = this.@us.asciiroth.client.store.GearsStore::names;
            while (result.isValidRow()) {
                var name = result.field(0);
                names.@java.util.List::add(Ljava/lang/Object;)(name);
                result.next();
            }
        } catch (e) {
            $wnd.alert("doGetSavedGames error message:" + e.message);
        }
    }-*/;
    public void loadScenario(StoreLoadCallback callback) {
        throw new UnsupportedOperationException("Can't load scenarios using Gears store");
    }
}
