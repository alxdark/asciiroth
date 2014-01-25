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

import us.asciiroth.client.Util;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Serializer;

/**
 * Somewhat simplifies the NPC serializers, which hold the most state of 
 * any piece in the game.
 *
 * @param <T>
 */
public abstract class NPCSerializer<T extends NPC> extends BaseSerializer<T> implements Serializer<T> {
    
    public String store(T a) {
        String type = Util.getType(a);
        return (a.questColor != null && a.doneColor != null && a.inQuestMsg != null && a.flag != null) ?
            Util.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}", type, a.state.getName(), a.getColor().getName(), 
                a.message, a.questColor.getName(), a.doneColor.getName(), a.inQuestMsg, a.flag) :
            Util.format("{0}|{1}|{2}|{3}", type, a.state.getName(), a.getColor().getName(), a.message);
    }
    public String template(String type) {
        return type+"|{state}|{color}|{message} [|{questColor}|{doneColor}|{inQuestMsg}|{flag}]";
    }
    public String getTag() {
        return "People";
    }
}
