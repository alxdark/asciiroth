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
package us.asciiroth.client.ui;

import static us.asciiroth.client.core.Flags.HIDES_ITEMS;

import java.util.ArrayList;
import java.util.List;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.HasBoard;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.event.GameListener;
import us.asciiroth.client.event.MessageListener;

import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * The <code>MessageListener</code> implementation in the game. It manages a cache of 
 * <code>CellMessagePanel</code> instances, positions them so they don't overlap, 
 * and manages their state. 
 *
 */
public class MessageManager extends Timer implements GameListener, MessageListener {

    private HasBoard hasBoard;
    private CellMessageModalPanel modalPanel;
    private MessageList modalMessages;
    private List<CellMessagePanel> panels;
    
    public MessageManager(HasBoard hasBoard) {
        this.hasBoard = hasBoard;
        this.modalPanel = new CellMessageModalPanel();
        this.panels = new ArrayList<CellMessagePanel>();
        this.modalMessages = new MessageList();
        scheduleRepeating(500);
    }
    public void onGamePaused() {
        // clearAllCells();
    }
    public void onGameResumed() {
    }
    @Override
    public void run() {
        double now = Duration.currentTimeMillis();
        
        for (int i = panels.size()-1; i >=0; i--) {
            CellMessagePanel panel = panels.get(i);
            if (panel.isInUse() && panel.getTimeout() < now) {
                panel.hide();
                panels.remove(panel);
            }
        }
    }
    public void clearAllCells() {
        for (CellMessagePanel panel : panels) {
            panel.hide();
        }
    }
    
    public void modalMessage(String message) {
        modalMessages.add(message);
    }
    public void handleModalMessage() {
        if (!modalMessages.isEmpty()) {
            Cell current = hasBoard.getBoard().getCurrentCell();
            modalPanel.setMessage(current.getTd(), modalMessages.toBreakListWithEnterPrompt());
            DialogManager.get().push(modalPanel);
            modalMessages.clear();
        }
    }
    
    public void message(Cell cell, String message) {
        CellMessagePanel panel = getMessagePanel(cell.getTd());
        panel.addMessage(message);
        displayPanel(panel);
    }
    
    public void handleInventoryMessaging() {
        displayItemsAtCurrentCell();
        clearCell( hasBoard.getBoard().getCurrentCell() );
        // Any new messages need to replace existing ones.
        for (CellMessagePanel panel : panels) {
            panel.clearMessages();
        }
    }
    public void clearCurrentCell() {
        Cell current = hasBoard.getBoard().getCurrentCell();
        for (CellMessagePanel panel : panels) {
            if (panel.getTd() == current.getTd()) {
                panel.hide();
            }
        }
    }
    /**
     * Hide any messages that would obscure this cell.
     * @param cell
     */
    public void clearCell(Cell cell) {
        for (CellMessagePanel panel : panels) {
            if (panel.overlaps(cell.getTd())) {
                panel.hide();
            }
        }
    }
    private void displayItemsAtCurrentCell() {
        Cell current = hasBoard.getBoard().getCurrentCell();
        if (!current.isBagEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (current.getTerrain().is(HIDES_ITEMS)){
                sb.append("Some items are hidden here...");
            } else if (current.getBag().size() > 10) { 
                sb.append("Use 'r' to rotate items in list");
            } else {
                sb.append("Use 'p' or # to pick up item");
            }
            int len = (current.getBag().size() <= 10) ? current.getBag().size() : 10;
            for (int i=0; i < len; i++) {
                int j = (i < 9) ? (i+1) : 0;
                Bag.Entry<Item> entry = (Bag.Entry<Item>)current.getBag().asEntryList().get(i);
                sb.append("<br/>");
                sb.append(j + ". ");
                sb.append(Util.renderSymbolAndLabel(false, (Item)entry.getPiece(), entry.getCount()));
                
            }
            message(current, sb.toString());
        }
    }
    private CellMessagePanel getMessagePanel(Element el) {
        CellMessagePanel found = null;
        // Already a panel for this cell?
        for (CellMessagePanel panel : panels) {
            if (panel.getTd() == el) {
                found = panel;
                break;
            }
        }
        // No, so find a panel that's not in use
        if (found == null) {
            for (CellMessagePanel panel : panels) {
                if (!panel.isInUse()) {
                    found = panel;
                    found.setTd(el);
                    break;
                }
            }
        }
        // Not event that... have to add to cache
        if (found == null) {
            found = new CellMessagePanel();
            panels.add(found);
            found.setTd(el);
        }
        return found;
    }
    
