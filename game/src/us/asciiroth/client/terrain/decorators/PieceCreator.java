package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.items.Bow;
import us.asciiroth.client.terrain.Floor;

/**
 * A utility terrain. When it receives a color event, it creates an item 
 * or agent at the cell it decorates, or at the location of the event, 
 * depending on the configuration. It will create an item regardless of 
 * whether the item is there or not, though it will not overwrite an agent
 * if it is in that location.
 *
 */
public class PieceCreator extends Decorator {

    private final Piece piece;
    private final boolean atOrigin;
    
    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param piece
     * @param atOrigin
     */
    public PieceCreator(Terrain terrain, Color color, Piece piece, boolean atOrigin) {
        super(terrain, 0, color);
        this.piece = piece;
        this.atOrigin = atOrigin;
    }
    public Terrain proxy(Terrain terrain) {
        return new PieceCreator(terrain, color, piece, atOrigin);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        Cell target = (atOrigin) ? origin : cell;
        if (piece instanceof Agent && target.getAgent() == null) {
            target.setAgent((Agent)piece);
        } else if (piece instanceof Item) {
            target.getBag().add((Item)piece);    
        }
    }
    /** Type serializer. */
    public static final Serializer<PieceCreator> SERIALIZER = new BaseSerializer<PieceCreator>() {
        public PieceCreator create(String[] args) {
            return new PieceCreator(unescTerrain(args[1]), Color.byName(args[2]), 
                unesc(args[3]), isTrue(args[4]));
        }
        public PieceCreator example() {
            return new PieceCreator(new Floor(), Color.STEELBLUE, new Bow(), false);
        }
        public String store(PieceCreator pc) {
            return Util.format("PieceCreator|{0}|{1}|{2}|{3}", esc(pc.terrain), pc.color.getName(), esc(pc.piece), 
                pc.atOrigin ? "true":"false");
        }
        public String template(String type) {
            return "PieceCreator|{terrain}|{color}|{piece}|{atOrigin}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
