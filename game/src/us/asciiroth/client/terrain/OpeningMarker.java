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

import static us.asciiroth.client.core.Color.BARELY_BUILDING_WALL;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Symbol;

public abstract class OpeningMarker extends AbstractTerrain {

    public OpeningMarker(String name, String entity) {
        super(name, (PENETRABLE | TRAVERSABLE),
                new Symbol(entity, NEARBLACK, null, BARELY_BUILDING_WALL, BUILDING_FLOOR));
    }
    public OpeningMarker(String name, String entity, String fancy) {
        super(name, (PENETRABLE | TRAVERSABLE),
                new Symbol(entity, fancy, NEARBLACK, null, BARELY_BUILDING_WALL, BUILDING_FLOOR));
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return (super.canEnter(agent, cell, direction) && !cell.hasOpeningEffect());
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        super.onEnter(event, player, cell, dir);
        if (cell.hasOpeningEffect()) {
            event.cancel();
        }
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        super.onAgentEnter(event, agent, cell, dir);
        if (cell.hasOpeningEffect()) {
            event.cancel();
        }
    }

}
