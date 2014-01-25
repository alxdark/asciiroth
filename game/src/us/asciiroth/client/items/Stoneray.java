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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import static us.asciiroth.client.core.Color.BLUE;
import static us.asciiroth.client.core.Color.LIGHTBLUE;
import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

import com.google.gwt.user.client.Random;

/**
 * A bullet that will turn its target to stone. For the player that's pretty
 * much <em>Game Over</em>. However, paralysis resistance will prevent being 
 * turned to stone.
 */
public class Stoneray extends AbstractItem {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&times;", LIGHTBLUE, null, BLUE, null),
        new Symbol("+", LIGHTBLUE, null, BLUE, null)
    };
    
    /** Constructor. */
    Stoneray() {
        super("Stoning Bullet", AMMUNITION | NOT_EDITABLE, SYMBOLS[0]);
    }
    @Override
    public void onHit(Event event, Cell agentLoc, Agent agent) {
        AgentUtils.turnToStone(agentLoc, agent, NONE);
    }
    @Override
    public Symbol getSymbol() {
        return SYMBOLS[Random.nextInt(2)];
    }

    /** Type serializer. */
    public static final Serializer<Stoneray> SERIALIZER = new TypeOnlySerializer<Stoneray>() {
        public Stoneray create(String[] args) {
            return new Stoneray();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
