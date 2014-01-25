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

import static us.asciiroth.client.core.Color.MEDIUMAQUAMARINE;
import static us.asciiroth.client.core.Color.MEDIUMSPRINGGREEN;
import static us.asciiroth.client.core.Flags.DETECT_HIDDEN;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When item is wielded, the player can see secret passages. 
 */
public class GlassEye extends AbstractItem {

    private GlassEye() {
        super("The Glass Eye", 0, 
            new Symbol("&Theta;", MEDIUMSPRINGGREEN, null, MEDIUMAQUAMARINE, null));
    }
    @Override
    public void onSelect(Context context, Cell cell) {
        context.getPlayer().add(DETECT_HIDDEN);
        AgentUtils.updateAdjacent(context, Player.DETECT_HIDDEN_RANGE);
    }
    @Override
    public void onDeselect(Event event, Cell cell) {
        event.getPlayer().remove(DETECT_HIDDEN);
        AgentUtils.updateAdjacent(event, Player.DETECT_HIDDEN_RANGE);
    }
    @Override
    public void onDrop(Event event, Cell cell) {
        AgentUtils.updateAdjacent(event, Player.DETECT_HIDDEN_RANGE);
    }
    @Override
    public void onThrow(Event event, Cell cell) {
        AgentUtils.updateAdjacent(event, Player.DETECT_HIDDEN_RANGE);
    }
    @Override
    public void onUse(Event event) {
        event.cancel("Just holding it brings power (look at your flags)");
    }

    /** Type serializer. */
    public static final Serializer<GlassEye> SERIALIZER = new TypeOnlySerializer<GlassEye>() {
        public GlassEye create(String[] args) {
            return new GlassEye();
        }
        public String getTag() {
            return "Artifacts";
        }
    };

}
