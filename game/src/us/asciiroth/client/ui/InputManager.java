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

import static us.asciiroth.client.core.Flags.PARALYZED;
import static us.asciiroth.client.core.Flags.TURNED_TO_STONE;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.agents.Targeting;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.event.FlagsListener;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;

/**
 * The key manager is a focus widget that processes keyboard events 
 * This works, although it would be better simply to capture events 
 * at the document level (this widget constantly has to grab the focus
 * in order to work). But I don't see any way to do that from within
 * GWT.
 *
 */
public class InputManager implements NativePreviewHandler, FlagsListener, MouseCellListener {
    
	// Movement keys
    private static final int KEY_UP =           38;
    private static final int KEY_DOWN =         40;
	private static final int KEY_LEFT =         37;
	private static final int KEY_RIGHT = 	    39;

	public static final int KEY_RETURN = 	    13;
	public static final int KEY_ESC = 		    27;
	// private static final int KEY_COMMA = 	    188;
	private static final int KEY_PERIOD = 	    190;

	private static final int KEY_A =            65;
	private static final int KEY_B =            66;
    private static final int KEY_C =            67;
    private static final int KEY_D =            68;
    private static final int KEY_E =            69;
    // private static final int KEY_F =            70;
    // private static final int KEY_G =            71;
    private static final int KEY_H =            72;
    // private static final int KEY_I =            73;
    private static final int KEY_J =            74;
    private static final int KEY_K =            75;
    private static final int KEY_L =            76;
    // private static final int KEY_M =            77;
    private static final int KEY_N =            78;
    // private static final int KEY_O =            79;
    private static final int KEY_P =            80;
    // private static final int KEY_Q =            81;
    private static final int KEY_R =            82;
    private static final int KEY_S =            83;
    private static final int KEY_T =            84;
    private static final int KEY_U =            85;
    private static final int KEY_V =            86;
    private static final int KEY_W =            87;
    // private static final int KEY_X =            88;
    private static final int KEY_Y =            89;
    private static final int KEY_Z =            90;

    private static final int KEY_0 =            48;
    private static final int KEY_1 =            49;
    private static final int KEY_2 =            50;
    private static final int KEY_3 =            51;
    private static final int KEY_4 =            52;
    private static final int KEY_5 =            53;
    private static final int KEY_6 =            54;
    private static final int KEY_7 =            55;
    private static final int KEY_8 =            56;
    private static final int KEY_9 =            57;
    
    // private static final int KEY_NUM_0 =         96;
    private static final int KEY_NUM_1 =         97;
    private static final int KEY_NUM_2 =         98;
    private static final int KEY_NUM_3 =         99;
    private static final int KEY_NUM_4 =         100;
    public static final int KEY_NUM_5 =         101;
    private static final int KEY_NUM_6 =         102;
    private static final int KEY_NUM_7 =         103;
    private static final int KEY_NUM_8 =         104;
    private static final int KEY_NUM_9 =         105;
    
    private static final int KEY_F1              = 112; // help
    // private static final int KEY_F2              = 113;
    private static final int KEY_F3              = 114; // save
    /// private static final int KEY_F4              = 115;
    private static final int KEY_F5              = 116; // main menu
    // private static final int KEY_F6              = 117;
    private static final int KEY_F7              = 118;
    private static final int KEY_F8              = 119;
    // private static final int KEY_F9              = 120;
    private static final int KEY_F10             = 121; // save & quit
    // private static final int KEY_F11             = 122;
    private static final int KEY_F12             = 123;
    
    // public final HandlerRegistration handlerRegistration;
    
	public InputManager() {
        this.charCode = -1;
        // handlerRegistration = Event.addNativePreviewHandler(this);
        Event.addNativePreviewHandler(this);
	}
	
    private int charCode;
    private boolean isSpecialCommand;
    private boolean isThrowCommand;
    private boolean limitedToGameCommands;
    private boolean processed;
    private boolean reportKey;
    
	public void onFlagsChanged(Agent agent) {
		// The only issue is that when you start a new game, this isn't reset.
		limitedToGameCommands = (agent.is(PARALYZED) || agent.is(TURNED_TO_STONE));
	}
	
