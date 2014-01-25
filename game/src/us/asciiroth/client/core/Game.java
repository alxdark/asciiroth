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
package us.asciiroth.client.core;

import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.VERTICAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.asciiroth.client.Profile;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.agents.Targeting;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.BoardLoader;
import us.asciiroth.client.board.BoardReader;
import us.asciiroth.client.board.BoardWriter;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.effects.Hit;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Grenade;
import us.asciiroth.client.store.NamesCallback;
import us.asciiroth.client.store.Store;
import us.asciiroth.client.store.StoreLoadCallback;
import us.asciiroth.client.terrain.CaveEntrance;
import us.asciiroth.client.terrain.StairsDown;
import us.asciiroth.client.terrain.StairsUp;
import us.asciiroth.client.ui.BoardView;
import us.asciiroth.client.ui.CellInfoPanel;
import us.asciiroth.client.ui.ConfirmQuitDialog;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.GameOverDialog;
import us.asciiroth.client.ui.InputManager;
import us.asciiroth.client.ui.InventoryPalette;
import us.asciiroth.client.ui.LinkBar;
import us.asciiroth.client.ui.LoadingDialog;
import us.asciiroth.client.ui.Log;
import us.asciiroth.client.ui.MainMenu;
import us.asciiroth.client.ui.MessageManager;
import us.asciiroth.client.ui.NewGameDialog;
import us.asciiroth.client.ui.PlayerPalette;
import us.asciiroth.client.ui.SaveGameDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Central controller for the game. 
 *
 */
public class Game implements HasBoard {
    
    private static Game instance = new Game();
    public static Game get() {
        return instance;
    }
    
    // Model
    private Board board;
    private Player player;
    
    // Controller
    private InputManager inputManager;
    private AnimationManager animator;

    // Views
    private boolean gameInProgress;
    private BoardView boardView;
    
    // IO
    // Yeow. That's a lot of readers/writers. Encapsulate? Combine?
    private Profile profile;
    private BoardReader boardReader;
    private BoardWriter boardWriter;
    private BoardLoader boardLoader;
    private PlayerReader playerReader;
    private PlayerWriter playerWriter;
    private Store store;
    
    private Timer loadingTimer;

    private static final Command LAND_PLAYER = new Command() {
        public void execute() { Game.get().land(); }
    };
    
    private static final Command LAND_WITHOUT_EVENTS = new Command() {
        public void execute() { Game.get().landWithoutEvents(); }
    };

    private static final Command QUIT = new Command() {
        public void execute() { Game.get().quitApplication(); }
    };
    
    /**
     * Because this is a singleton and functions a lot like a function library,
     * and the UI is fired directly from the model components, there's a lot
     * of danger of the editor eventually calling Game.get() (to show an error
     * for example). That's safe, but not if it leads to the side-effect of 
     * initializing other components. So initialization is done in an init() 
     * method that is only called from the Launch class of the game module, 
     * not the editor module. It's the downside of the otherwise easy approach
     * of having one massive controller for the whole game.
     *
     */
    private Game() {
        profile = new Profile();
    }
    
    public void init() {
        store = GWT.create(Store.class);
        Log.get().debug(store.getClass().getName());
        
        new LinkBar();

        Events events = Events.get();
        animator = new AnimationManager();
        events.addGameListener(animator);
        
        PlayerPalette playerPalette = new PlayerPalette();
        events.addPlayerListener(playerPalette);
        events.addFlagsListener(playerPalette);
        events.addInventoryListener(new InventoryPalette());
        
        inputManager = new InputManager();
        events.addFlagsListener(inputManager);
        
        boardView = new BoardView(this);
        boardView.addMouseCellListener(new CellInfoPanel());
        boardView.addMouseCellListener(inputManager);
        events.addCellListener(boardView);
        events.addMessageListener(new MessageManager(this));
        
        boardReader = new BoardReader();
        boardWriter = new BoardWriter();
        boardLoader = new BoardLoader(boardReader);
        playerReader = new PlayerReader();
        playerWriter = new PlayerWriter();
    }
    
    /**
     * Is this game a test run from the map editor? If so, many options
     * are disabled to discourage "cheating" in this manner.
     * @return      true if the game was opened from within the map editor.
     */
    public boolean isTestGame() {
        return (profile.getTestScenarioURL() != null || profile.getTestBoardID() != null);
    }
    