    /**
     * Does not overlap any other CellPanels, the player's table cell,
     * and does not extend off of the page.
     * @param panel
     * @return  true if the position is good as is.
     */
    private boolean positionDoesNotOverlap(CellMessagePanel panel) {
        for (CellMessagePanel peer : panels) {
            if (peer != panel && panel.overlaps(peer)) {
                return false;
            }
        }
        // Only do this check when positioning messages off the current cell.
        // Obviously, the positions all overlap adjacent cells to the current
        // cell.
        Cell current = hasBoard.getBoard().getCurrentCell();
        if (panel.getTd() != current.getTd()) {
            for (Cell cell : current.getAdjacentCells(null)) {
                if (panel.overlaps(cell.getTd())) {
                    return false;
                }
            }
        }
        
        // Does it extend off the page? The panel's left is not less than 5 and 
        // it's top plus its height (and padding) are not greater than the window's height
        int bottom = (panel.getAbsoluteTop() + panel.getOffsetHeight() + 5);
        return (panel.getAbsoluteLeft() > 5) && (bottom < Window.getClientHeight());
    }
    
    private void displayPanel(CellMessagePanel panel) {
        // TODO: It turns out that some of the cell positions may *never* display, 
        // possibly because they always consider themselves to overlap the player?
        // No wonder this is slow... we're calculating positions over and over and over...
        for (int i=0; i < POSITIONS.length; i++) {
            POSITIONS[i].position(panel);
            if (positionDoesNotOverlap(panel)) {
                // Log.get().debug(i + ": " + panel.label.getHTML().substring(0,10));
                return;
            }
        }
        // So show it, but go through and hide anyone who is underneath the current panel,
        // because there may be... the user is triggering a lot of messages simultaneously.
        for (CellMessagePanel peer : panels) {
            if (peer != panel && !positionDoesNotOverlap(peer)) {
                peer.hide();
            }
        }
        // Log.get().debug("x: " + panel.label.getHTML().substring(0,10));
    }
    
    private interface Positioner {
        public void position(final CellMessagePanel panel);
    }
    
    private static final Positioner RightDownPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft() + panel.getTd().getOffsetWidth() + 6;
            final int top = panel.getTdTop();
            panel.display(left, top);
        }
    };
    private static final Positioner RightUpPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft() + panel.getTdWidth() + 6;
            final int top = panel.getTdTop() - panel.getOffsetHeight() + panel.getTdHeight() + 3;
            panel.display(left, top);
        }
    };
    private static final Positioner LeftDownPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            // The constant offset is necessary or the panel thinks it overlaps with the td itself.
            // I'd actually like the panel to be a little closer to the td.
            final int left = panel.getTdLeft() - panel.getOffsetWidth() - 1;
            final int top = panel.getTdTop();
            panel.display(left, top);
        }
    };
    private static final Positioner LeftUpPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            // The constant offset is necessary or the panel thinks it overlaps with the td itself.
            // I'd actually like the panel to be a little closer to the td.
            final int left = panel.getTdLeft() - panel.getOffsetWidth() - 1;
            final int top = panel.getTdTop() + panel.getTdHeight() - panel.getOffsetHeight() + 3;
            panel.display(left, top);
        }
    };
    private static final Positioner BelowRightPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft() + 3;
            final int top = panel.getTdTop() + panel.getTdHeight() + 7;
            panel.display(left, top);
        }
    };
    private static final Positioner AboveRightPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft();
            final int top = panel.getTdTop() - panel.getOffsetHeight() - 3;
            panel.display(left, top);
        }
    };
    private static final Positioner BelowLeftPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft() - panel.getOffsetWidth() - 3;
            final int top = panel.getTdTop() + panel.getTdHeight() + 7;
            panel.display(left, top);
        }
    };
    private static final Positioner AboveLeftPositioner = new Positioner() {
        public void position(final CellMessagePanel panel) {
            final int left = panel.getTdLeft() - panel.getOffsetWidth() - 3;
            final int top = panel.getTdTop() - panel.getOffsetHeight() - 3;
            panel.display(left, top);
        }
    };
    // Can add more of these if necessary.
    private static final Positioner[] POSITIONS = {
        RightDownPositioner, RightUpPositioner, LeftDownPositioner, 
        LeftUpPositioner, BelowRightPositioner, BelowLeftPositioner, 
        AboveLeftPositioner, AboveRightPositioner
    };
}
