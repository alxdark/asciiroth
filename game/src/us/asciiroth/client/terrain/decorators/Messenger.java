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
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Wall;

/**
 * A utility terrain that shows a modal message. 
 *
 */
public class Messenger extends Decorator {

    private final String message;
    
    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param message
     */
    public Messenger(Terrain terrain, Color color, String message) {
        super(terrain, 0, color);
        if (message == null) {
            throw new RuntimeException("Message should not be null");
        }
        this.message = message;
    }
    public Terrain proxy(Terrain terrain) {
        return new Messenger(terrain, color, message);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        Events.get().fireModalMessage(message);
    }
    /** Type serializer. */
    public static final Serializer<Messenger> SERIALIZER = new BaseSerializer<Messenger>() {
        public Messenger create(String[] args) {
            return new Messenger(unescTerrain(args[1]), Color.byName(args[2]), args[3]);
        }
        public Messenger example() {
            return new Messenger(new Wall(), Color.STEELBLUE, "test");
        }
        public String store(Messenger m) {
            return Util.format("Messenger|{0}|{1}|{2}", esc(m.terrain), m.color.getName(), m.message);
        }
        public String template(String type) {
            return "Messenger|{terrain}|{color}|{message}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
