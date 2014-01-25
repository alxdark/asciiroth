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
package us.asciiroth.client.event;

import java.util.ArrayList;
import java.util.List;

import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Symbol;

/**
 * A singleton that provides a simple publish/subscribe interface for communication between
 * controller/model and view instances. 
 *
 */
public class Events {

    private static Events instance = new Events();
    /**
     * Get the <code>Events</code> singleton
     * @return  the <code>Events</code> singleton
     */
    public static Events get() {
        return instance;
    }
    
    /* If necessary, but for performance will simplify the representation of listeners 
     * until we actually need to support more than one for a given even type.
    private List playerListeners;
    private List inventoryListeners;
    
    private List cellListeners;
    */
    private List<GameListener> gameListeners;
    private PlayerListener playerListeners;
    private InventoryListener inventoryListeners;
    private List<FlagsListener> flagsListeners;
    private CellListener cellListeners;
    private MessageListener messageListeners;
    
    /**
     * Register a game listener.
     * @param listener
     */
    public void addGameListener(GameListener listener) {
        if (gameListeners == null) {
            gameListeners = new ArrayList<GameListener>();
        }
        gameListeners.add(listener);
    }
    /**
     * Register a player listener.
     * @param listener
     */
    public void addPlayerListener(PlayerListener listener) {
        /*
        if (playerListeners == null) {
            playerListeners = new ArrayList();
        }
        playerListeners.add(listener);
        */
        if (playerListeners != null) {
            throw new RuntimeException("PlayerListener already set; implement a collection");
        }
        playerListeners = listener;
    }
    /**
     * Register an inventory listener.
     * @param listener
     */
    public void addInventoryListener(InventoryListener listener) {
        /*
        if (inventoryListeners == null) {
            inventoryListeners = new ArrayList();
        }
        inventoryListeners.add(listener);
        */
        if (inventoryListeners != null) {
            throw new RuntimeException("InventoryListener already set; implement a collection");
        }
        inventoryListeners = listener;        
    }
    /**
     * Register a flags listener.
     * @param listener
     */
    public void addFlagsListener(FlagsListener listener) {
        if (flagsListeners == null) {
            flagsListeners = new ArrayList<FlagsListener>();
        }
        flagsListeners.add(listener);
    }
    /**
     * Register a cell listener.
     * @param listener
     */
    public void addCellListener(CellListener listener) {
        /*
        if (cellListeners == null) {
            cellListeners = new ArrayList();
        }
        cellListeners.add(listener);
        */
        if (cellListeners != null) {
            throw new RuntimeException("CellListener already set; implement a collection");
        }
        cellListeners = listener;                
    }
    /**
     * Add a message listener.
     * @param listener
     */
    public void addMessageListener(MessageListener listener) {
        /*
        if (messageListeners == null) {
            messageListeners = new ArrayList();
        }
        messageListeners.add(listener);
        */
        if (messageListeners != null) {
            throw new RuntimeException("MessageListener already set; implement a collection");
        }
        messageListeners = listener;                
    }
    /**
     * Fire a game paused event and notify listeners.
     */ 
    public void fireGamePaused() {
        if (gameListeners != null) {
            for (int i=0; i < gameListeners.size(); i++) {
                GameListener listener = (GameListener)gameListeners.get(i);
                listener.onGamePaused();
            }
        }
    }
    /**
     * Fire a game resumed event and notify listeners.
     */ 
    public void fireGameResumed() {
        if (gameListeners != null) {
            for (int i=0; i < gameListeners.size(); i++) {
                GameListener listener = (GameListener)gameListeners.get(i);
                listener.onGameResumed();
            }
        }
    }
    
    /**
     * Fire a player changed event and notify listeners.
     * @param player
     */ 
    public void firePlayerChanged(Player player) {
        if (playerListeners != null) {
            playerListeners.onPlayerChanged(player);
        }
        /*
        if (playerListeners != null) {
            for (int i=0; i < playerListeners.size(); i++) {
                PlayerListener listener = (PlayerListener)playerListeners.get(i);
                listener.onPlayerUpdated(player);
            }
        }
        */
    }
    /**
     * Fire an inventory changed event and notify listeners.
     * @param bag   the players invetory bag
     */ 
    public void fireInventoryChanged(PlayerBag bag) {
        if (inventoryListeners != null) {
            inventoryListeners.onInventoryChanged(bag);
        }
        /*
        if (inventoryListeners != null) {
            for (int i=0; i < inventoryListeners.size(); i++) {
                InventoryListener listener = (InventoryListener)inventoryListeners.get(i);
                listener.onInventoryUpdated(bag);
            }
        }
        */
    }
    /**
     * Fire a flags changed event and notify listeners.
     * @param agent
     */
    public void fireFlagsChanged(Agent agent) {
        if (flagsListeners != null) {
            for (int i=0; i < flagsListeners.size(); i++) {
                FlagsListener listener = (FlagsListener)flagsListeners.get(i);
                listener.onFlagsChanged(agent);
            }
        }
    }
    /**
     * Fire a cell changed event and notify listeners.
     * @param cell
     */
    public void fireCellChanged(Cell cell) {
        if (cellListeners != null) {
            cellListeners.onCellChanged(cell);
        }
        /*
        if (cellListeners != null) {
            for (int i=0; i < cellListeners.size(); i++) {
                CellListener listener = (CellListener)cellListeners.get(i);
                listener.onCellChanged(cell);
            }
        }
        */
    }
    /**
     * Fire a rerender event/request and notify listeners.
     * @param cell
     * @param piece
     * @param sym
     */
    public void fireRerender(Cell cell, Piece piece, Symbol sym) {
        if (cellListeners != null) {
            cellListeners.onRerender(cell, piece, sym);
        }
        /*
        if (cellListeners != null) {
            for (int i=0; i < cellListeners.size(); i++) {
                CellListener listener = (CellListener)cellListeners.get(i);
                listener.onCellChanged(cell);
            }
        }
        */
    }
    /**
     * Fire a message event to be positioned relative to the indicated cell.
     * @param cell
     * @param message
     */
    public void fireMessage(Cell cell, String message) {
        if (messageListeners != null) {
            messageListeners.message(cell, message);
        }
    }
    /**
     * Fire a modal message, which is always positioned next to the player's
     * location. Such messages must be explicitly dismissed by the player.
     * @param message
     */
    public void fireModalMessage(String message) {
        if (messageListeners != null) {
            messageListeners.modalMessage(message);
        }
    }
    /**
     * Display information about the items at the player's current location.  
     */
    public void fireHandleInventoryMessaging() {
        if (messageListeners != null) {
            messageListeners.handleInventoryMessaging();
        }
    }
    /**
     * Cause any messages that are positioned around the current cell to be
     * removed from the board.
     */
    public void fireClearCurrentCell() {
        if (messageListeners != null) {
            messageListeners.clearCurrentCell();
        }
    }
    /**
     * Cause any messages that are positioned around the indicated cell to be
     * removed from the board.
     * @param cell
     */
    public void fireClearCell(Cell cell) {
        if (messageListeners != null) {
            messageListeners.clearCell(cell);
        }
    }
    /**
     * Hide all messages, regardless of where they are.
     */
    public void fireHideAllMessages() {
        if (messageListeners != null) {
            messageListeners.clearAllCells();
        }
    }
    /**
     * Cause a modal message, which was sent out by the <code>fireModalMessage</code>
     * method, to be displayed on the board.
     */
    public void fireHandleModalMessage() {
        if (messageListeners != null) {
            messageListeners.handleModalMessage();
        }
    }
}
