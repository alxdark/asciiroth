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
package us.asciiroth.client.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * In order to make effective use of proxies, limited use can be made of the
 * <code>instanceof</code> operator. Instead, flags are assigned to pieces 
 * to indicate basic characteristics of the piece.
 * 
 */
public class Flags {
    /** This piece is the player (or a proxy for the player). */
    public static final int PLAYER                          = 1 << 0;
    /** This terrain is traversable by agents. */
    public static final int TRAVERSABLE                     = 1 << 1; /* Allows travel through */
    /** Thrown items can fly over this terrain. */ 
    public static final int PENETRABLE                      = 1 << 2; /* Allows throwing through/over. */
    /** Items dropped on this terrain will disappear, rather than falling to the ground. */
    public static final int ETHEREAL                        = 1 << 3;
    /** This piece represents water, or an agent that can only move in water. */
    public static final int AQUATIC                         = (1 << 4) | PENETRABLE | ETHEREAL;
    /** This piece represents lava, or an agent that can only move in lava. */
    public static final int LAVITIC                         = (1 << 5) | PENETRABLE | ETHEREAL;
    /** This agent can fly. */
    public static final int FLIER                           = 1 << 6;
    /** This piece should not be available in the map editor. */
    public static final int NOT_EDITABLE                    = 1 << 7;
    /** This item is a form of ammunition (it is shot from another item marked as a RANGED_WEAPON. */
    public static final int AMMUNITION                      = 1 << 8;
    /** This item is a weapon. */
    public static final int WEAPON                          = 1 << 9;
    /** This item is a melee weapon. */
    public static final int MELEE_WEAPON                    = (1 << 10) | WEAPON;
    /** This item is a ranged weapon. It should return an item of type ammunition from the 
     * event <code>onFire</code> 
     */
    public static final int RANGED_WEAPON                   = (1 << 11) | WEAPON;
    /** This agent likes meat. It'll be distracted from chasing the player to chase a bone, 
     * a head, etc.
     */
    public static final int CARNIVORE						= 1 << 12;
    /** This agent is organic, and can be effected by fire. */
    public static final int ORGANIC                         = 1 << 13;
    /** This agent is pushable. */
    public static final int PUSHABLE                        = 1 << 14;
    /** This is a quality imparted to the player that makes him or her resistant to lava and fire. */
    public static final int FIRE_RESISTANT                  = 1 << 15;
    /** This is a quality imparted to the player that makes him or her able to travel in water. */
    public static final int WATER_RESISTANT                 = 1 << 16;
    /** This is a quality imparted to the player that makes him or her resistant to theft attacks. */
    public static final int THEFT_RESISTANT                 = 1 << 17;
    /** This is a quality imparted to the player that makes him or her resistant to poisoning. */
    public static final int POISON_RESISTANT                = 1 << 18; 
    /** This is a quality imparted to the player that makes him or her resistant to paralysis. */
    public static final int PARALYSIS_RESISTANT             = 1 << 19;
    /** The player is poisoned (cannot heal until the poisoning is removed). */
    public static final int POISONED                        = 1 << 20;
    /** The agent has been turned to stone. */
    public static final int TURNED_TO_STONE                 = 1 << 21;
    /** The agent has been paralyzed. */
    public static final int PARALYZED                       = 1 << 22;
    /** This is a quality imparted to the player that makes him or her able to see hidden things. */
    public static final int DETECT_HIDDEN                   = 1 << 23;
    /** This is a quality imparted to the player that makes him or her resistant to stoning. */
    public static final int STONING_RESISTANT               = 1 << 24;
    /** The player is too weak to hold or pick up anything. */
    public static final int WEAK                            = 1 << 25;
    /** 
     * A piece that enlivens ammo when it hits it, making the ammo dangerous to everyone, player
     * and agents alike. Typically ammo is not dangerous to the agent that shoots it, and non-player
     * agents can't hit each other because that would be too easy.
     */
    public static final int AMMO_ENLIVENER                  = 1 << 26;
    /** This item is meat, and CARNIVORES will chase after it rather than the player. */
    public static final int MEAT                            = 1 << 27;
    
    /** A kind of terrain that hides the items that are located on it. */
    public static final int HIDES_ITEMS                     = 1 << 28;
    /** A ranged weapon that wants to consume some kind of ammo. There is also an 
     * interface for this behavior, so this flag could go.
     */
    public static final int REQUIRES_AMMO                   = 1 << 29;
    
    /**  */
    public static final int TRANSIENT                       = 1 << 30;
    /** The terrain allows vertical movement. */
    public static final int VERTICAL                        = 1 << 31;
    
    private static final Map<Integer, String> labels = new LinkedHashMap<Integer, String>();
    static {
        labels.put(DETECT_HIDDEN, "Detect Hidden");
        labels.put(FIRE_RESISTANT, "Fire Resistant");
        labels.put(POISONED, "Poisoned");
        labels.put(PARALYSIS_RESISTANT, "Paralysis Resistant");
        labels.put(PARALYZED, "Paralyzed");
        labels.put(POISON_RESISTANT, "Poison Resistant");
        labels.put(STONING_RESISTANT, "Resistant to Stoning");
        labels.put(THEFT_RESISTANT, "Theft Resistant");
        labels.put(TURNED_TO_STONE, "Turned to Stone");
        labels.put(WATER_RESISTANT, "Water Resistant");
        labels.put(WEAK, "Weak");
    }
    
    public static boolean matches(String label, int flags) {
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            if (is(entry.getKey(), flags) && entry.getValue().equals(label)) {
                return true;
            }
        }
        return false;
    }
    
    public static int getFlag(String label) {
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            if (entry.getValue().equals(label)) {
                return entry.getKey();
            }
        }
        return -1;
    }
    
    /**
     * Generates the description of those flags that are visible to the player, that are
     * displayed in the "Flags" section of the game interface.  
     * @param flags
     * @return  a string representation of those flags visible to the player
     */
    public static final String description(int flags) {
        StringBuilder sb = new StringBuilder();
        for (int flag : labels.keySet()) {
            if (is(flag, flags)) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(labels.get(flag));
            }
        }
        if (sb.length() == 0) {
            sb.append("<em>None</em>");
        }
        return sb.toString();
    }
    public static boolean is(int flag, int flags) {
        return (flags & flag) == flag;
    }
    public static boolean not(int flag, int flags) {
        return (flags & flag) != flag;
    }
}
