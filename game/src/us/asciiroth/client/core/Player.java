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

import static us.asciiroth.client.core.Color.GREEN;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.POISONED;
import static us.asciiroth.client.core.Flags.WATER_RESISTANT;
import static us.asciiroth.client.core.Flags.WEAK;

import java.util.HashMap;
import java.util.Map;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.effects.Fade;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.BlueRing;
import us.asciiroth.client.items.MirrorShield;
import us.asciiroth.client.items.Parabullet;
import us.asciiroth.client.items.PurpleMushroom;
import us.asciiroth.client.items.RedRing;
import us.asciiroth.client.items.Stoneray;
import us.asciiroth.client.ui.InputManager;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;

/**
 * This represents both the player, and the state of the game for the 
 * player. Not to be confused with the Game object which is the primary
 * controller component for the game.
 *  
 */
public class Player implements Agent, Animated {
    public static final int DETECT_HIDDEN_RANGE = 3;
    public static final int MAX_HEALTH = 255;

    private String name;
    private InputManager inputManager;
    private Map<String, String> unsavedMaps;
    private PlayerBag bag;
    private int health;
    private String scenarioURL;
    private String boardID;
	private int startX;
	private int startY;
    private int damageCountDown;
    private int healCountDown;
    private int flags;
    
    private PlayerSymbol symbol;
    
	public Player(InputManager im, String name, String scenarioURL, String boardID, int x, int y) {
		this.inputManager = im;
        this.bag = new PlayerBag();
        this.health = MAX_HEALTH;
        this.unsavedMaps = new HashMap<String, String>();
        this.symbol = new PlayerSymbol();
        this.name = name;
        this.scenarioURL = scenarioURL;
        this.boardID = boardID;
        this.startX = x;
        this.startY = y;
        this.flags = (NOT_EDITABLE | ORGANIC | PLAYER);
        
        // For IM to update key status
        Events.get().fireFlagsChanged(this);
	}
    // The player as game state store
    public boolean is(int flag) {
        return (flags & flag) == flag;
    }
    public boolean not(int flag) {
        return (flags & flag) != flag;
    }
    public void add(int flag) { 
        flags |= flag;
        Events.get().fireFlagsChanged(this);
    }
    public void remove(int flag) { 
        flags &= ~flag; 
        Events.get().fireFlagsChanged(this);
    }
    public int getFlags() {
        return flags;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name.trim();
    }
    public Color getColor() {
        return Color.NONE;
    }
    public String getBoardID() {
        return boardID;
    }
    public void setBoardID(String url) {
        this.boardID = url;
    }
    public String getScenarioURL() {
        return (scenarioURL == null) ? "" : scenarioURL;
    }
    public void setScenarioURL(String base) {
        this.scenarioURL = base;
    }
    public Map<String, String> getUnsavedMaps() {
        return unsavedMaps;
    }
    public void setUnsavedMaps(Map<String, String> unsavedMaps) {
        this.unsavedMaps = unsavedMaps;
    }
    public PlayerBag getBag() {
        return bag;
    }
    public void setHealth(int health) {
        this.health = health;
        Events.get().firePlayerChanged(this);
    }
    public int changeHealth(int delta) {
        if (delta == 0) {
            return health;
        }
        // The game uses positive numbers for the changeHealth delta,
        // these are treated as percentage chances to kill all agents
        // except the player, where we just subtract the delta.
        int old = health;
        health = Util.inRange(health-delta, 0, MAX_HEALTH);
        if (health < old) {
            damageCountDown = 2;
        } else if (old < health) {
            healCountDown = 2;
        }
        // This is annoying, but short of passing a context object into 
        // every friggin' method... or having actual state.
        Events.get().fireCellChanged(Game.get().getBoard().getCurrentCell());
        Events.get().firePlayerChanged(this);
        if (health <= 0) {
        	Game.get().gameOver("../../dead.html", false);
        }
        return health;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }
    public void setStartXY(int x, int y) {
        this.startX = x;
        this.startY = y;
    }
    
