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

import static us.asciiroth.client.core.Color.FIREBRICK;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.items.Fireball;

/**
 * A fire-breathing behemoth with very good range and tracking but short firing range.
 * Also it is currently pretty slow. Fires a color event when killed.
 *
 */
public class Thermadon extends AbstractAnimatedAgent {

    private static final Targeting SHOOT_TARGETING = 
        new Targeting().attackPlayer(6);
    private static final Targeting MOVE_TARGETING = 
        new Targeting().dodgeBullets(90).attackPlayer(14).moveRandomly().trackPlayer();
    
    private final Fireball bullet;
    
    /**
     * Constructor.
     * @param color
     */
    public Thermadon(Color color) {
        super("Thermadon", (CARNIVORE | ORGANIC | FIRE_RESISTANT), color, 
            CombatStats.THERMADON_CTBH, new Symbol("&ETH;", FIREBRICK));
        this.bullet = (Fireball)Registry.get().getPiece(Fireball.class);
    }
    
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.THERMADON_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 15 == 0) {
            Game.get().shoot(cell, this, bullet, SHOOT_TARGETING);
        } else if (frame % 7 == 0) {
            Game.get().agentMove(cell, this, MOVE_TARGETING);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Thermadon> SERIALIZER = new BaseSerializer<Thermadon>() {
        public Thermadon create(String[] args) {
            return new Thermadon(Color.byName(args[1]));
        }
        public Thermadon example() {
            return new Thermadon(Color.NONE);
        }
        public String store(Thermadon t) {
            return "Thermadon|"+t.color.getName();
        }
        public String template(String type) {
            return "Thermadon|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
