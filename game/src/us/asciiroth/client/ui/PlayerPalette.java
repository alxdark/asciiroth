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

import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Flags;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.event.FlagsListener;
import us.asciiroth.client.event.PlayerListener;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class PlayerPalette extends AttributesPalette implements PlayerListener, FlagsListener {

    private HTML flagsPanel;
    private Image healthImg;
    
    public PlayerPalette() {
        super();
        FlowPanel healthPanel = new FlowPanel();
        healthPanel.setStyleName("healthBar");
        healthImg = new Image("health.gif");
        healthImg.setHeight("11px");
        healthPanel.add(healthImg);
        
        flagsPanel = new HTML();
        flagsPanel.setStyleName("flagsContent");
        
        setAttribute("Health", healthPanel);
        setAttribute("Flags", flagsPanel);
        
        RootPanel.get("sideViews").add(this);
    }
    public void onPlayerChanged(Player player) {
        updateName(player.getName());
        updateHealth(player.changeHealth(0));
        updateFlags(player.getFlags());
    }
    public void onFlagsChanged(Agent agent) {
        updateFlags(((Player)agent).getFlags());
    }
    public void updateName(String name) {
        setTitle(name);
    }
    public void updateFlags(int flags) {
        flagsPanel.setHTML(Flags.description(flags));
    }
    public void updateHealth(int value) {
        int width = computeWidth(120, 255, value);
        healthImg.setWidth(width+"px");
    }
    private int computeWidth(double barSize, double max, double value) {
        double width = (value / max) * barSize;
        if (width > barSize) width = barSize;
        return (int)width;
    }
}
