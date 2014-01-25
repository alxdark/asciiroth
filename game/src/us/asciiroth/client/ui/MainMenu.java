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

import us.asciiroth.client.core.Game;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;

/**
 * This menu is suprisingly hairy. It has to represent the following states:
 * <p>
 * 1. A browser app but without persistence (!isDesktopApp && !hasPersistence)
 * 2. A browser app with persistence (!isDesktopApp && hasPersistence)
 * 3. A desktop app (isDesktopApp)
 * 4. A test game started from the editor   (isTestGame)
 * 
 *
 */
public class MainMenu extends CenteredPopupPanel {

    private MousyButton playGame;
    private MousyButton loadGame;
    private MousyButton saveGame;
    private MousyButton newGame;
    
    private boolean isTestGame;
    private boolean isDesktopApp;
    private boolean hasPersistence;
    
    public MainMenu() {
        super();
        setAutoHideEnabled(false);
        addStyleName("n-mainMenu");
        setAnimationEnabled(true);
        
        isTestGame = Game.get().isTestGame();
        isDesktopApp = Game.get().getProfile().isAirProvided();
        hasPersistence = Game.get().getProfile().isPersistenceProvided();
        
        playGame = addMainButton("Play", DialogManager.CLOSE_DIALOG_HANDLER);
        
        if (hasPersistence && !isTestGame) {
            saveGame = addMainButton("Save Game", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Game.get().selectSaveGameName();
                }
            });
            loadGame = addMainButton("Load Game", new ClickHandler() {
                public void onClick(ClickEvent eventi) {
                    DialogManager.get().push(LoadGameDialog.class);
                }
            });
        }
        if (!isTestGame) {
            newGame = addMainButton("New Game", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    DialogManager.get().push(NewGameDialog.class);
                }
            });
        }
        addMainButton("About", new ClickHandler() {
            public void onClick(ClickEvent event) {
                DialogManager.get().pushSystemDialog("about.html");
            }
        });
        if (isDesktopApp) {
            addMainButton("Quit", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Game.get().confirmQuit();
                }
            });
        }
    }
    
    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
    	boolean inProgress = Game.get().isGameInProgress();
    	if (!inProgress && isDismissingDialog(preview)) {
    		preview.cancel();
    	} else {
    		super.onPreviewNativeEvent(preview);
    	}
    }
    
    private boolean isDismissingDialog(NativePreviewEvent preview) {
    	return (preview.getTypeInt() == Event.ONKEYDOWN && 
    			preview.getNativeEvent().getKeyCode() == InputManager.KEY_ESC);
    }
    
    @Override
    public void center() {
        boolean inProgress = Game.get().isGameInProgress();
        if (playGame != null) {
            playGame.setVisible(inProgress);
        }
        if (saveGame != null) {
            saveGame.setVisible(inProgress);    
        }
        super.center();
    }
    @Override
    public void focus() {
        boolean inProgress = Game.get().isGameInProgress();
        if (hasPersistence && !isTestGame) {
            if (loadGame == null) {
                throw new RuntimeException("loadGame is null");
            }
            loadGame.setFocus(true);
        } else if (inProgress || isTestGame) {
            if (playGame == null) {
                throw new RuntimeException("playGame is null");
            }
            playGame.setFocus(true);
        } else {
            if (newGame == null) {
                throw new RuntimeException("newGame is null");
            }
            newGame.setFocus(true);
        }
    }
}