	/**
	 * This is a complicated method that attempts to do what would be the most
	 * obvious action indicated by clicking on a cell on the board, in order 
	 * to provide a simple, mouse-based interface for novice users
	 */
    public void onMouseDown(Element td, Cell cell, boolean shifted) {
        // Timer? Could do it periodically till mouseup...
        if (cell == null || DialogManager.get().isPaused()) {
            return;
        }
        Events.get().fireClearCurrentCell();
        clearActionBuffer();
        us.asciiroth.client.core.Event e = Game.get().createEvent();
        Cell current = e.getBoard().getCurrentCell();
        if (current == cell) {
            // Either move to the next board, or do pickup/use as relevant
            if (cell.getAdjacentCell(Direction.NORTH) == null) {
                Game.get().move(Direction.NORTH);
            } else if (cell.getAdjacentCell(Direction.SOUTH) == null) {
                Game.get().move(Direction.SOUTH);
            } else if (cell.getAdjacentCell(Direction.EAST) == null) {
                Game.get().move(Direction.EAST);
            } else if (cell.getAdjacentCell(Direction.WEST) == null) {
                Game.get().move(Direction.WEST);   
            } else {
                Game.get().defaultForCurrentCell();
            }
        } else {
            // Otherwise assume a fire/move action
            Direction dir = AgentUtils.getDirectionToCell(current, cell);
            if (isThrowCommand) {
            	Game.get().throwItem(dir);
            	isThrowCommand = false;
            } else if (shifted) {
                Game.get().fireWeapon(dir);
            } else {
                Cell next = current.getAdjacentCell(dir);
                if (cell == next) { // always do the move, even into boulders, switches, etc.
                    Game.get().move(dir);
                } else {
                    dir = AgentUtils.findPathInDirection(current, e.getPlayer(), cell, dir, new Targeting());
                    Game.get().move(dir);
                }
            }
        }
        Events.get().fireHandleInventoryMessaging();
    }
    public void onMouseOver(Element td, Cell cell, boolean shifted) {}
    public void onMouseUp(Element td, Cell cell, boolean shifted) {}
    public void onMouseOut(Element td, Cell cell, boolean shifted) {}
    public void onContextMenu(Element td, Cell cell, boolean shifted) {}

	public void onPreviewNativeEvent(NativePreviewEvent preview) {
		// I would very strongly have expected the cancellation of the event by a dialog
		// to have prevented this onPreviewNativeEvent() handler from even firing in the
		// first place, but it does. So I manually cancel here... this is almost certainly
		// wrong but until a good description of even previewing comes along, this will work.
		if (preview.isCanceled()) {
			return;
		}
		
		NativeEvent event = preview.getNativeEvent();
        // Capture key on keydown because that's most reliable, and process action on keypress
        // because that's the only event that reliably repeats. However, the way that escape 
        // works, with the event previewing going on, keypress events have to be avoided when
        // that's the key. Just don't touch it 'cause it's all magic...
        
        // On the normal keyboard, shift seemed to work better because control with AIR/Safari
        // led to odd side effects. However when you use the number pad, the numbers change 
        // when you press shift, you don't get the same charCode. So there, the control key is
        // necessary. Opera doesn't recognize the keys on the keypad as being 
        // any different at all, so no numpad support for Opera.
		// WARN: This may be dead wrong, I'm not really sure.
        int type = preview.getTypeInt();
        if (type == Event.ONKEYDOWN) {
            charCode = event.getKeyCode();
            isSpecialCommand = event.getShiftKey() || event.getCtrlKey();
            processed = false;
            if (reportKey) {
                Util.showError("Key #" + charCode);
            }
            // Other unmodified keys don't have natural defaults...
            if (charCode >= KEY_F1 && charCode <= KEY_F12) {
                event.preventDefault(); 
            }
        } /*else if (type == Event.ONKEYPRESS) {
            if (event.getKeyCode() != InputManager.KEY_ESC) {
                processed = false;
            }
        }*/
	}

    public void clearActionBuffer() {
        processed = true;
    }
    
    public void processNextAction() {
        if (!processed) {
            processed = true;
            
            Events.get().fireClearCurrentCell();
            if (isThrowCommand && !limitedToGameCommands) {
                handleThrow(charCode);
            } else if (isSpecialCommand && !limitedToGameCommands) {
                handleSpecialCommand(charCode);
            } else {
                handleCommand(charCode);
            }
            if (charCode != KEY_T) {
                isThrowCommand = false;
            }
            Events.get().fireHandleInventoryMessaging();
        }
    }
    
    private void handleThrow(int charCode) {
        switch(charCode) {
        case KEY_K:
        case KEY_UP:
        case KEY_NUM_8:
            Game.get().throwItem(Direction.NORTH);
            break;
        case KEY_J:
        case KEY_DOWN:
        case KEY_NUM_2:
            Game.get().throwItem(Direction.SOUTH);
            break;
        case KEY_L:
        case KEY_RIGHT:
        case KEY_NUM_6:
            Game.get().throwItem(Direction.EAST);
            break;
        case KEY_H:
        case KEY_LEFT:
        case KEY_NUM_4:
            Game.get().throwItem(Direction.WEST);
            break;
        case KEY_U:
        case KEY_NUM_9:
            Game.get().throwItem(Direction.NORTHEAST);
            break;
        case KEY_B:
        case KEY_NUM_1:
            Game.get().throwItem(Direction.SOUTHWEST);
            break;
        case KEY_Y:
        case KEY_NUM_7:
            Game.get().throwItem(Direction.NORTHWEST);
            break;
        case KEY_N:
        case KEY_NUM_3:
            Game.get().throwItem(Direction.SOUTHEAST);
            break;
        }
    }
    
