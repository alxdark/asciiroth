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

import java.util.Stack;

import us.asciiroth.client.Util;
import us.asciiroth.client.event.Events;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;

public class DialogManager implements CloseHandler<PopupPanel> {

    public static final ClickHandler CLOSE_DIALOG_HANDLER = new ClickHandler() {
        public void onClick(ClickEvent event) {
            DialogManager.get().pop();
        }
    };

    private static DialogManager instance = new DialogManager();
    public static DialogManager get() {
        return instance;
    }
    
    private DialogManager() {
        stack = new Stack<PopupPanel>();
        layout = DOM.getElementById("layout"); 
    }
    
    private Element layout;    
    private Stack<PopupPanel> stack;
    private LoadingDialog loading;
    private MainMenu mainMenu;
    private LoadGameDialog loadGameDialog;
    private NewGameDialog newGameDialog;
    private GameListingDialog saveGameDialog;
    private ConfirmQuitDialog confirmQuitDialog;
    private HTMLDialog messageDialog;
    
    public void push(Class<?> c) {
        if (LoadingDialog.class == c) {
            if (loading == null) {
                loading = new LoadingDialog();
            }
            push(loading);
        } else if (MainMenu.class == c) {
            if (mainMenu == null) {
                mainMenu = new MainMenu();
            }
            push(mainMenu);
        } else if (LoadGameDialog.class == c) {
            if (loadGameDialog == null) {
                loadGameDialog = new LoadGameDialog();
            }
            push(loadGameDialog);
        } else if (NewGameDialog.class == c) {
            if (newGameDialog == null) {
                newGameDialog = new NewGameDialog();
            }
            push(newGameDialog);
        } else if (SaveGameDialog.class == c) {
            if (saveGameDialog == null) {
                saveGameDialog = new SaveGameDialog();
            }
            push(saveGameDialog);
        } else if (ConfirmQuitDialog.class == c) {
            if (confirmQuitDialog == null) {
                confirmQuitDialog = new ConfirmQuitDialog();
            }
            push(confirmQuitDialog);
        } else if (FontSpec.class == c) {
            push(new FontSpec());
        }
    }
    public void push(PopupPanel dialog) {
        stack.push(dialog);
        dialog.center();
        if (dialog instanceof CenteredPopupPanel) {
            ((CenteredPopupPanel)dialog).focus();
        }
        if (stack.size() == 1) {
            Util.setOpacity(layout, 0.6);
            if (!(dialog instanceof LoadingDialog)) {
                Events.get().fireGamePaused();    
            }
        }
    }
    /**
     * Display an HTML file in the modal dialog. Does not adjust for the
     * URL of the scenario; used for help, about and similar dialogs.
     * 
     * @param url
     */
    public void pushSystemDialog(String url) {
        push("25em", url);
    }
    /**
     * Used to display HTML in a modal dialog that comes from a specific
     * scenario, and so is adjusted to the scenario base URL. 
     *
     * @param scenarioURL
     * @param url
     */
    public void pushScenarioDialog(String scenarioURL, String url) {
        push(HTMLDialog.DEFAULT_WIDTH, scenarioURL+url);
    }

    private void push(String width, String url) {
        if (messageDialog == null) {
            messageDialog = new HTMLDialog();
        }
        messageDialog.setWidth(width);
        messageDialog.setHTML(url);
        push(messageDialog);
    }
    
    public void pop() {    	
        if (!stack.isEmpty()) {
            PopupPanel dialog = stack.pop();
            dialog.hide();
            if (dialog == mainMenu) {
            	mainMenu = null;
            }
        }
        if (stack.isEmpty()) {
            Util.setOpacity(layout, 1.0);
            Events.get().fireGameResumed();
        }
    }
    
    public void popAll() {
        while (!stack.isEmpty()) {
            pop();
        }
    }
    public boolean isPaused() {
        return !stack.isEmpty();
    }

	public void onClose(CloseEvent<PopupPanel> event) {
        if (!stack.isEmpty()) {
            PopupPanel panel = stack.peek();
            if (panel instanceof CenteredPopupPanel) {
                ((CenteredPopupPanel)panel).focus();
            }
        }
	}
    
}
