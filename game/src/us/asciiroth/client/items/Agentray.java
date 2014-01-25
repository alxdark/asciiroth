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
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When Asciiroth receives a color event, it shoot a kind of ammo that
 * releases an agent when it explodes.  
 */
public class Agentray extends AbstractItem {

    private final Agent agent;
    
    /** Constructor. */
    public Agentray(Agent agent) {
        super("Agentray", AMMUNITION | NOT_EDITABLE, new Symbol("&deg;", 
            agent.getSymbol().getColor(false), null, agent.getSymbol().getColor(true), null));
        this.agent = agent;
    }
    public Agent getAgent() {
        return agent;
    }
    @Override
    public void onThrowEnd(final Event event, Cell cell) {
        event.cancel();
        if (cell.getAgent() == null) {
            cell.setAgent(agent);    
        }
    }
    /** Type serializer. */
    public static final Serializer<Weakray> SERIALIZER = new TypeOnlySerializer<Weakray>() {
        public Weakray create(String[] args) {
            return new Weakray();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
