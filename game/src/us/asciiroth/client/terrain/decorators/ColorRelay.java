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
package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * A terrain that looks like the terrain it decorates, and is for all 
 * purposes the terrain it looks like, but when it receives a color event, 
 * it triggers an event of a different color. 
 * <p>
 * Why is this useful? Let's say you want to have six doors that all 
 * open with one key using a key switch. The problem is that the key can 
 * only open one door, which will consume it. With a color relay, 
 * an orange key switch can trigger an orange color event, and the relay
 * can convert this to another color, let's say a blue color event. Now 
 * an orange key can open six blue doors via the orange key switch.
 *
 */
public class ColorRelay extends Decorator {

    private final Color relayTo;
    
    private ColorRelay(Terrain terrain, Color color, Color relayTo) {
        super(terrain, 0, color);
        this.relayTo = relayTo;
    }
    public Terrain proxy(Terrain terrain) {
        return new ColorRelay(terrain, color, relayTo);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        cell.getBoard().fireColorEvent(ctx, relayTo, origin);
    }
    /** Type serializer. */
    public static final Serializer<ColorRelay> SERIALIZER = new BaseSerializer<ColorRelay>() {
        public ColorRelay create(String[] args) {
            return new ColorRelay(unescTerrain(args[1]), 
                Color.byName(args[2]), Color.byName(args[3]));
        }
        public ColorRelay example() {
            return new ColorRelay(new Wall(), Color.STEELBLUE, Color.RED);
        }
        public String store(ColorRelay cr) {
            return Util.format("ColorRelay|{0}|{1}|{2}", esc(cr.terrain), 
                cr.color.getName(), cr.relayTo.getName());
        }
        public String template(String type) {
            return "ColorRelay|{terrain}|{fromColor}|{toColor}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
