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
 * WHAT 5 local storage API, which has been implemented in Safari, and may eventually be implemented 
 * in Firefox and Opera. At that point, saving games would just work. 
 */
public class HTML5Store implements Store {

    private List<String> names;
    
    public HTML5Store() {
        names = new ArrayList<String>();
        createTables();
    }
    
    private native void createTables() /*-{
    	var okFunc = function() {}
    	var errorFunc = function(tx, error) { $wnd.alert("HTML5Store create error message: " + error.message); }
    	
        // What's that last number? Store size limit?
        $wnd.db = openDatabase("asciiroth", "1.0", "Asciiroth", (1024 * 1024 * 4));
        $wnd.db.transaction(function(tx) {
            tx.executeSql("CREATE TABLE IF NOT EXISTS player(name TEXT, playerJSON TEXT);", [], okFunc, errorFunc); 
            tx.executeSql("CREATE TABLE IF NOT EXISTS boards(name TEXT, boardID TEXT, boardJSON TEXT);", [], okFunc, errorFunc);
        });
    }-*/;
    
    public native void getSavedGames(NamesCallback callback) /*-{
    	var names = this.@us.asciiroth.client.store.HTML5Store::names;
    	names.@java.util.List::clear()();
    	var okFunc = function(tx, result) {
            $wnd.console.log("1");
            if (result && result.rows) {
                for (var i=0, len = result.rows.length; i < len; i++) {
                    $wnd.console.log("2");
                    var name = result.rows.item(i)['name'];
                    names.@java.util.List::add(Ljava/lang/Object;)(name);
                    $wnd.console.log("3: " + name);
                    $wnd.console.log("4: " + names.@java.util.List::size()());
                }
            }
            if (callback != null) {
            	// TODO: Probably wrong.
                callback.@us.asciiroth.client.store.NamesCallback::execute(Ljava/util/List;)(names);
            }
        }
        var errorFunc = function(tx, error) { $wnd.alert("doGetSavedGames error message: " + error.message); }
    
        $wnd.db.transaction(function(tx) {
            tx.executeSql("SELECT name FROM player ORDER BY name;", [], okFunc, errorFunc);
        });
    }-*/;
    
    public native void deleteGame(String name, Command callback) /*-{
    	var nullFunc = function() {}
    	var okFunc = function(tx, result) {
            if (callback != null) {
                callback.@com.google.gwt.user.client.Command::execute()();
            }
        } 
        var errorFunc = function(tx, error) {
            if (callback != null) {
                callback.@com.google.gwt.user.client.Command::execute()();
            }
        }
        $wnd.db.transaction(function(tx) {
            tx.executeSql("DELETE FROM player WHERE name = ?;", [name], nullFunc, errorFunc);
            tx.executeSql("DELETE FROM boards WHERE name = ?;", [name], okFunc, errorFunc);
        });
    }-*/;

    public native void loadBoard(String name, String boardID, StoreLoadCallback callback) /*-{
    	var okFunc = function(tx, result) {
            var json = null;
            if (result && result.rows) {
                for (var i=0, len = result.rows.length; i < len; i++) {
                    json = result.rows.item(i)['boardJSON'];
                }
            }
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        }
    	var errorFunc = function(tx, error) { $wnd.alert("doLoadBoard error message: " + error.message); }
        $wnd.db.transaction(function (tx) {
            tx.executeSql("SELECT boardJSON FROM boards WHERE name = ? and boardID = ?;", [name, boardID], okFunc, errorFunc);
        });
    }-*/;

    public native void loadGame(String name, StoreLoadCallback callback) /*-{
    	var okFunc = function(tx, result) {
            $wnd.console.log("a3");
            var json = null;
            if (result && result.rows) {
                for (var i=0, len = result.rows.length; i < len; i++) {
                    $wnd.console.log("a4");
                    json = result.rows.item(i)['playerJSON'];
                }
            }
            $wnd.console.log("a5");
            callback.@us.asciiroth.client.store.StoreLoadCallback::load(Ljava/lang/String;)(json);
        }
    	var errorFunc = function(tx, error) { $wnd.alert("loadGame error message: " + error.message); }
        $wnd.console.log("a1");
        $wnd.db.transaction(function (tx) {
            $wnd.console.log("a2");
            tx.executeSql("SELECT playerJSON FROM player WHERE name = ?;", [name], 
            okFunc, errorFunc);
        });
    }-*/;

    public void loadScenario(StoreLoadCallback callback) {
        throw new UnsupportedOperationException("Can only load scenarios in AIR version of game");
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
    	var okFunc = function(tx, result) {}
    	var errorFunc = function(tx, error) { $wnd.alert("copyBoards error message:" + error.message); } 
        $wnd.db.transaction(function(tx) {
            tx.executeSql("INSERT INTO boards (name, boardID, boardJSON) SELECT ?, boardID, boardJSON FROM boards WHERE name = ?;",
            [newName, currentName], okFunc, errorFunc);
        });
    }-*/;
    private native void deletePlayer(String name) /*-{
    	var okFunc = function(tx, result) {}
    	var errorFunc = function(tx, error) { $wnd.alert("deletePlayer error message: " + error.message); }
        $wnd.db.transaction(function(tx) {
            tx.executeSql("DELETE FROM player WHERE name = ?;", [name], okFunc, errorFunc);
        });
    }-*/;
    private native void deleteBoard(String name, String boardID) /*-{
    	var okFunc = function(tx, result) {}
    	var errorFunc = function(tx, error) { $wnd.alert("deleteBoard error message: " + error.message); }
    	
        $wnd.db.transaction(function(tx) {
            tx.executeSql("DELETE FROM boards WHERE name = ? and boardID = ?;", [name, boardID], okFunc, errorFunc);
        });
    }-*/;
    private native void doSavePlayer(String name, String playerJSON) /*-{
    	var okFunc = function(tx, result) {}
    	var errorFunc = function(tx, error) { $wnd.alert("doSavePlayer error message: " + error.message); }
    	
        $wnd.db.transaction(function(tx) {
            tx.executeSql("INSERT INTO player(name, playerJSON) VALUES (?, ?);", [name, playerJSON], okFunc, errorFunc);
        });
    }-*/;
    private native void doSaveBoard(String name, String boardID, String boardJSON) /*-{
    	var okFunc = function(tx, result) {}
    	var errorFunc = function(tx, error) { $wnd.alert("doSaveBoard error message: " + error.message); }
    
        $wnd.db.transaction(function(tx) {
            tx.executeSql("INSERT INTO boards(name, boardID, boardJSON) VALUES (?, ?, ?);", [name, boardID, boardJSON], okFunc, errorFunc);
        });
    }-*/;
}
