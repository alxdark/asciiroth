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
package us.asciiroth.editor.client.ui;

import us.asciiroth.client.board.Board;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.ui.CenteredPopupPanel;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.Log;
import us.asciiroth.client.ui.MousyButton;
import us.asciiroth.editor.client.Editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

public class MetadataDialog extends CenteredPopupPanel {
    
    private RadioButton outside;
    private RadioButton notOutside;
    
    private TextBox startX;
    private TextBox startY;
    private TextBox startInventory;
    private TextBox music;
    private TextBox north;
    private TextBox south;
    private TextBox east;
    private TextBox west;
    private TextBox up;
    private TextBox down;
    
    private TextBox name;
    private TextBox creator;
    private TextBox description;
    
    public MetadataDialog() {
        super();
        setWidth("400px");
        
        Fieldset fs = new Fieldset("Board");
        outside = new RadioButton("outside", "Yes");
        notOutside = new RadioButton("outside", "No");
        FlowPanel panel = new FlowPanel();
        panel.add(outside);
        panel.add(notOutside);
        fs.add("Is the map outside?", panel);
        north = new TextBox();
        fs.add("North map", north);
        south = new TextBox();
        fs.add("South map", south);
        east = new TextBox();
        fs.add("East map", east);
        west = new TextBox();
        fs.add("West map", west);
        up = new TextBox();
        fs.add("Up map", up);
        down = new TextBox();
        fs.add("Down map", down);        
        music = new TextBox();
        fs.add("Music", music);
        addComponent(fs);
        
        fs = new Fieldset("Start board (start.js) and/or editor preview");
        startX = new TextBox();
        fs.add("Player starts at X", startX);
        startY = new TextBox();
        fs.add("Player starts at Y", startY);
        startInventory = new TextBox();
        fs.add("Player starts with items", startInventory);
        addComponent(fs);

        fs = new Fieldset("Start board scenario info (start.js only)");
        name = new TextBox();
        fs.add("Scenario name", name);
        creator = new TextBox();
        fs.add("Creator", creator);
        description = new TextBox();
        fs.add("Description", description);
        addComponent(fs);
        
        addButton(new MousyButton("5em", "OK", new ClickHandler() {
            public void onClick(ClickEvent event) {
                Editor.get().setDirty(true);
                Board board = Editor.get().getBoard();
                board.setOutside( outside.getValue().booleanValue() );
                board.setStartXY(parseInt(startX.getText()), parseInt(startY.getText()));
                board.setAdjacentBoard(Direction.NORTH.getName(), stripJsExt(north));
                board.setAdjacentBoard(Direction.SOUTH.getName(), stripJsExt(south));
                board.setAdjacentBoard(Direction.EAST.getName(), stripJsExt(east));
                board.setAdjacentBoard(Direction.WEST.getName(), stripJsExt(west));
                board.setAdjacentBoard(Direction.UP.getName(), stripJsExt(up));
                board.setAdjacentBoard(Direction.DOWN.getName(), stripJsExt(down));
                board.setStartInventory(startInventory.getText());
                board.setMusic(music.getText());
                board.setScenarioName(name.getText());
                board.setCreator(creator.getText());
                board.setDescription(description.getText());
                DialogManager.get().pop();
            }
        }));
        addButton(new MousyButton("5em", "Cancel", DialogManager.CLOSE_DIALOG_HANDLER));
    }
    private String stripJsExt(TextBox field) {
        String value = field.getText();
        if (value.endsWith(".js")) {
            value = value.substring(0, value.length()-3);
            field.setText(value);
        }
        return value;
    }
    private int parseInt(String value) {
        Log.get().debug("Value to parse: " + value);
        try {
            return Integer.parseInt(value);
        } catch(Throwable throwable) {
            return -1;
        }
    }
    @Override
    public void center() {
        Board board = Editor.get().getBoard();
        outside.setValue( Boolean.valueOf(board.isOutside()) );
        notOutside.setValue( !Boolean.valueOf(board.isOutside()) );
        startX.setText(Integer.toString(board.getStartX()));
        startY.setText(Integer.toString(board.getStartY()));
        north.setText(board.getAdjacentBoard(Direction.NORTH));
        south.setText(board.getAdjacentBoard(Direction.SOUTH));
        east.setText(board.getAdjacentBoard(Direction.EAST));
        west.setText(board.getAdjacentBoard(Direction.WEST));
        up.setText(board.getAdjacentBoard(Direction.UP));
        down.setText(board.getAdjacentBoard(Direction.DOWN));
        startInventory.setText(board.getStartInventory());
        music.setText(board.getMusic());
        name.setText(board.getScenarioName());
        creator.setText(board.getCreator());
        description.setText(board.getDescription());
        super.center();
    }
    @Override
    public void focus() {
    }
}