    public int getHeightOfGameScreen() {
    	return (boardView != null) ? boardView.getHeight() : Window.getClientHeight();
    }
    /*
    public boolean isGamePaused() {
        return (pauseCalls >0);
    }
    */
    
    public boolean isGameInProgress() {
        return gameInProgress;
    }
    
	public void quitApplication() {
		if (profile.isAirProvided()) {
			closeWindow();
		}
	}

    private native void closeWindow() /*-{
	    $wnd.close();
	}-*/;
    
    /* --------------------------------------------------------------------- */
    /* Accessors (I hate these, but it's simple) */
    /* --------------------------------------------------------------------- */
    
    // There are now 3 references to this that I can't get rid of.
    public Board getBoard() {
        return board;
    }
    
    public Player getPlayer() {
        return player;
    }

    public Profile getProfile() {
        return profile;
    }
    
    /**
     * Oh, you can whine about context objects, but the tools in this 
     * environment are limited. 
     * @return  an event object.
     */
    public Event createEvent() {
        return new Event(player, board);
    }
    
    /* --------------------------------------------------------------------- */
    /* Game start/stop/save. If feels pretty darn messy.
    /* --------------------------------------------------------------------- */

    public void changeFonts() {
        board.rerenderBoard();
        Events.get().fireInventoryChanged(player.getBag());
    }
    
    public void mainMenu() {
        DialogManager.get().push(MainMenu.class);
    }
    public void testGame() {
        // Because the game has not yet been paused, continueGame() will not fire this
        // event. We need to fire it directly.
        // Events.get().fireGameResumed();
        DialogManager.get().popAll();
        gameInProgress = true;
        player = new Player(inputManager, "Test Player", 
            "file:///"+profile.getTestScenarioURL(), profile.getTestBoardID(), -1, -1);
        Events.get().firePlayerChanged(player);
        Events.get().fireInventoryChanged(player.getBag());
        loadBoard(player, LAND_PLAYER);
    }
    public void newGame(String name, String scenarioUrl) {
        DialogManager.get().popAll();
        gameInProgress = true;
        player = new Player(inputManager, name, scenarioUrl, "start", -1, -1);
        Events.get().firePlayerChanged(player);
        Events.get().fireInventoryChanged(player.getBag());

        loadBoard(player, LAND_PLAYER);
    }
    
    public void selectSaveGameName() {
        if (!Game.get().isTestGame()) {
            DialogManager.get().push(SaveGameDialog.class);
        } else {
            Util.showError("There is no save game support.");
        }
    }
    
    public void saveGame(String saveUnderName) {
        saveGame(saveUnderName, LAND_WITHOUT_EVENTS);
    }
    
    public void saveGameAndQuit() {
        if (!Game.get().isTestGame()) {
            saveGame(player.getName(), QUIT);
        } else {
            Util.showError("There is no save game support.");
        }
    }
    
    private void saveGame(final String saveUnderName, final Command callback) {
        if (loadingTimer != null) {
            return;
        }
        DialogManager.get().push(LoadingDialog.class);
        loadingTimer = new Timer() {
            @Override
            public void run() {
                if (!board.hasNonTransientEffect()) {
                    this.cancel();
                    // Save current board, all unsaved boards, and the player's state.
                    Map<String, String> boards = new HashMap<String, String>();
                    boards.putAll(player.getUnsavedMaps());
                    // Then overwrite with the current board...
                    boards.put(player.getBoardID(), boardWriter.write(board, true));
                    
                    Cell current = board.getCurrentCell();
                    
                    String currentName = player.getName();
                    player.setName(saveUnderName);
                    String playerJSON = playerWriter.write(player, current.getX(), current.getY());
                    store.saveGame(saveUnderName, currentName, playerJSON, boards, callback);

                    // Should be safe, we've copied everything over.
                    player.getUnsavedMaps().clear();
                    Events.get().firePlayerChanged(player);
                }                
            }
        };
        loadingTimer.scheduleRepeating(100);
    }
    public void loadGame(String name) {
        DialogManager.get().push(LoadingDialog.class);
        store.loadGame(name, new StoreLoadCallback() {
            public void load(String json) {
                gameInProgress = true;
                player = playerReader.read(inputManager, json);
                Events.get().firePlayerChanged(player);
                Events.get().fireInventoryChanged(player.getBag());
                loadBoard(player, LAND_PLAYER);
            }
        });
    }
    public void loadBoard(final Player player, final Command command) {
        DialogManager.get().push(LoadingDialog.class);
        board = new Board();
        String unsavedMap = (String)player.getUnsavedMaps().get(player.getBoardID());
        if (unsavedMap != null) {
            Log.get().debug(player.getBoardID() + " unsaved, retrieved from memory");
            boardReader.read(boardView, player, board, unsavedMap, command);
        } else {
            store.loadBoard(player.getName(), player.getBoardID(), new StoreLoadCallback() {
                public void load(String json) {
                    if (json != null) {
                        Log.get().debug(player.getBoardID() + " retrieved from DB");
                        boardReader.read(boardView, player, board, json, command);
                    } else {
                        Log.get().debug(player.getBoardID() + " never visited, retrieved from disk");
                        boardLoader.load(boardView, player, board, command);
                    }
                }
            });
        }
    }
    public void promptForScenario(final NewGameDialog dialog) {
        store.loadScenario(new StoreLoadCallback() {
            public void load(String filePath) {
                ScenarioLoader loader = new ScenarioLoader();
                loader.load(filePath, dialog);
            }
        });
    }
    public void deleteGame(String name, Command callback) {
        store.deleteGame(name, callback);
    }
    public void getSavedGames(NamesCallback callback) {
        store.getSavedGames(callback);
    }
    
