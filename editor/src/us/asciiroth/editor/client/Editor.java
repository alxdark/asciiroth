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
package us.asciiroth.editor.client;

import us.asciiroth.client.Profile;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.BoardLoader;
import us.asciiroth.client.board.BoardReader;
import us.asciiroth.client.board.BoardWriter;
import us.asciiroth.client.core.HasBoard;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.editor.client.store.EditorStore;
import us.asciiroth.editor.client.store.FilePathCallback;
import us.asciiroth.editor.client.store.LoadCallback;
import us.asciiroth.editor.client.ui.ApplicationBar;
import us.asciiroth.editor.client.ui.BrushPalette;
import us.asciiroth.editor.client.ui.InstancePalette;
import us.asciiroth.editor.client.ui.MetadataDialog;
import us.asciiroth.editor.client.ui.SaveFirstDialog;
import us.asciiroth.editor.client.ui.TypePalette;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;


public class Editor implements HasBoard {

    private static Editor instance = new Editor();
    public static Editor get() {
        return instance;
    }
    private Editor() {
    }
    public void init() {
        profile = new Profile();
        store = GWT.create(EditorStore.class);
        
        brushPalette = new BrushPalette();
        typePalette = new TypePalette();
        instancePalette = new InstancePalette();
        
        // TOOD: Need to be able to attach mouse listeners to the grid or the BoardView 
        boardEditor = new BoardEditor(this);
        boardReader = new BoardReader();
        boardLoader = new BoardLoader(boardReader);
        boardWriter = new BoardWriter();
        Events.get().addCellListener(boardEditor);
        
        new ApplicationBar();
        
        dirtyIcon = DOM.getElementById("dirtyIcon");
    }
    
    private Profile profile;
    private EditorStore store;
    private boolean isDirty;
    private Element dirtyIcon;
    
    private MetadataDialog metadataDialog;
    private BrushPalette brushPalette;
    private TypePalette typePalette;
    private InstancePalette instancePalette;
    private SaveFirstDialog saveBeforeNewBoard;
    private SaveFirstDialog saveBeforeLoadBoard;
    private NameGenerator namegen;
    
    private String boardFilePath;
    private Board board;
    
    private BoardEditor boardEditor;
    private BoardReader boardReader;
    private BoardLoader boardLoader;
    private BoardWriter boardWriter;
    
    private Player player;
    
    private class Loader implements LoadCallback {
        public void execute(String path, String json) {
            setBoardFilePath(path);
            player = new Player(null, "Test Player", "", path, -2, -2);
            boardReader.read(boardEditor, player, board, json, RESET_PALETTE);
        }
    }
    
    public Profile getProfile() {
        return profile;
    }
    public BrushPalette getBrushPalette() {
        return brushPalette;
    }
    public TypePalette getTypePalette() {
        return typePalette;
    }
    public InstancePalette getInstancePalette() {
        return instancePalette;
    }
    public Board getBoard() {
        return board;
    }
    public BoardEditor getBoardEditor() {
        return boardEditor;
    }
    public boolean isDirty() {
        return isDirty;
    }
    public void setDirty(boolean dirty) {
    	dirtyIcon.getStyle().setProperty("visibility", dirty ? "visible" : "hidden");
        isDirty = dirty;
    }
    public String getBoardFilePath() {
        return boardFilePath;
    }
    public void setBoardFilePath(String url) {
        this.boardFilePath = url;

        Element filePath = DOM.getElementById("filePath");
        filePath.setInnerHTML((url == null) ? "<em>&lt;Not Saved&gt;</em>" : url);
    }
    
    private final Command SHOW_LAYOUT_RESET_PALETTE = new Command() {
        public void execute() {
            Element layout = DOM.getElementById("layout");
            layout.getStyle().setProperty("visibility", "visible");
            instancePalette.resetToBoard(board);
            setDirty(false);
        }
    };
    
