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
package us.asciiroth.client.terrain.triggers;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * Trigger that will fire one time, if the player has a specific flag or a specific item. 
 *
 */
public class TriggerOnceIf extends TriggerOnce {

    private final String test;
    
    /**
     * Constructor.
     * @param terrain
     * @param test      the name of a flag or an item that must be present
     *                  for this trigger to fire.
     * @param color
     * @param message
     */
    private TriggerOnceIf(Terrain terrain, String test, Color color, String message) {
        super(terrain, color, message);
        this.test = test;
    }
    private TriggerOnceIf(Terrain terrain, String test, Color color) {
        this(terrain, test, color, null);
    }
    /**
     * A test value; there must be a flag or an item by this name in order for 
     * the trigger to fire.
     * @return      the value needed for this trigger to fire.
     */
    public String getTest() {
        return test;
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        if (player.matchesFlagOrItem(test)) {
            super.onEnterInternal(event, player, cell, dir);    
        }
    }
    /** Type serializer. */
    public static final Serializer<TriggerOnceIf> SERIALIZER = new BaseSerializer<TriggerOnceIf>() {
        public TriggerOnceIf create(String[] args) {
            if (args.length == 5) {
                return new TriggerOnceIf(unescTerrain(args[1]), args[2], Color.byName(args[3]), args[4]);    
            } else if (args.length == 4) {
                return new TriggerOnceIf(unescTerrain(args[1]), args[2], Color.byName(args[3]));
            }
            return null;
        }
        public TriggerOnceIf example() {
            return new TriggerOnceIf(new Wall(), "value", Color.STEELBLUE, "");
        }
        public String store(TriggerOnceIf t) {
            if (t.message != null) {
                return Util.format("TriggerOnceIf|{0}|{1}|{2}|{3}",
                    esc(t.terrain), t.test, t.color.getName(), t.message);
            }
            return Util.format("TriggerOnceIf|{0}|{1}|{2}",
                esc(t.terrain), t.test, t.color.getName());
        }
        public String template(String type) {
            return "TriggerOnceIf|{terrain}|{testValue}|{color}|{message?}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