    public void gameOver(final String url, final boolean hasWon) {
        gameInProgress = false;
        Timer timer = new Timer() {
            private Style style = RootPanel.getBodyElement().getStyle();
            private int i=0;
            @Override
            public void run() {
                style.setProperty("backgroundColor", Color.random().toString());
                if (!hasWon || i++ >= 20) {
                    cancel();
                    style.setProperty("backgroundColor", Color.BLACK.toString());
                    GameOverDialog dialog = new GameOverDialog();
                    dialog.setHTML(player.getScenarioURL()+url);
                    DialogManager.get().push(dialog);
                }
            }
        };
        timer.scheduleRepeating(100);
    }
    
    public void confirmQuit() {
        if (gameInProgress && !isTestGame()) {
            DialogManager.get().push(ConfirmQuitDialog.class);
        } else {
            quitApplication();
        }            
    }
    
    /* --------------------------------------------------------------------- */
    /* In-game actions
    /* --------------------------------------------------------------------- */
    
    public void fireWeapon(Direction dir) {
        Event event = createEvent();
        Item item = player.getBag().getSelected();
        // It's really common to want to fire a grenade...
        if (item instanceof Grenade) {
            throwItem(dir);
        } else {
            Item ammo = item.onFire(event);
            if (ammo != null) {
                Cell current = board.getCurrentCell();
                shoot(event, current, player, ammo, dir);
            }
        }
    }
    
    public void shoot(Cell agentLoc, Agent agent, Item ammo, Targeting targeting) {
        Direction dir = AgentUtils.getDirectionToCellRangeLimited(
            agentLoc, board.getCurrentCell(), targeting);
        if (dir != null) {
            Event event = createEvent();
            shoot(event, agentLoc, agent, ammo, dir);
        }
    }

    public void shoot(Event event, Cell agentLoc, Piece originator, Item ammo, Direction dir) {
        if (originator == null) {
            throw new RuntimeException("Something tried to shoot with a null originator");
        }
        InFlightItem flier = new InFlightItem(ammo, dir, originator);
        agentLoc.getTerrain().onFlyOver(event, agentLoc, flier);
        if (!event.isCancelled()) {
            agentLoc.getEffects().addInFlightItem(flier);
        }
        event.cancel();
    }
    
    public void setToThrowItem() {
        if (player.getBag().getSelected() != PlayerBag.EMPTY_HANDED) {
            Events.get().fireMessage(board.getCurrentCell(), "Throw in what direction?");    
        } else {
            inputManager.clearActionBuffer();
        }
    }
    
    public void throwItem(Direction direction) {
        Event event = createEvent();
        Item item = player.getBag().getSelected();
        Cell current = board.getCurrentCell();
        if (item == PlayerBag.EMPTY_HANDED) {
            return;
        } else if (item.is(AMMUNITION)) {
            // It would disappear... it's ammo. And that's wierd. So let's just 
            // not allow the player to throw it.
            Events.get().fireMessage(current, "Uh, you don't throw ammo, you use it with a weapon...");
            return;
        }
        InFlightItem flier = new InFlightItem(item, direction, player);
        current.getTerrain().onFlyOver(event, current, flier);
        if (event.isCancelled()) {
            return;
        }
        item.onThrow(event, current);
        if (event.isCancelled()) {
            return;
        }
        player.getBag().remove(item);
        current.getEffects().addInFlightItem(flier);
    }
    
