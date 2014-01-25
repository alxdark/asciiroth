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

/**
 * A trigger that will fire a color event on the board one and only one 
 * time. When it fires it will also optionally show a message. 
 * <p> 
 * There are many ways we could ensure that the trigger fires one time.
 * A flag is set (the trigger's key); this ensures that the trigger will
 * only fire once, even if it occurs on different boards.
 *
 */
public class TriggerOnce extends Trigger {

    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param message
     */
    TriggerOnce(Terrain terrain, Color color, String message) {
        super(terrain, color, message);
    }
    TriggerOnce(Terrain terrain, Color color) {
        this(terrain, color, null);
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        event.getBoard().fireColorEvent(event, color, cell);
        if (Util.isNotBlank(message)) {
            Events.get().fireModalMessage(message);
        }
        TerrainUtils.removeDecorator(event, this);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TriggerOnce)) return false;
        TriggerOnce that = (TriggerOnce)obj;
        return (color == that.color && message == that.message);
    }
    /** Type serializer. */
    public static final Serializer<TriggerOnce> SERIALIZER = new BaseSerializer<TriggerOnce>() {
        public TriggerOnce create(String[] args) {
            if (args.length == 4) {
                return new TriggerOnce(unescTerrain(args[1]), Color.byName(args[2]), args[3]);    
            } else if (args.length == 3) {
                return new TriggerOnce(unescTerrain(args[1]), Color.byName(args[2]));
            }
            return null;
        }
        public TriggerOnce example() {
            return new TriggerOnce(new Wall(), Color.STEELBLUE, "");
        }
        public String store(TriggerOnce t) {
            if (t.message != null) {
                return Util.format("TriggerOnce|{0}|{1}|{2}",
                    esc(t.terrain), t.color.getName(), t.message);
            }
            return Util.format("TriggerOnce|{0}|{1}",
                esc(t.terrain), t.color.getName());
        }
        public String template(String type) {
            return "TriggerOnce|{terrain}|{color}|{message?}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
