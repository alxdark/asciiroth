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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BLUE;
import static us.asciiroth.client.core.Color.LIGHTBLUE;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.PALEVIOLETRED;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.AbstractAnimatedAgent;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.agents.Targeting;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

import com.google.gwt.user.client.Random;

/**
 * See package description.
 *
 */
public abstract class NPC extends AbstractAnimatedAgent {
    
    private static final Color HUMAN_ALERT = new Color("Human Alert", "#000000");
    private static final Color MALLOC_ALERT = new Color("Malloc Alert", "#000000");
    private static final Targeting ENCOUNTER_TARGETING = 
        new Targeting().attackPlayer(10); // but limited below to 3 or so cells.
    
    protected final State state;
    protected final String message;
    protected final Color questColor;
    protected final Color doneColor;
    protected final Color alertColor;
    protected final String inQuestMsg;
    protected final String flag;
    protected final int damage;
    
    /**
     * Full, insane base constructor. I really wish I could expose this through sub-classes,
     * but you cannot. Constructors suck in Java.
     * @param name
     * @param state
     * @param color
     * @param flags
     * @param message
     * @param questColor
     * @param doneColor
     * @param inQuestMsg
     * @param flag
     * @param chanceToHit
     * @param symbol
     */
    protected NPC(String name, State state, Color color, int flags, String message, Color questColor, 
        Color doneColor, String inQuestMsg, String flag, int chanceToHit, int damage, 
        String symbol) {
        // These string tests are really ugly...
        super(name, flags, color, chanceToHit,
            state.isOn() ?
                new Symbol(symbol, PALEVIOLETRED, null, PALEVIOLETRED, null) :
            (questColor == NONE && doneColor != NONE) ?
                new Symbol(symbol, LIGHTBLUE, null, BLUE, null) :
                new Symbol(symbol, WHITE, null, BLACK, null)
        );
        if (state == null) {
            throw new RuntimeException("NPC must have state");
        }
        if (message == null) {
            throw new RuntimeException("NPC must have talk message");
        }

        this.state = state;
        this.message = message;
        this.damage = damage;
        this.alertColor = (name.indexOf("Malloc")==-1) ? HUMAN_ALERT : MALLOC_ALERT; 
        // These are optional.
        this.questColor = questColor;
        this.doneColor = doneColor;
        this.inQuestMsg = inQuestMsg;
        this.flag = flag;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOff()) {
            if (hasColor(questColor) && 
                AgentUtils.getDistance(cell, ctx.getBoard().getCurrentCell()) < 3 &&
                Random.nextInt(100) < 70) {
                Game.get().agentMove(cell, this, ENCOUNTER_TARGETING);
            } else {
                friendlyTurn(ctx, cell, frame);    
            }
        } else {
            hostileTurn(ctx, cell, frame);
        }
    }
    @Override
    public int changeHealth(int delta) {
        // Quest providers are indestructible
        return (isQuestProvider()) ? 1 : super.changeHealth(delta);
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        if (state.isOn()) {
            Game.get().damage(agentLoc, agent, damage);
        }
    }
    protected void hostileTurn(Context ctx, Cell cell, int frame) {}
    protected void friendlyTurn(Context ctx, Cell cell, int frame) {}

    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        super.onHitBy(event, agentLoc, agent, dir);
        if (state.isOff()) {
            Item item = event.getPlayer().getBag().getSelected();
            if (item.is(MELEE_WEAPON) && !isQuestProvider()) {
                alert(event.getBoard());
            } else {
                talk(event, agentLoc.getAdjacentCell(dir));
            }
        }
    }
    
    private boolean isQuestProvider() {
        return hasColor(questColor) || hasColor(doneColor);
    }
    
    protected void alert(Board board) {
        board.visit(new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                if (cell.getAgent() instanceof NPC) {
                    NPC agent = (NPC)cell.getAgent();
                    if (!agent.isQuestProvider() && agent.state.isOff()) {
                        cell.setAgent( AgentUtils.getAgentOtherState(agent, State.OFF) );
                    }
                }
                return false;
            }
        });
    }
    
    protected void talk(Event event, Cell cell) {
        Board board = event.getBoard();
        Player player = event.getPlayer();
        
        // Assign quest
        if (hasColor(questColor)) {
            board.fireColorEvent(event, questColor, cell);
            turnOffColor(cell, 4);
        } 
        // Quest has been completed
        else if (hasColor(doneColor) && player.matchesFlagOrItem(flag)) {
            board.fireColorEvent(event, doneColor, cell);
            turnOffColor(cell, 5);
        }
        else if (!hasColor(questColor) && hasColor(doneColor)) {
            Events.get().fireModalMessage(inQuestMsg);
        }
        // No quest colors, give normal talk message
        else if (Util.isNotBlank(message)) {
            Events.get().fireModalMessage(message);    
        }
    }
    @Override
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        super.onHitBy(event, itemLoc, item, dir);
        if (state.isOff()) {
            // Melee weapon is already accounted for by the agent hit.
            if (item.is(AMMUNITION)) {
                // This absolutely does not work if the agent proceeds to die.
                // It's been replaced by an agent that has already gotten mad.
                alert(event.getBoard());
            }
        }
    }
    protected boolean hasColor(Color color) {
        return (color != null && color != Color.NONE);
    }
    private void turnOffColor(Cell cell, int index) {
        Agent agent = cell.getAgent();
        cell.removeAgent(agent);
        
        String key = Registry.get().getKey(agent);
        String[] parts = key.split("\\|");
        parts[index] = "None";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < parts.length; i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(parts[i]);
        }
        agent = (Agent)Registry.get().getPiece( sb.toString() );
        cell.setAgent(agent);
    }
}
