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
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.LESSNEARBLACK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.Util;
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
 * A statue. Statues represent an agent, and in fact, can be turned
 * into that agent on a color event. However, you can't wrap a player
 * with this currently because the player cannot be serialized like 
 * that.
 *
 */
public class Statue extends AgentProxy implements ColorListener {

    private final Color color;
    private final String name;
    private final Symbol symbol;
    
    /**
     * Constructor.
     * @param agent
     * @param color
     */
    public Statue(Agent agent, Color color) {
        super(agent, 0);
        this.color = color;
        this.name = agent.getName() + " Statue";
        this.symbol = new Symbol(agent.getSymbol().getAdjustedEntity(), LESSNEARBLACK, null, BUILDING_WALL, null);
    }
    @Override
    public int changeHealth(int value) {
        return 100; 
    }
    @Override
    public Color getColor() {
        return color;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public Symbol getSymbol() {
        return symbol;
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    public boolean randomSeed() {
    	return false;
    }
    @Override
    public void onFrame(Context ctx, Cell cell, int frame) {
    	if (is(PLAYER)) {
    		super.onFrame(ctx, cell, frame);
    	}
    }
    /**
     * On a color trigger, the statue turns into the agent it represents.
     */
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        cell.setAgent(agent);
    }
    
    /** Type serializer. */
    public static final Serializer<Statue> SERIALIZER = new BaseSerializer<Statue>() {
        public Statue create(String[] args) {
            return new Statue(unescAgent(args[1]), Color.byName(args[2]));
        }
        public Statue example() {
            return new Statue(new Triffid(NONE), NONE);
        }
        public String store(Statue s) {
            return Util.format("Statue|{0}|{1}", esc(s.agent), s.color.getName());
        }
        public String template(String type) {
            return "Statue|{agent}|{color}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