    public void agentMove(Cell agentLoc, Agent agent, Targeting targeting) {
        // Duration d = new Duration();
        Direction direction = AgentUtils.findPathToTarget(agentLoc, agent, targeting);
        if (direction != null) {
            Event event = createEvent();
            agentMove(event, agentLoc, agent, direction);
        }
        // Log.get().debug(agent.getName(), "move time", Integer.toString(d.elapsedMillis()));
    }
    
    public void agentMove(Event event, Cell agentLoc, Agent agent, Direction direction) {
        Cell next = agentLoc.getAdjacentCell(direction);
        agentLoc.getTerrain().onAgentExit(event, agent, agentLoc, direction);
        if (event.isCancelled()) {
            return;
        }
        if (next == null) {
            // Agents can't cross map boundaries. 
            event.cancel();
            return;
        }
        next.getTerrain().onAgentEnter(event, agent, next, direction);
        if (event.isCancelled()) {
            return;
        }
        // Creating a reference to this agent is necessary when the player
        // dies in the course of all this, or an NPE is thrown.
        Agent nextAgent = next.getAgent();
        if (nextAgent != null) {
            if (nextAgent.is(PLAYER)) { 
                agent.onHit(event, agentLoc, next, nextAgent);
                // agent.onHit(event, next, nextAgent, direction);    
            }
            nextAgent.onHitBy(event, agentLoc, agent, direction);
            if (event.isCancelled()) {
                return;
            }
        }
        // It has to be agentLoc.getAgent()... don't ask me why.
        agentLoc.moveAgentTo(next, agentLoc.getAgent());
        if (!next.isBagEmpty()) {
            next.onSteppedOn(event, next, next.getAgent());
        }
    }
    /**
     * After a board is loaded, there's a bunch of work to show the 
     * right UI components, and to fire the events for the cell the player
     * has landed on.
     */
    public void land() {
        landWithoutEvents();
        Cell current = board.getCurrentCell();
        if (current != null) { // which is the case when using the map editor...
            Events.get().fireGameResumed();
            Event event = Game.get().createEvent();
            for (Cell adj : current.getAdjacentCells(null)) {
                adj.getTerrain().onAdjacentTo(event, current);    
            }
            current.getTerrain().onEnter(event, player, current, null);
        }
    }
    
    /** 
     * Land the player after a save, without firing events on the cell.
     */
    public void landWithoutEvents() {
        Events.get().fireHideAllMessages();
        DialogManager.get().popAll();
        DOM.getElementById("layout").getStyle().setProperty("visibility", "visible");
        loadingTimer = null;
    }
    
    public void moveVertical() {
        Terrain current = board.getCurrentCell().getApparentTerrain();
        if (current instanceof StairsUp) {
            move(Direction.UP);
        } else if (current instanceof StairsDown) {
            move(Direction.DOWN);
        } else if (current instanceof CaveEntrance) {
            if (board.isOutside()) {
                move(Direction.DOWN);
            } else {
                move(Direction.UP);
            }
        }
    }
    
