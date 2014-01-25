package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.DARKVIOLET;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import static us.asciiroth.client.core.Flags.PARALYZED;
import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * An AgentProxy decorator that renders the underlying agent paralyzed. 
 */
public class Paralyzed extends AgentProxy {

    private final Symbol symbol;
    
    /**
     * Constructor.
     * @param agent
     */
    public Paralyzed(Agent agent) {
        super(agent, NOT_EDITABLE);
        Symbol sym = agent.getSymbol();
        this.symbol = new Symbol(sym.getAdjustedEntity(), sym.getColor(false), 
            DARKVIOLET, sym.getColor(true), DARKVIOLET);
    }
    @Override
    public Symbol getSymbol() {
        return symbol;
    }
    public boolean randomSeed() {
    	return false;
    }
    @Override
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame <= 40) {
            if (is(PLAYER)) {
        		super.onFrame(ctx, cell, frame);
        	}
            return;
        }
        // After 40 frames where the agent gets no onTick method and thus
        // can't move or anything, replace Paralyzed with the original agent.
        if (is(PLAYER)) {
            ctx.getPlayer().remove(PARALYZED);
        }
        cell.setAgent(agent);
    }
    /** Type serializer. */
    public static final Serializer<Paralyzed> SERIALIZER = new BaseSerializer<Paralyzed>() {
        public Paralyzed create(String[] args) {
            return new Paralyzed(unescAgent(args[1]));
        }
        public Paralyzed example() {
            return new Paralyzed(new Triffid(NONE));
        }
        public String store(Paralyzed s) {
            return Util.format("Paralyzed|{0}", esc(s.agent));
        }
        public String template(String type) {
            return "Paralyzed|{agent}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
    
}
