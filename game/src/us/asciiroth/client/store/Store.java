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

import java.util.Map;

import com.google.gwt.user.client.Command;

/**
 * An interface describing a persistence provider for the game. There are currently 
 * four providers: <br/><br/>
 * 
 * <ul>
 * <li>In memory: for testing (but rarely seen or used anymore);
 * <li>HTML 5: uses HTML 5 database API, currently supported by Safari 4;
 * <li>Gears: supported by IE, Firefox and Chrome, but you must install Gears;
 * <li>AIR: Persistence is part of the AIR framework.
 * </ul>
 * 
 * I am anticipating that HTML 5 will become better supported and that eventually,
 * you'll just be able to play this game without any plug-ins. 
 *
 */
public interface Store {
    /**
     * Get a list of names for all the current saved games.
     * 
     * @param	callback to receive saved game names
     */
    public void getSavedGames(NamesCallback callback);
    /**
     * Delete the game with this name.
     * 
     * @param name
     * @param callback
     */
    public void deleteGame(String name, Command callback);
    /**
     * Save the game, including the player and any changed but unsaved maps. Once
     * the save is completed, the optional callback instance will be executed. 
     * @param saveUnderName     the name of the game
     * @param currentName       the current name of the player
     * @param player            the player instance for this game
     * @param boards            a map of boards keyed by their URLs that have changed
     * @param callback          an optional callback command
     */
    public void saveGame(String saveUnderName, String currentName, 
        String player, Map<String, String> boards, Command callback);
    /**
     * Loads the player's JSON data from persistence and provides it to the callback
     * instance. 
     * 
     * @param name      the name of the saved game
     * @param callback  an optional callback to execute after the game loads 
     */
    public void loadGame(String name, StoreLoadCallback callback);
    /**
     * Load the board for the saved game indicated, with the given URL (this is the
     * URL as specified in the map data, relative to the root directory of the game). 
     * Loads the board's JSON data from persistence and provides it to the callback 
     * instance. 
     *
     * 
     * @param name      the name of the saved game
     * @param url       the url of the board (
     * @param callback
     */
    public void loadBoard(String name, String url, StoreLoadCallback callback);
    
    /**
     * A bit of a hack... in AIR, this presents a directory browsing dialog, and
     * the callback is passed not JSON, but a file path to a directory which can 
     * then be used to attempt to load a scenario from disk. 
     * @param callback  will be passed the file path to a directory, not a JSON blob
     */
    public void loadScenario(StoreLoadCallback callback);
}
