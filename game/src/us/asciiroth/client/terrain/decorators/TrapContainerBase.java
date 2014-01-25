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
package us.asciiroth.client.terrain.decorators;

import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Flags.DETECT_HIDDEN;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.EnergyCloud;
import us.asciiroth.client.effects.Open;
import us.asciiroth.client.effects.PoisonCloud;
import us.asciiroth.client.effects.ResistancesCloud;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Chest;
import us.asciiroth.client.terrain.Crate;

import com.google.gwt.user.client.Command;

/**
 * A container (chest or crate) that is trapped and explodes when opened. 
 * (Currently, a poison cloud, but that will configurable). It's rather 
 * easy to avoid, if you try to avoid it, so I wonder if detect hidden 
 * is even necessary here. 
 */
public abstract class TrapContainerBase extends Decorator {

    protected final int POISON = 1;
    protected final int ENERGY = 2;
    protected final int RESISTANCES = 3;
    
    protected int trapType;
    protected final Symbol detected;
    
    public TrapContainerBase(Terrain terrain) {
        super(terrain, 0);
        assert terrain != null  : "TrapContainerBase: terrain is null";
        
        Symbol s = terrain.getSymbol();
        this.detected = new Symbol(s.getEntity(),s.getAdvanced(),s.getColor(false), RED,s.getColor(true), RED);
    }
    
    @Override
    protected void onEnterInternal(Event event, final Player player, final Cell cell, Direction dir) {
        for (int i=0, len = cell.getEffects().size(); i < len; i++) {
            Effect effect = cell.getEffects().get(i);
            if (effect instanceof Open) {
                final Open oldOpen = (Open)effect;
                if (!(oldOpen.getCommand() instanceof TrappedOpenCommand)) {
                    TrappedOpenCommand c = new TrappedOpenCommand(cell, oldOpen.getCommand());
                    cell.getEffects().remove(effect);
                    cell.getEffects().add(new Open(c));
                    return;
                }
            }
        }
    }
    @Override
    public void onAdjacentToInternal(Context context, Cell cell) {
        if (context.getPlayer().is(DETECT_HIDDEN) && 
            (terrain instanceof Chest || terrain instanceof Crate)) {
            Events.get().fireRerender(cell, this, detected);
        }
    }
    @Override
    protected void onNotAdjacentToInternal(Context context, Cell cell) {
        Events.get().fireRerender(cell, this, terrain.getSymbol());
    }
    private class TrappedOpenCommand implements Command {
        private Cell cell;
        private Command command;
        public TrappedOpenCommand(Cell cell, Command command) {
            assert cell != null     : "TrappedOpenCommand: cell is null";
            assert command != null  : "TrappedOpenCommand: command is null";
            this.cell = cell;
            this.command = command;
        }
        public void execute() {
            if (trapType == POISON) {
                Events.get().fireMessage(cell, "A poison trap!");
                cell.createCloud(PoisonCloud.class);
            } else if (trapType == ENERGY){
                Events.get().fireMessage(cell, "An energy trap!");
                cell.createCloud(EnergyCloud.class);
            } else if (trapType == RESISTANCES){
                Events.get().fireMessage(cell, "A resistance trap!");
                cell.createCloud(ResistancesCloud.class);
            }
            command.execute();
        }
    }
}
