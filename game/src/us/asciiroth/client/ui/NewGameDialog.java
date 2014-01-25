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

import java.util.List;

import us.asciiroth.client.core.Game;
import us.asciiroth.client.json.JsArray;
import us.asciiroth.client.json.ScenarioJSON;
import us.asciiroth.client.store.NamesCallback;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class NewGameDialog extends CenteredPopupPanel implements ChangeHandler, ClickHandler {
    
    private final TextBox name;
    private final Label error;
    private final ListBox list;
    private final FlowPanel details;
    JsArray<ScenarioJSON> scenarios;
    
    public NewGameDialog() {
        super();
        scenarios = ScenarioJSON.getScenarios();
        
        addLabel("What is your name, adventurer?");
        name = new TextBox();
        name.setWidth("14em");
        name.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				validate(false);
			}
        });
        addComponent(name);
        
        error = new Label("Please enter a name.");
        error.setStyleName("n-error");
        error.setVisible(false);
        addComponent(error);
        
        addLabel("Select a scenario");
        
        list = new ListBox(false);
        list.setVisibleItemCount(5);
        list.setWidth("14em");
        list.setHeight("12em");
        list.addStyleName("n-newGameList");
        for (int i=0; i < scenarios.size(); i++) {
            ScenarioJSON scenario = scenarios.get(i);
            list.addItem(scenario.name(), scenario.url());
        }
        list.addChangeHandler(this);
        // There has to be at least one scenario...
        list.setItemSelected(0, true);
        
        addComponent(list);
        
        details = new FlowPanel();
        details.addStyleName("n-details");
        addComponent(details);

        changeInternal(list);
        
        addButton("Play", this);
        if (Game.get().getProfile().isAirProvided()) {
            addButton("Load", new ClickHandler() {
                public void onClick(ClickEvent event) {
                    promptForScenario();
                }
            });
        }
        addCancelButton();
    }
    private void promptForScenario() {
        Game.get().promptForScenario(this);
    }
    public void showLoadedScenario(ScenarioJSON scenario) {
        list.clear();
        list.addItem(scenario.name(), scenario.url());
        list.setSelectedIndex(0);
        updateDetailsFromScenario(scenario);
    }
    public void onChange(ChangeEvent event) {
    	// WARN: Here again, use of getSource() may be incorrect
    	changeInternal( (ListBox)event.getSource() );
    }
    private void changeInternal(ListBox list) {
        ScenarioJSON scenario = scenarios.get( list.getSelectedIndex() );
        updateDetailsFromScenario(scenario);
    }
    private void updateDetailsFromScenario(ScenarioJSON scenario) {
        details.clear();
        HTML div = new HTML(scenario.name());
        div.addStyleName("n-name");
        details.add(div);

        div = new HTML("by " + scenario.creator());
        div.addStyleName("n-creator");
        details.add(div);
        
        div = new HTML(scenario.description());
        div.addStyleName("n-description");
        details.add(div);
    }
    private void validate(final boolean thenStartGame) {
    	Game.get().getSavedGames(new NamesCallback() {
			public void execute(List<String> games) {
		        if (name.getText() == null || name.getText().length() == 0) {
		            error.setText("Please enter a name.");
		            error.setVisible(true);
		        } else if (games.contains(name.getText())) {
		            error.setText("That name is aleady taken.");
		            error.setVisible(true);
		        } else {
			        error.setVisible(false);
			        if (thenStartGame) {
			            DialogManager.get().pop();
			            Game.get().newGame(name.getText(), list.getValue(list.getSelectedIndex()));
			        }
		        }
			}
    	});
    }
    public void onClick(ClickEvent event) {
    	validate(true);
    }
    @Override
    public void center() {
        name.setText("");
        list.clear();
        for (int i=0; i < scenarios.size(); i++) {
            ScenarioJSON scenario = scenarios.get(i);
            list.addItem(scenario.name(), scenario.url());
        }
        list.setSelectedIndex(0);
        error.setVisible(false);
        super.center();
    }
    @Override
    public void focus() {
        name.setFocus(true);
    }
}
