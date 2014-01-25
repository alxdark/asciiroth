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


/**
 * A kind of piece that exists on the board above items and terrain, 
 * used to handle animation like the throwing of items. It has two 
 * important consequences for animated pieces. First the animation 
 * timeline (frame count) of an effect always starts at zero, and not at a
 * randomly seeded starting frame. Second, effects are not saved on 
 * the board. Instead, the game will wait to save until all non-transient
 * effects have played out on the board. This is important with thrown 
 * items, for example, since their flight must end to accurately decide
 * what will happen to them on the board. Other effects, like bullets, are
 * not saved with the board.
 *
 */
public interface Effect extends Piece {
    /**
     * Should this effect be displayed above or below an agent if an agent appears
     * on the same cell?
     * @return  true if this effect should appear above the agent
     */
    public boolean isAboveAgent();
}
