package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Wall;

/**
 * A player gate decorates another terrain and tests to see if the player 
 * has a specific flag or item. It will block movement if the player does 
 * not, and optionally show a modal message to the user if he/she is blocked. 
 * (The message is modal to match the behavior of talking to an NPC). 
 * <p>
 * This can be used in tandom with NPCs to simulate situations where an 
 * agent will not allow the player to pass until a condition is met. It 
 * can probably be used for other things as well. 
 */
public class PlayerGate extends Decorator {

    private final String flag;
    private final String message;
    
    public PlayerGate(Terrain terrain, String flag, String message) {
        super(terrain, 0);
        if (flag == null) {
            throw new RuntimeException("Flag cannot be null");
        }
        this.flag = flag;
        this.message = message;
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        if (!player.matchesFlagOrItem(flag)) {
            event.cancel();
            if (Util.isNotBlank(message)) {
                Events.get().fireModalMessage(message);    
            }
        }
    }
    public Terrain proxy(Terrain terrain) {
        return new PlayerGate(terrain, this.flag, this.message);
    }
    /** Type serializer. */
    public static final Serializer<PlayerGate> SERIALIZER = new BaseSerializer<PlayerGate>() {
        public PlayerGate create(String[] args) {
            return (args.length == 4) ?
                new PlayerGate(unescTerrain(args[1]), args[2], args[3]) :
                new PlayerGate(unescTerrain(args[1]), args[2], null);
        }

        public PlayerGate example() {
            return new PlayerGate(new Wall(), "", "");
        }
        public String getTag() {
            return "Utility Terrain";
        }
        public String store(PlayerGate pg) {
            return (pg.message == null) ?
                Util.format("PlayerGate|{0}|{1}", esc(pg.terrain), pg.flag) :
                Util.format("PlayerGate|{0}|{1}|{2}", esc(pg.terrain), pg.flag, pg.message);
        }
        public String template(String type) {
            return "PlayerGate|{terrain}|{flag}|{message?}";
        }
    };
}
