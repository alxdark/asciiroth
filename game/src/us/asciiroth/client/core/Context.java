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
package us.asciiroth.client.core;

import us.asciiroth.client.board.Board;

/**
 * This could almost just be an interface that both Game and Event
 * implement, and then HasBoard could be removed, and only this 
 * interface could be exposed when event is used for certain methods.
 * 
 *
 */
public class Context {

    private final Player player;
    private final Board board;    
    
    public Context(Player player, Board board) {
        this.player = player;
        this.board = board;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Player getPlayer() {
        return player;
    }
    
}
