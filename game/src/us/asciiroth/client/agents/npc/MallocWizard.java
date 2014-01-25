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
 *  Wizards are talking NPCs who are also good in combat, firing paralyzing bullets as
 *  well as fireballs.
 *
 */
public class MallocWizard extends Wizard {
    /**
     * Constructor.
     * @param state
     * @param color
     * @param message
     */
    public MallocWizard(State state, Color color, String message) {
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
    public MallocWizard(State state, Color color, String message, Color questColor, Color doneColor, String inQuestMsg, String flag) {
        super("Malloc Shaman", state, color, message, questColor, doneColor, inQuestMsg, flag, 
            CombatStats.WIZARD_CTBH, CombatStats.WIZARD_DAMAGE, "&Otilde;");
    }
    public static final Serializer<MallocWizard> SERIALIZER = new NPCSerializer<MallocWizard>() {
        public MallocWizard create(String[] args) {
            return (args.length == 8) ?
                new MallocWizard(State.byName(args[1]), Color.byName(args[2]), args[3], Color.byName(args[4]), Color.byName(args[5]), args[6], args[7]) :
                new MallocWizard(State.byName(args[1]), Color.byName(args[2]), args[3]);
        }
        public MallocWizard example() {
            return new MallocWizard(State.OFF, NONE, "I am a test");
        }
        @Override
        public String getTag() {
            return "Mallocs";
        }
    };
}
