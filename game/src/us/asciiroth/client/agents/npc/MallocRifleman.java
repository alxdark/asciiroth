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
 * Shoots bullets.
 */
public class MallocRifleman extends Rifleman {
    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     */
    public MallocRifleman(State state, Color color, String message) {
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
    public MallocRifleman(State state, Color color, String message, Color questColor, Color doneColor, String inQuestMsg, String flag) {
        super("Malloc Carbiner", state, color, message, questColor, doneColor, inQuestMsg, flag, 
            CombatStats.RIFLEMAN_CTBH, CombatStats.RIFLEMAN_DAMAGE, "&Ocirc;"); // "&Aring;"
    }
    /** Type serializer. */
    public static final Serializer<? extends NPC> SERIALIZER = new NPCSerializer<MallocRifleman>() {
        public MallocRifleman create(String[] args) {
            return (args.length == 8) ?
                new MallocRifleman(State.byName(args[1]), Color.byName(args[2]), args[3], Color.byName(args[4]), Color.byName(args[5]), args[6], args[7]) :
                new MallocRifleman(State.byName(args[1]), Color.byName(args[2]), args[3]);
        }
        public MallocRifleman example() {
            return new MallocRifleman(State.OFF, NONE, "I am a test");
        }
        @Override
        public String getTag() {
            return "Mallocs";
        }
    };
}
