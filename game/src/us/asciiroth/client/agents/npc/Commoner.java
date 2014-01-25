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
package us.asciiroth.client.agents.npc;

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.agents.Targeting;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;

/**
 * Commoner. When NPCs on the board go hostile, a commoner will run away
 * rather than fight (by design, it should attack if the player is standing
 * right next to it, so as not to be suicidal... not sure if this works). 
 *
 */
public class Commoner extends NPC {

    private static final Targeting WANDER_TARGETING = 
        new Targeting().moveRandomly();
    private static final Targeting FLEE_TARGETING = 
        new Targeting().fleePlayer(10).moveRandomly();
    
    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     */
    public Commoner(State state, Color color, String message) {
        this(state, color, message, null, null, null, null);
    }
    
    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     * @param questColor
     * @param doneColor
     * @param inQuestMsg
     * @param flag
     */
    public Commoner(State state, Color color, String message, Color questColor, Color doneColor,
        String inQuestMsg, String flag) {
        this("Commoner", state, color, message, questColor, doneColor, inQuestMsg, flag, 
            CombatStats.COMMONER_CTBH, CombatStats.COMMONER_DAMAGE, "A");
    }
    
    protected Commoner(String name, State state, Color color, String message, Color questColor, 
        Color doneColor, String inQuestMsg, String flag, int chanceToHit, int damage, String symbol) {
        super(name, state, color, ORGANIC, message, questColor, doneColor, inQuestMsg, flag, 
            chanceToHit, damage, symbol);
    }
    
    @Override
    protected void friendlyTurn(Context ctx, Cell cell, int frame) {
        if (frame % 10 == 0) {
            Game.get().agentMove(cell, this, WANDER_TARGETING);
        }
    }
    @Override
    protected void hostileTurn(Context ctx, Cell cell, int frame) {
        if (frame % 4 == 0) {
            Game.get().agentMove(cell, this, FLEE_TARGETING);
        }
    }
    /** Type serializer. */
    public static final Serializer<Commoner> SERIALIZER = new NPCSerializer<Commoner>() {
        public Commoner create(String[] args) {
            return (args.length == 8) ?
                new Commoner(State.byName(args[1]), Color.byName(args[2]), args[3], Color.byName(args[4]), Color.byName(args[5]), args[6], args[7]) :
                new Commoner(State.byName(args[1]), Color.byName(args[2]), args[3]);
        }
        public Commoner example() {
            return new Commoner(State.OFF, NONE, "I am a test");
        }
    };
}
