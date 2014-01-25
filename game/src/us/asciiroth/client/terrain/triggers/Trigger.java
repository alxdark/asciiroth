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
import us.asciiroth.client.terrain.Wall;
import us.asciiroth.client.terrain.decorators.Decorator;

/**
 * Triggers a color event when entered without any conditions. 
 * In this sense, not a lot different than a switch, but invisible.
 * Subclasses of trigger are generally more useful.
 */
public class Trigger extends Decorator {

    protected final String message;
    
    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param message
     */
    Trigger(Terrain terrain, Color color, String message) {
        super(terrain, 0, color);
        this.message = message;
    }
    Trigger(Terrain terrain, Color color) {
        this(terrain, color, null);
    }
    public Terrain proxy(Terrain terrain) {
        return new Trigger(terrain, color, message);
    }
    /** 
     * Get the message or URL for this trigger. Can either be the text itself 
     * (will be displayed inline) or an URL to an HTML file that will be 
     * displayed in a modal dialog.
     * 
     * @return  A message to display inline or, if the string ends in ".html", 
     *          the url to an HTML file to display in a modal dialog.
     */
    public String getMessage() {
        return message;
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        event.getBoard().fireColorEvent(event, color, cell);
        if (Util.isNotBlank(message)) {
            Events.get().fireMessage(cell, message);
        }
    }
    /** Type serializer. */
    public static final Serializer<Trigger> SERIALIZER = new BaseSerializer<Trigger>() {
        public Trigger create(String[] args) {
            if (args.length == 4) {
                return new Trigger(unescTerrain(args[1]), Color.byName(args[2]), args[3]);    
            } else if (args.length == 3) {
                return new Trigger(unescTerrain(args[1]), Color.byName(args[2]));
            }
            return null;
        }
        public Trigger example() {
            return new Trigger(new Wall(), Color.STEELBLUE, "");
        }
        public String store(Trigger t) {
            if (t.message != null) {
                return Util.format("Trigger|{0}|{1}|{2}", esc(t.terrain), t.color.getName(), t.getMessage());
            }
            return Util.format("Trigger|{0}|{1}", esc(t.terrain), t.color.getName());
        }
        public String template(String type) {
            return "Trigger|{terrain}|{color}|{message?}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
