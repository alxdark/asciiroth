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
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.TerrainUtils;
import us.asciiroth.client.terrain.Wall;

public class TriggerOnceIfNot extends TriggerOnce {

    private final String test;
    
    /**
     * Constructor.
     * @param terrain
     * @param test      the name of a flag or an item that would prevent this 
     *                  trigger from firing. 
     * @param color
     * @param message
     */
    private TriggerOnceIfNot(Terrain terrain, String test, Color color, String message) {
        super(terrain, color, message);
        this.test = test;
    }
    private TriggerOnceIfNot(Terrain terrain, String test, Color color) {
        this(terrain, test, color, null);
    }
    /**
     * A test value; if this value is present (either as a flag, or as the name of 
     * an item), it will prevent the trigger from firing. 
     * @return      the value that prevents this trigger from firing. 
     */
    public String getTest() {
        return test;
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        if (!player.matchesFlagOrItem(test)) {
            // Now only fire if the key for this trigger instance hasn't already
            // been added as an invisible flag.
            event.getBoard().fireColorEvent(event, color, cell);
            String message = getMessage();
            if (Util.isNotBlank(message)) {
                Events.get().fireModalMessage(message);
            }
            TerrainUtils.removeDecorator(event, this);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TriggerOnceIfNot)) return false;
        TriggerOnceIfNot that = (TriggerOnceIfNot)obj;
        return (color == that.color && message == that.message && test == that.test);
    }
    /** Type serializer. */
    public static final Serializer<TriggerOnceIfNot> SERIALIZER = new BaseSerializer<TriggerOnceIfNot>() {
        public TriggerOnceIfNot create(String[] args) {
            if (args.length == 5) {
                return new TriggerOnceIfNot(unescTerrain(args[1]), args[2], Color.byName(args[3]), args[4]);    
            } else if (args.length == 4) {
                return new TriggerOnceIfNot(unescTerrain(args[1]), args[2], Color.byName(args[3]));
            }
            return null;
        }
        public TriggerOnceIfNot example() {
            return new TriggerOnceIfNot(new Wall(), "test", Color.STEELBLUE, "");
        }
        public String store(TriggerOnceIfNot t) {
            if (t.message != null) {
                return Util.format("TriggerOnceIfNot|{0}|{1}|{2}|{3}",
                    esc(t.terrain), t.test, t.color.getName(), t.message);
            }
            return Util.format("TriggerOnceIfNot|{0}|{1}|{2}",
                esc(t.terrain), t.test, t.color.getName());
        }
        public String template(String type) {
            return "TriggerOnceIfNot|{terrain}|{testValue}|{color}|{message?}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
};
}
