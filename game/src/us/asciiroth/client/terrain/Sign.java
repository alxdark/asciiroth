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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.decorators.Decorator;

/**
 * A sign that will show a message when the player walks onto it. Designed
 * so that the sign can be on any of the traversable terrains.
 */
public class Sign extends Decorator {

    private String message;
    
    public Sign(Terrain terrain, String message) {
        super(terrain, "Sign", 0, NONE, new Symbol("&#8962;", 
            WHITE, terrain.getSymbol().getBackground(false), 
            BLACK, terrain.getSymbol().getBackground(true)));
        this.message = message;
    }
    @Override
    protected void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        Events.get().fireMessage(cell, this.message);
    }
    
    public Terrain proxy(Terrain terrain) {
        return new Sign(terrain, message);
    }
    
    /** Type serializer. */
    public static final Serializer<Sign> SERIALIZER = new BaseSerializer<Sign>() {
        public Sign create(String[] args) {
            return new Sign(unescTerrain(args[1]), args[2]);
        }
        public Sign example() {
            return new Sign(new Floor(), "test");
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(Sign s) {
            return Util.format("Sign|{0}|{1}", esc(s.terrain), s.message);
        }
        public String template(String type) {
            return "Sign|{terrain}|{message}";
        }
        
    };
}