    public Symbol getSymbol() {
        return symbol;
    }
    public boolean canEnter(Direction direction, Cell from, Cell to) {
        return true;
    }
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Item item = getBag().getSelected();
        item.onHit(event, agentLoc, agent);
    }
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    public void onDie(Event event, Cell agentLoc) {
    }
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        Item selected = getBag().getSelected();
        // NOTE: here I think instanceof is appropriate because we're looking for very 
        // specific things, not capabilities.
        if ((item instanceof Parabullet || item instanceof Stoneray) && selected instanceof MirrorShield) {
            Game.get().shoot(event, itemLoc, this, item, dir.getReverseDirection());
        }
        // Cancel event after shooting or else shooting itself is canceled
        event.cancel(); 
    }

    public boolean randomSeed() {
    	return false;
    }
    
    public void onFrame(Context ctx, Cell cell, int frame) {
        // While damage count down is positive, decrement and then at zero, re-render
        if (damageCountDown > 0 && --damageCountDown == 0) {
            Events.get().fireRerender(cell, this, getSymbol());
        }
        if (healCountDown > 0 && --healCountDown == 0) {
            Events.get().fireRerender(cell, this, getSymbol());
        }
        inputManager.processNextAction();
    }
    
    public boolean testResistance(int resistance) {
        if (is(resistance)) {
            // But, these resistances are never actually tested anywhere...
            if (is(FIRE_RESISTANT) && bag.getSelected() instanceof RedRing ||
                is(WATER_RESISTANT) && bag.getSelected() instanceof BlueRing) {
                return true;
            }
            if (Random.nextInt(100) < 15) {
                remove(resistance);
            }
            return true;
        }
        return false;
    }
    /**
     * Cure the player of weakness. If the player is cured of weakness, 
     * the item is consumed.
     * @param event
     * @param healer
     */
    public void cureWeakness(Event event, Item healer) {
        if (not(WEAK)) {
            event.cancel("You feel pretty good right now, let's save it");
        } else {
            remove(WEAK);
            Events.get().fireModalMessage("Your energy is restored.");
            bag.remove(healer);
        }
    }
    
    /**
     * Cure the player of poisoning. If the player is cured of poisoning, 
     * the item is consumed.
     * @param event
     * @param healer
     */
    public void curePoison(Event event, Item healer) {
        if (not(POISONED)) {
            if (healer instanceof PurpleMushroom) {
                event.cancel("Let's not eat strange fungus until we're really desperate, shall we?");    
            } else {
                event.cancel("If you were poisoned, this would have cured it");
            }
        } else {
            remove(POISONED);
            Events.get().fireModalMessage("The sick feeling goes away. ");
            bag.remove(healer);
        }
    }
    
    /**
     * Heal the player. If healing occurs, the item doing the healing
     * is consumed.
     * @param event
     * @param healer
     * @param amount
     */
    public void heal(Event event, Item healer, int amount) {
        int currentValue = changeHealth(0);
        if (is(POISONED)) {
            event.cancel("You can't heal, you're poisoned.");
        } else if (currentValue == Player.MAX_HEALTH) {
            event.cancel("You're at full health already; let's skip it.");
        } else {
            int newValue = changeHealth(-amount);
            Events.get().fireMessage(event.getBoard().getCurrentCell(),
                "Healed " + (newValue-currentValue) + " points.");
            bag.remove(healer);
        }
    }
    
    /**
     * After picking up an item, adjust inventory if the player is weak so that he/she
     * still only holds one item. 
     * @param event
     * @param loc
     * @param item
     * @return  true if one item inventory was enforced, false if nothing special happened
     */
    public boolean enforceWeakness(Event event, Cell loc, Item item) {
        if (not(WEAK) || bag.size() <= 1) {
            return false;
        }
        // What a mess.
        event.cancel("You're weak, you can only hold one thing at a time");
        boolean emptyHanded = false;
        if (bag.getSelected() == PlayerBag.EMPTY_HANDED) {
            emptyHanded = true;
            bag.select(1);
        }
        Item oldItem  = bag.exchange(item);
        loc.getBag().remove(item);
        loc.getBag().add(oldItem);
        if (emptyHanded) {
            bag.selectEmptyHanded();
        }
        return true;
    }
    
    /**
     * Weaken the player, which will also have the consequence that
     * the player will drop everything except the currently equipped
     * item.
     * @param cell
     */
    public void weaken(Cell cell) {
        if (not(WEAK)) {
            add(WEAK);
            for(int i=bag.size()-1; i >= 0; i--) {
                Bag.Entry<Item> entry = bag.asEntryList().get(i);
                if (!bag.isSelected(entry) && entry.getPiece() != PlayerBag.EMPTY_HANDED) {
                    for (int j=0, len = entry.getCount(); j < len; j++) {
                        bag.remove(entry.getPiece());
                        cell.getBag().add(entry.getPiece());
                    }
                }
            }
            Events.get().fireMessage(cell, 
                "You are weak; you can only hold one item at a time");
        }
    }

    public void teleport(final Event event, Direction dir, final String boardID, final int x, final int y) {
        event.cancel();
        Cell current = event.getBoard().getCurrentCell();
        current.removeAgent(this);
        Fade fade = new Fade(getSymbol(), new Command() {
            public void execute() {
                Game.get().teleport(boardID, x, y);
            }
        });
        current.getAdjacentCell(dir).getEffects().add(fade);
    }
    
    public boolean matchesFlagOrItem(String token) {
        return (bag.getByName(token) != null) || Flags.matches(token, flags);
    }
    
    private class PlayerSymbol extends Symbol {
        private PlayerSymbol() {
            super("@", WHITE);
        }
        @Override
        public Color getColor(boolean outside) {
            return (outside) ? 
                new Color(MAX_HEALTH-health, 0, 0) : new Color(MAX_HEALTH, health, health);
        }
        @Override
        public Color getBackground(boolean outside) {
            if (healCountDown > 0) {
                return GREEN;
            } else if (damageCountDown > 0) {
                return RED;
            }
            return null;
        }
    }
}