    public void move(Direction direction) {
        final Event event = createEvent();
        Cell origin = board.getCurrentCell();
        Cell target = origin.getAdjacentCell(direction);
        
        origin.getTerrain().onExit(event, player, origin, direction);
        if (event.isCancelled()) {
            return;
        }
        // if they're equal, player is going up or down. onEnter, et al,
        // these are irrelevant for up/down, but the game must ensure 
        // it's always legal (e.g. can't push an agent onto stairs).
        // if (target == null || target.equals(origin)) {
        if (target == null) {
            moveOffBoard(event, direction);
            return;
        }
        target.getTerrain().onEnter(event, player, target, direction);
        if (event.isCancelled()) {
            return;
        }
        if (target.getAgent() != null) {
            Agent agent = target.getAgent();
            player.onHit(event, origin, target, agent);
            // WARN: This isn't right. The cell and the agent should always be 
            // the same, and the player hasn't moved yet.
            // agent.onHitBy(event, target, player, direction);
            agent.onHitBy(event, origin, player, direction);
            if (event.isCancelled()) {
                return;
            }
        }
        origin.getTerrain().onNotAdjacentTo(event, origin);
        for (Cell adj : origin.getAdjacentCells(null)) {
            adj.getTerrain().onNotAdjacentTo(event, adj);    
        }
        
        board.visitRange(origin, Player.DETECT_HIDDEN_RANGE, true, new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                cell.getTerrain().onNotAdjacentTo(event, cell);
                return true;
            }
        });
        origin.moveAgentTo(target, player);
        board.visitRange(target, Player.DETECT_HIDDEN_RANGE, true, new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                cell.getTerrain().onAdjacentTo(event, cell);
                return true;
            }
        });
        if (!target.isBagEmpty()) {
            target.onSteppedOn(event, target, player);
        }
    }
    /**
     * Do not allow an agent to move due to animation, when we're in the middle
     * of figuring out if their dead or not. Or we won't successfully remove 
     * the agent from the cell (it'll be in a different cell), and it'll 
     * simultaneously die and move away. Rare but annoying. This basically 
     * means changeHealth should never be called anywhere but here, how 
     * to enforce? 
     *  
     * @param cell
     * @param agent
     * @param delta
     */
    public void damage(Cell cell, Agent agent, int delta) {
        // If striking the player, delta is subtracted; if striking any other 
        // agent, delta is added to the chance to hit. This keeps all numbers 
        // positive, so they are easier to use.
        
        // You cannot do this because the player, when wrapped in StandIn,
        // doesn't meet the contract.
        int value = agent.changeHealth(delta);
        if (value == 0) {
            die(cell, agent);
        }
    }
    public void die(Cell cell, Agent agent) {
        Event event = createEvent();
        agent.onDie(event, cell);
        if (!event.isCancelled()) {
            // There are cases where an agent is transformed just prior to being 
            // killed. When this happens, removeAgent() cannot remove the agent 
            // passed into this method from the array of animated pieces, because 
            // the piece instance has changed. So we use both the agent actually 
            // in the cell, and the agent passed in. There's no easy way to 
            // address this, it's a side-effect of the approach taken here to 
            // identity and immutability.
            cell.removeAgent( cell.getAgent() );
            cell.removeAgent( agent );
            cell.getEffects().add(new Hit(agent));
        }
    }
    public void drop(Event event, Cell cell, Item item) {
        if (item.is(AMMUNITION)) {
            item.onThrowEnd(event, cell);
        } else {
            // Clear cancel and test if the terrain allows a drop here, finally,
            // execute the item's onFall (which can also cause the item to disappear.
            // This gets into ammunition territory.
            event.suppressCancel();
            cell.getTerrain().onDrop(event, cell, item);
            if (!event.isCancelled()) {
                item.onThrowEnd(event, cell);
                // Ammunition lands on the ground.
                if (!event.isCancelled()) {
                    cell.getBag().add(item);
                }
            }
        }
    }
    public void selectUp() {
        player.getBag().selectUp();
    }
    public void selectDown() {
        player.getBag().selectDown();
    }
    public void selectEmptyHanded() {
        player.getBag().selectEmptyHanded();
    }
    public void useSelectedItem() {
        player.getBag().getSelected().onUse(createEvent());
    }
    public void pickupItem(int index) {
        pickupItem(board.getCurrentCell(), index);
    }
    public void pickupItem(Cell cell, int index) {
        Event event = createEvent();
        Item item = (Item)cell.getBag().get(index);
        if (item != null) {
            if (!player.enforceWeakness(event, cell, item)) {
                cell.getTerrain().onPickup(event, cell, player, item);
                if (!event.isCancelled()) {
                    cell.getBag().remove(item);
                    player.getBag().add(item);
                }
            }
        }
    }
    public void dropSelectedItem() {
        Event event = createEvent();
        Item item = player.getBag().getSelected();
        if (item == PlayerBag.EMPTY_HANDED) {
            return;
        }
        // If item cancels the event, it is not dropped
        Cell current = board.getCurrentCell();
        item.onDrop(event, current);
        if (event.isCancelled()) {
            return;
        }
        player.getBag().remove(item);
        
        // If terrain cancels event, it does not fall to floor. 
        current.getTerrain().onDrop(event, current, item);
        if (event.isCancelled()) {
            return;
        }
        current.getBag().add(item);
    }
    
    public void moveSelectedUp() {
        player.getBag().moveSelectedUp();
    }
    public void moveSelectedDown() {
        player.getBag().moveSelectedDown();
    }
    public void selectFirstWeapon() {
        player.getBag().selectFirstWeapon();
    }
    /**
     * Although you can drop or throw things to the point where they are not
     * listed in the menu of ten items, you can then rotate the list to see
     * each item, for the obscure case where you'd want to do that.
     */
    public void rotateItems() {
        Cell cell = board.getCurrentCell();
        Bag<Item> bag = cell.getBag();
        if (bag.size() > 1) {
            List<Bag.Entry<Item>> entries = bag.asEntryList();
            entries.add(entries.remove(0));
        }
    }
    /**
     * For those using the number pad, it's helpful to use "5" for default 
     * actions. Sometimes though, the action to take is ambiguous. Here, 
     * we will pick up an item if it is underfoot, or move vertically if 
     * that is possible, otherwise we'll use the currently selected item.
     */
    public void defaultForCurrentCell() {
        if (board.getCurrentCell().getBag().isEmpty()) {
            Terrain t = board.getCurrentCell().getTerrain();
            if (t.is(VERTICAL)) {
                moveVertical();
            } else {
                useSelectedItem();    
            }
        } else {
            pickupItem(0);
        }
    }
    
    /* --------------------------------------------------------------------- */
    /* Private */
    /* --------------------------------------------------------------------- */
    
    public void teleport(final String boardID, final int x, final int y) {
        if (loadingTimer != null) {
            return;
        }
        Events.get().fireHideAllMessages();
        DialogManager.get().push(LoadingDialog.class);
        loadingTimer = new Timer() {
            @Override
            public void run() {
                if (!board.hasNonTransientEffect()) {
                    this.cancel();
                    Events.get().fireGamePaused();
                    String json = boardWriter.write(board, true);
                    player.getUnsavedMaps().put(player.getBoardID(), json);
                    
                    player.setBoardID(boardID);
                    player.setStartXY(x, y);

                    loadBoard(player, LAND_PLAYER);
                }
            }
        };
        loadingTimer.scheduleRepeating(100);
    }
    private void moveOffBoard(Event event, final Direction direction) {
        if (loadingTimer != null) {
            return;
        }
        Events.get().fireHideAllMessages();
        
        // Diagonals now allowed... there's no way you can tie two maps together
        // where a diagonal move won't lead you into rock, unless we preload the 
        // target map and examine it, and it isn't worth it.
        final String url = board.getAdjacentBoard(direction);
        if (direction.isDiagonal()) {
            event.cancel("You cannot move diagonally off a map");
        } else if (url == null){
            event.cancel("Currently this leads nowhere");
        } else {
            DialogManager.get().push(LoadingDialog.class);
            loadingTimer = new Timer() {
                @Override
                public void run() {
                    if (!board.hasNonTransientEffect()) {
                        this.cancel();
                        Events.get().fireGamePaused();
                        String json = boardWriter.write(board, true);
                        player.getUnsavedMaps().put(player.getBoardID(), json);
                        
                        // Now adjust player to the new anticipated board and location.
                        // Player now acts as a parameter object to the board loader/readers.
                        player.setBoardID(url);
                        int x = board.getCurrentCell().getX();
                        int y = board.getCurrentCell().getY();
                        
                        if (Direction.UP == direction || Direction.DOWN == direction) {
                            player.setStartXY(x, y);
                            loadBoard(player, LAND_PLAYER);
                        } else if (Direction.NORTH == direction) {
                            player.setStartXY(x, Board.ROWS-1);
                            loadBoard(player, LAND_PLAYER);
                        } else if (Direction.SOUTH == direction) {
                            player.setStartXY(x, 0);
                            loadBoard(player, LAND_PLAYER);
                        } else if (Direction.EAST == direction) {
                            player.setStartXY(0, y);
                            loadBoard(player, LAND_PLAYER);
                        } else if (Direction.WEST == direction) {
                            player.setStartXY(Board.COLUMNS-1, y);
                            loadBoard(player, LAND_PLAYER);
                        }
                    }
                }
            };
            loadingTimer.scheduleRepeating(100);
        }
    }
    /*
    public void fixMe() {
        player.remove(WEAK);
        player.remove(POISONED);
        player.remove(PARALYZED);
        player.changeHealth( player.changeHealth(0)-Player.MAX_HEALTH );
    }
    */
}