    private final Command RESET_PALETTE = new Command() {
        public void execute() {
            instancePalette.resetToBoard(board);
            setDirty(false);
        }
    };
    
    private final Command LOAD_TEST_BOARD = new Command() {
        public void execute() {
            if (board.getStartX() < 0 || board.getStartY() < 0) {
                Util.showError("Player's starting X/Y not specified for board.<br/>Shift-click on a cell to select position with cell editor.");
            } else {
                // Yeah, copy it in there.
                String s = "us.asciiroth.Launch/Game.html?"+URL.encode(boardFilePath);
                instancePalette.resetToBoard(board);
                setDirty(false);
                Window.open(s, "test", "width=820,height=545");
            }
        }
    };
    
    public void testBoard() {
        if (boardFilePath == null) {
            Util.showError("You must save the board first.");
        } else {
            if (isDirty()) {
                String json = boardWriter.write(board, true);
                store.saveBoard(boardFilePath, json, LOAD_TEST_BOARD);
            } else {
                LOAD_TEST_BOARD.execute();
            }
        }
    }
    
    public void newBoard() {
        if (isDirty()) {
            if (saveBeforeNewBoard == null) {
                saveBeforeNewBoard = new SaveFirstDialog("Save before starting on a new map?", new Command() {
                    public void execute() {
                        doNewBoard();
                    }
                });
            }
            DialogManager.get().push(saveBeforeNewBoard);
        } else {
            doNewBoard();
        }
    }
    
    public void loadBoard(final String path) {
        if (isDirty()) {
            if (saveBeforeLoadBoard == null) {
                saveBeforeLoadBoard = new SaveFirstDialog("Save before loading the next map?", new Command() {
                    public void execute() {
                        doLoadBoard(path);
                    }
                });
            }
            DialogManager.get().push(saveBeforeLoadBoard);
        } else {
            doLoadBoard(path);
        }
    }
    
    public void saveBoardAs() {
        String json = boardWriter.write(board, false);
        store.saveBoardAs(json, new FilePathCallback() {
            public void execute(String filePath) {
                if (!filePath.endsWith(".js")) {
                    filePath += ".js";
                }
                setBoardFilePath(filePath);
                setDirty(false);
            }
        });
    }
    
    private void doNewBoard() {
        setBoardFilePath(null);
        // Not great, but the player is how url, x, and y are
        // passed to the board writer/loader.
        player = new Player(null, "Test Player", "", "blank", -2, -2);
        board = new Board();
        boardLoader.load(boardEditor, player, board, SHOW_LAYOUT_RESET_PALETTE);
    }
    
    private void doLoadBoard(String path) {
        if (path != null) {
            if (store.fileExists(path)) {
                board = new Board();
                store.loadBoard(path, new Loader());    
            } else {
                Util.showError("The board links to a map but the file doesn't exist:\n"+path);
            }
        } else {
            store.selectFile(new FilePathCallback() {
               public void execute(String filePath) {
                   board = new Board();
                   store.loadBoard(filePath, new Loader());
               };
            });
        }
    }
    
    public void saveBoard(Command command) {
        if (boardFilePath == null) {
            saveBoardAs();
        } else if (!boardFilePath.endsWith(".js")) {
            Util.showError("Board file name must end with '.js'");
        } else {
            // New and load use a more complicated command.
            if (command == null) {
                command = RESET_PALETTE;
            }
            String json = boardWriter.write(board, true);
            store.saveBoard(boardFilePath, json, command);
        }
    }
    
    public void showMetadataDialog() {
        if (metadataDialog == null) {
            metadataDialog = new MetadataDialog();
        }
        DialogManager.get().push(metadataDialog);
    }
    
    public void makeUpName() {
        if (namegen == null) {
            namegen = new NameGenerator();
        }
        Element el = DOM.getElementById("makeName");
        el.setInnerHTML(namegen.getName());
    }
}
