package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Flags.AQUATIC;
import static us.asciiroth.client.core.Flags.ETHEREAL;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.FLIER;
import static us.asciiroth.client.core.Flags.LAVITIC;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import static us.asciiroth.client.core.Flags.WATER_RESISTANT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.ItemBag;
import us.asciiroth.client.core.AbstractPiece;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Grenade;

/**
 * A simple abstract adapter for the terrain interface. 
 */
public abstract class AbstractTerrain extends AbstractPiece implements Terrain {

    /**
     * Constructor.
     * @param name
     * @param flags
     * @param symbol
     */
    protected AbstractTerrain(String name, int flags, Symbol symbol) {
        super(name, flags, symbol);
    }
    /**
     * Constructor.
     * @param name
     * @param flags
     * @param color
     * @param symbol
     */
    protected AbstractTerrain(String name, int flags, Color color, Symbol symbol) {
        super(name, flags, color, symbol);
    }
    
    public boolean canEnter(Agent agent, Cell cell, Direction dir) {
        if (cell.getTerrain().is(AQUATIC)) {
            Cell from = cell.getAdjacentCell(dir.getReverseDirection());
            if (from.getTerrain() instanceof Raft) {
                return true;
            }
        }
        return (
             is(TRAVERSABLE) ||
            (is(PENETRABLE) && agent.is(FLIER)) ||     
            (is(LAVITIC) && agent.is(LAVITIC)) ||
            (is(AQUATIC) && agent.is(AQUATIC))
        );
    }
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return (
             is(TRAVERSABLE) ||
            (is(PENETRABLE) && agent.is(FLIER)) ||     
            (is(LAVITIC) && agent.is(LAVITIC)) ||
            (is(AQUATIC) && agent.is(AQUATIC))
        );
    }
    public void onDrop(Event event, Cell itemLoc, Item item) {
        if (itemLoc.getTerrain().is(ETHEREAL) && !(item instanceof Grenade)) {
            event.cancel();
        } else if (itemLoc.getAgent() != null && itemLoc.getAgent().is(ETHEREAL)) {
            // It's easiest to model the campfire as an agent... but it should
            // also prevent things from landing there.
            event.cancel();
        }
    }
    public void onPickup(Event event, Cell loc, Agent agent, Item item) {
    }
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (is(LAVITIC) && player.is(FIRE_RESISTANT) || 
            is(AQUATIC) && player.is(WATER_RESISTANT)) {
            return;
        }
        if (not(TRAVERSABLE)) {
            event.cancel();
            
            if (is(PENETRABLE) && !cell.isBagEmpty()) {
                ItemBag bag = cell.getBag();
                String s = (bag.size() > 1 || bag.asEntryList().get(0).getCount() > 1) ? 
                    "You pick up some stuff" :
                     bag.get(0).getDefiniteNoun("You pick up {0}");
                while (!cell.isBagEmpty()) {
                    Game.get().pickupItem(cell, 0);
                }
                Events.get().fireMessage(cell, s);
            }
        }
    }
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (is(LAVITIC) && player.is(FIRE_RESISTANT) || 
            is(AQUATIC) && player.is(WATER_RESISTANT)) {
            return;
        }
        // Certain classes like cave entrance and stairs override this very general behavior
        if (not(TRAVERSABLE) || dir.isVertical()) {
            event.cancel();
        }
    }
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (!canEnter(agent, cell, dir)) {
            event.cancel();
        }
    }
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        if (!canExit(agent, cell, dir)) {
            event.cancel();
        }
    }
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (not(PENETRABLE)) {
            event.cancel();
        }
    }
    public void onAdjacentTo(Context context, Cell cell) {
    }
    public void onNotAdjacentTo(Context context, Cell cell) {
    }
}
