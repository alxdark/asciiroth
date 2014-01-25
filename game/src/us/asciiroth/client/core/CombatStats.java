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

/**
 * A constants class that brings together combat and damage numbers from 
 * across the game so it is easier to tweak.
 * <p>
 * CTBH = chance to be hit, the percentage chance that the agent will 
 * be destroyed when struck by a bullet, sword, etc. A weapon's damage is
 * added to this number for the final percentage that is tested against 
 * (the higher the number, the more likely the agent will be destroyed).
 * For example, an arrow against a triffid (20 + 20) should lead to a 
 * 40% chance of destroying the triffid on each hit. 
 * <p>
 * Against the player, weapon damage is subtracted from the player's
 * health.
 */
public class CombatStats {
    public static final int INDESTRUCTIBLE =        -500;

    public static final int ASCIIROTH_CTBH =        -20;
    public static final int CEPHALID_CTBH =         0;
    public static final int CORVID_CTBH =           25;
    public static final int FARTHAPOD_CTBH =        15;
    public static final int HOOLOOVOO_CTBH =        -20;
    public static final int KILLER_BEE_CTBH =       5;
    public static final int LAVA_WORM_CTBH =        25;
    public static final int LIGHTNING_LIZARD_CTBH = 25;
    public static final int OPTILISK_CTBH =         25;
    public static final int SLEESTAK_CTBH =         15;
    public static final int SQUEAKY_CTBH =          15;
    public static final int TETRITE_CTBH =          25;
    public static final int THERMADON_CTBH =        25;
    public static final int TRIFFID_CTBH =          15;

    public static final int ARCHER_CTBH =           30;
    public static final int ARCHER_DAMAGE =         20;
    public static final int COMMONER_CTBH =         50;
    public static final int COMMONER_DAMAGE =       5;
    public static final int RIFLEMAN_CTBH =         30;
    public static final int RIFLEMAN_DAMAGE =       20;
    public static final int WIZARD_CTBH =           40;
    public static final int WIZARD_DAMAGE =         15;
    public static final int NOBLE_CTBH =            20;
    public static final int NOBLE_DAMAGE =          20;
    
    public static final int ASCIIROTH_DAMAGE =          50;
    public static final int CORVID_DAMAGE =             5;
    public static final int FARTHAPOD_DAMAGE =          5;
    public static final int GREAT_OLD_ONE_DAMAGE =      130;
    public static final int KILLER_BEE_DAMAGE =         10;
    public static final int LAVA_WORM_DAMAGE =          20;
    public static final int LIGHTNING_LIZARD_DAMAGE =   3;
    public static final int OPTILISK_DAMAGE =           40;
    public static final int PUSHER_DAMAGE =             3;
    public static final int SLEESTAK_DAMAGE =           30;
    public static final int SQUEAKY_DAMAGE =            30;
    public static final int TETRITE_DAMAGE =            10;
    public static final int THERMADON_DAMAGE =          20;
    public static final int TRIFFID_DAMAGE =            20;

    public static final int ARROW_DAMAGE =          15;
    public static final int BOULDER_DAMAGE =        30;
    public static final int CROWBAR_DAMAGE =        0;
    public static final int BULLET_DAMAGE =         20;
    public static final int DART_DAMAGE =           10;
    public static final int FIRE_DAMAGE =           30;
    public static final int FIREBALL_DAMAGE =       20;
    public static final int FISH_DAMAGE =           -10;
    public static final int HOOLOOVOO_DAMAGE =      20;
    public static final int ROCK_DAMAGE =           -5;
    public static final int SLING_ROCK_DAMAGE =     5;
    public static final int DAGGER_DAMAGE =         10;
    public static final int HAMMER_DAMAGE =         10;
    public static final int SWORD_DAMAGE =          15;
    public static final int TERMINUS_EST_DAMAGE =   50;
}
