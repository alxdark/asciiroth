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

import static us.asciiroth.client.core.Color.STEELBLUE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.TerrainUtils;
import us.asciiroth.client.terrain.Wall;
import us.asciiroth.client.terrain.decorators.Decorator;

/**
 * Trigger (once) when the specified item is picked up on this cell.
 * As I can't think of any interesting cases for a repeat trigger of
 * this type, I didn't create one (this trigger is used to deliver an
 * item as part of an item quest). 
 */
public class TriggerOnceOnPickup extends Decorator {

    private final String flag;
    
    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param flag
     */
    public TriggerOnceOnPickup(Terrain terrain, Color color, String flag) {
        super(terrain, 0, color);
        if (flag == null) {
            throw new RuntimeException("Must have flag");
        }
        this.flag = flag;
    }
    
    @Override
    protected void onPickupInternal(Event event, Cell loc, Agent agent, Item item) {
        if (flag.equals(item.getName()) || flag.equals(Util.getType(item))) {
            event.getBoard().fireColorEvent(event, color, loc);
            TerrainUtils.removeDecorator(event, this);
        }
    }
    public Terrain proxy(Terrain terrain) {
        return new TriggerOnceOnPickup(terrain, color, flag);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TriggerOnceOnPickup)) return false;
        TriggerOnceOnPickup that = (TriggerOnceOnPickup)obj;
        return (color == that.color && flag == that.flag);
    }
    /** Type serializer. */
    public static final Serializer<TriggerOnceOnPickup> SERIALIZER = new BaseSerializer<TriggerOnceOnPickup>() {
        public TriggerOnceOnPickup create(String[] args) {
            return new TriggerOnceOnPickup(unescTerrain(args[1]), Color.byName(args[2]), args[3]);
        }
        public TriggerOnceOnPickup example() {
            return new TriggerOnceOnPickup(new Wall(), STEELBLUE, "test");
        }
        public String getTag() {
            return "Utility Terrain";
        }
        public String store(TriggerOnceOnPickup t) {
            return Util.format("TriggerOnceOnPickup|{0}|{1}|{2}", esc(t.terrain), t.color.getName(), t.flag);
        }
        public String template(String type) {
            return "TriggerOnceOnPickup|{terrain}|{color}|{flag}";
        }
    };
}
