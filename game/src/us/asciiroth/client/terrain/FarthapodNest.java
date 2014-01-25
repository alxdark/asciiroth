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
import static us.asciiroth.client.core.Color.DARKSLATEBLUE;
import static us.asciiroth.client.core.Color.GRASS;
import static us.asciiroth.client.core.Color.LIGHTSTEELBLUE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.Farthapod;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * An <code>AgentCreator</code> specifically for <code>Farthapod</code>s. It listens for 
 * color events, presumably triggered by dying <code>Farthapod</code>s, and creates replacements. 
 *
 */
public class FarthapodNest extends AbstractTerrain implements ColorListener {

    private final Farthapod farthapod;
    
    /**
     * Constructor.
     * @param color
     */
    public FarthapodNest(Color color) {
        // It looks like it's on grass, damn it. This is a recurring problem... what
        // if you want to go to the bushes.
        super("Farthapod Nest", 0, color, 
            new Symbol("&equiv;", "&#8779;", LIGHTSTEELBLUE, BLACK, DARKSLATEBLUE, GRASS));
        this.farthapod = (Farthapod)Registry.get().getPiece("Farthapod|"+color.getName());
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return (agent instanceof Farthapod);
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        if (cell.getAgent() == null) {
            cell.setAgent(this.farthapod);
        }
    }
    /**
     * The farthapods can get out, however.
     */
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        
    }
    public static final Serializer<FarthapodNest> SERIALIZER = new BaseSerializer<FarthapodNest>() {
        public FarthapodNest create(String[] args) {
            return new FarthapodNest(Color.byName(args[1]));
        }
        public FarthapodNest example() {
            return new FarthapodNest(Color.STEELBLUE);
        }
        public String store(FarthapodNest fn) {
            return Util.format("FarthapodNest|{0}", fn.color.getName());
        }
        public String template(String type) {
            return "FarthapodNest|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
