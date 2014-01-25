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
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;

/**
 * A noble. Like a commoner this person runs away when things get hostile.
 * Typically I like to have a noble drop something when they die.
 */
public class MallocNoble extends Noble {

    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     */
    public MallocNoble(State state, Color color, String message) {
        this(state, color, message, null, null, null, null);
    }
    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     * @param questColor
     * @param doneColor
     * @param flag
     */
    public MallocNoble(State state, Color color, String message, Color questColor, Color doneColor, 
        String inQuestMsg, String flag) {
        super("Malloc Lord", state, color, message, questColor, doneColor, inQuestMsg, flag, 
            CombatStats.NOBLE_CTBH, CombatStats.NOBLE_DAMAGE, "&Ouml;");
    }
    /** Type serializer. */
    public static final Serializer<? extends NPC> SERIALIZER = new NPCSerializer<MallocNoble>() {
        public MallocNoble create(String[] args) {
            return (args.length == 8) ?
                new MallocNoble(State.byName(args[1]), Color.byName(args[2]), args[3], Color.byName(args[4]), Color.byName(args[5]), args[6], args[7]) :
                new MallocNoble(State.byName(args[1]), Color.byName(args[2]), args[3]);
        }
        public MallocNoble example() {
            return new MallocNoble(State.OFF, NONE, "I am a test");
        }
        @Override
        public String getTag() {
            return "Mallocs";
        }
    };
}
