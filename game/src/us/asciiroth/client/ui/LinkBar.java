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

import us.asciiroth.client.Profile;
import us.asciiroth.client.Util;
import us.asciiroth.client.core.Game;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class LinkBar {

    // It's not worth it to localize these...
    private static String GEARS_LINK = "http://gears.google.com/?action=install&message={0}&return={1}";
    private static String GEARS_MESSAGE = "Install Gears and then restart Asciiroth; you'll be able to save your game.";
    private static String RETURN_URL = "http://icculus.org/asciiroth/";
    
    public LinkBar() {
        Hyperlink saveGame = new Hyperlink("Save", "save");
        saveGame.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Game.get().selectSaveGameName();
			}
        });
        Hyperlink installGears = new Hyperlink("Install Google Gears to save game", "gears");
        installGears.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
                // Pauses the game included the focus behavior.
                Game.get().mainMenu();
                Window.open(
                    Util.format(GEARS_LINK, GEARS_MESSAGE, RETURN_URL),
                    "installGears", 
                    "");
        	}
        });
        Hyperlink fonts = new Hyperlink("Show extended fonts", "fonts");
        fonts.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Hyperlink h = (Hyperlink)event.getSource();
        		// WARN: Translated to getSource(), might not be correct.
                // Hyperlink h = (Hyperlink)sender;
                Profile p = Game.get().getProfile();
                p.setExtendedFonts( !p.useExtendedFonts() );
                if (p.useExtendedFonts()) {
                    h.setText("Hide extended fonts");
                } else {
                    h.setText("Show extended fonts");
                }
                Game.get().changeFonts();
        	}
        });
        
        Hyperlink mainMenu = new Hyperlink("Game Menu", "menu");
        mainMenu.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Game.get().mainMenu();
        	}
        });
        Hyperlink help = new Hyperlink("Help", "help");
        help.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		DialogManager.get().pushSystemDialog("help.html");
        	}
        });
        Hyperlink about = new Hyperlink("About", "about");
        about.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		DialogManager.get().pushSystemDialog("about.html");
        	}
        });
        Hyperlink quit = new Hyperlink("Quit", "quit");
        quit.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Game.get().confirmQuit();
        	}
        });
        
        // Create link bar
        RootPanel linkBar = RootPanel.get("linkBar");
        if (!Game.get().isTestGame()) {
            if (Game.get().getProfile().isPersistenceProvided()) {
                linkBar.add(saveGame);
            } else {
                linkBar.add(installGears);
            }
            linkBar.add(new Label(" | "));
        }
        linkBar.add(fonts);
        linkBar.add(new Label(" | "));
        linkBar.add(mainMenu);
        linkBar.add(new Label(" | "));
        linkBar.add(help);
        linkBar.add(new Label(" | "));
        linkBar.add(about);
        if (Game.get().getProfile().isAirProvided()) {
            linkBar.add(new Label(" | "));
            linkBar.add(quit);
        }
        linkBar.add(new HTML("&nbsp;| &copy; 2009-2014 Alx Dark ")); // nbsp fixes IE bug
    }
}
