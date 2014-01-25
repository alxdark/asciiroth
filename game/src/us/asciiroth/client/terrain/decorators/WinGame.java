package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * When this piece receives a color event, the player wins the game. Used 
 * in combination with triggers to end the game when a certain condition
 * is reached. The url should reference an HTML document to describe the 
 * end of the game, relative to the folder of the current scenario.
 *
 */
public class WinGame extends Decorator {

    private final String url;
    
    /**
     * Constructor. 
     * @param terrain
     * @param color
     * @param url
     */
    public WinGame(Terrain terrain, Color color, String url) {
        super(terrain, 0, color);
        this.url = url;
        if (this.url == null || !this.url.endsWith(".html")) {
            throw new RuntimeException("WinGame does not specify an URL");
        }
    }
    public Terrain proxy(Terrain terrain) {
        return new WinGame(terrain, this.color, this.url);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        Game.get().gameOver(url, true);
    }
    
    /** Type serializer. */
    public static final Serializer<WinGame> SERIALIZER = new BaseSerializer<WinGame>() {
        public WinGame create(String[] args) {
            return new WinGame(unescTerrain(args[1]), Color.byName(args[2]), args[3]);
        }
        public WinGame example() {
            return new WinGame(new Wall(), Color.NONE, "test.html");
        }
        public String getTag() {
            return "Utility Terrain";
        }
        public String store(WinGame wg) {
            return Util.format("WinGame|{0}|{1}|{2}", esc(wg.terrain), wg.color.getName(), wg.url);
        }
        public String template(String type) {
            return "WinGame|{terrain}|{color}|{url}";
        }
    };
}