    private void handleSpecialCommand(int charCode) {
        switch(charCode) {
        case KEY_K:
        case KEY_UP:
        case KEY_NUM_8:
            Game.get().fireWeapon(Direction.NORTH);
            break;
        case KEY_J:
        case KEY_DOWN:
        case KEY_NUM_2:
            Game.get().fireWeapon(Direction.SOUTH);
            break;
        case KEY_L:
        case KEY_RIGHT:
        case KEY_NUM_6:
            Game.get().fireWeapon(Direction.EAST);
            break;
        case KEY_H:
        case KEY_LEFT:
        case KEY_NUM_4:
            Game.get().fireWeapon(Direction.WEST);
            break;
        case KEY_U:
        case KEY_NUM_9:
            Game.get().fireWeapon(Direction.NORTHEAST);
            break;
        case KEY_B:
        case KEY_NUM_1:
            Game.get().fireWeapon(Direction.SOUTHWEST);
            break;
        case KEY_Y:
        case KEY_NUM_7:
            Game.get().fireWeapon(Direction.NORTHWEST);
            break;
        case KEY_N:
        case KEY_NUM_3:
            Game.get().fireWeapon(Direction.SOUTHEAST);
            break;
        }
    }
	private void handleCommand(int charCode) {
        switch(charCode) {
        case KEY_F1:
            DialogManager.get().pushSystemDialog("help.html");
            return;
        case KEY_F3:
            Game.get().selectSaveGameName();
            return;
        case KEY_F5:
            Game.get().mainMenu();
            return;
        case KEY_F7:
            DialogManager.get().push(FontSpec.class);
            return;
        case KEY_F8:
            reportKey = !reportKey;
            Util.showError("Key reporting is " + ((reportKey) ? "on" : "off"));
            return;
        case KEY_F10:
            Game.get().saveGameAndQuit();
            return;
        case KEY_ESC:
            Game.get().mainMenu();
            return;
        }
        if (limitedToGameCommands) {
        	return;
        }
        switch(charCode) {
        case KEY_A:
            Game.get().selectUp();
            break;
        case KEY_S:
            Game.get().selectDown();
            break;
        case KEY_D:
            Game.get().dropSelectedItem();
            break;
        case KEY_C:
            Game.get().moveSelectedUp();
            break;
        case KEY_V:
            Game.get().moveSelectedDown();
            break;
        case KEY_W:
            Game.get().selectFirstWeapon();
            break;
        case KEY_P:
        case KEY_PERIOD:
            Game.get().pickupItem(0);
            break;
        case KEY_E:
            Game.get().selectEmptyHanded();
            break;
        case KEY_R:
            Game.get().rotateItems();
            break;
        case KEY_T:
            isThrowCommand = true;
            Game.get().setToThrowItem();
            break;
        case KEY_RETURN:
            Game.get().useSelectedItem();
            break;
        case KEY_NUM_5:
            Game.get().defaultForCurrentCell();
            break;
        case KEY_1:
        case KEY_2:
        case KEY_3:
        case KEY_4:
        case KEY_5:
        case KEY_6:
        case KEY_7:
        case KEY_8:
        case KEY_9:
            int index = charCode-49; // 1 == 49 == index 0
            Game.get().pickupItem(index);
            break;
        case KEY_0:
            Game.get().pickupItem(9);
            break;
        case KEY_K:
		case KEY_UP:
        case KEY_NUM_8:
            Game.get().move(Direction.NORTH);
            break;
        case KEY_J:
		case KEY_DOWN:
        case KEY_NUM_2:
            Game.get().move(Direction.SOUTH);
            break;
		case KEY_L:
		case KEY_RIGHT:
		case KEY_NUM_6:
            Game.get().move(Direction.EAST);
            break;
        case KEY_H:
		case KEY_LEFT:
        case KEY_NUM_4:
            Game.get().move(Direction.WEST);
            break;
        case KEY_U:
        case KEY_NUM_9:
            Game.get().move(Direction.NORTHEAST);
            break;
        case KEY_B:
        case KEY_NUM_1:
            Game.get().move(Direction.SOUTHWEST);
            break;
        case KEY_Y:
        case KEY_NUM_7:
            Game.get().move(Direction.NORTHWEST);
            break;
        case KEY_N:
        case KEY_NUM_3:
            Game.get().move(Direction.SOUTHEAST);
            break;
        case KEY_Z:
            Game.get().moveVertical();
            break;
		}
	}
}
