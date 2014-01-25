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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.asciiroth.client.board.Cell;

/**
 * A direction on the map, also representing the information to interact with 
 * adjacent terrain on the map or adjacent maps. 
 */
public class Direction {

    /**
     * Retrieve a direction by its name.
     * @param name  the name of the direction, in lowercase
     * @return      the direction
     */
    public static Direction byName(String name) {
        return byName.get(name);
    }
    
    private static final List<Direction> mapDirections = new ArrayList<Direction>();
    private static final List<Direction> adjDirections = new ArrayList<Direction>();
    private static final Map<String, Direction> byName = new HashMap<String, Direction>();
	
	private String name;
    private String reverseName;
	private int xDelta;
	private int yDelta;
	
	private Direction(String name, int xDelta, int yDelta, String reverseName) {
		this.name = name;
		this.xDelta = xDelta;
		this.yDelta = yDelta;
        this.reverseName = reverseName;
        String lower = name.toLowerCase();
        String caps = name.substring(0,1).toUpperCase() + name.substring(1);
        byName.put(lower, this);
        byName.put(caps, this);
	}
    /**
     * @return  the name of this direction
     */
    public String getName() {
        return name;
    }
    /**
     * @return  the change in x necessary to move in this direction
     */
    public int getXDelta() {
        return xDelta;
    }
    /**
     * @return  the change in y necessary to move in this direction
     */
    public int getYDelta() {
        return yDelta;
    }
    /**
     * @return  the direction in the reverse direction of this one
     */
    public Direction getReverseDirection() {
        return byName.get(reverseName);
    }
    
    /** 
     *  A diagonal direction: northeast, northwest, southeast or southwest.
     *  @return true if diagonal  
     */
    public boolean isDiagonal() {
        return (this == Direction.NORTHEAST || this == Direction.NORTHWEST || 
                this == Direction.SOUTHEAST || this == Direction.SOUTHWEST);
    }
    /** 
     *  A direction that leads up or down. 
     *  @return true if vertical  
     */
    public boolean isVertical() {
        return (this == Direction.UP || this == Direction.DOWN);
    }
    /**
     * A direction that leads north or south.
     * @return true if north or south
     */
    public boolean isNorthSouth() {
        return (this == Direction.NORTH || this == Direction.SOUTH);
    }
    /** 
     * A direction that leads east or west. 
     * @return true if east or west
     */
    public boolean isEastWest() {
        return (this == Direction.WEST || this == Direction.EAST);
    }
    /** 
     * A direction that includes a northerly component. 
     * @return true if northwest, north, or northeast
     */
    public boolean isNortherly() {
        return (this == Direction.NORTHWEST || this == Direction.NORTH || this == Direction.NORTHEAST);
    }
    /** 
     * A direction that includes a southerly component. 
     * @return true if southwest, south, or southeast
     */
    public boolean isSoutherly() {
        return (this == Direction.SOUTHWEST || this == Direction.SOUTH || this == Direction.SOUTHEAST);
    }
    /** 
     * A direction that includes a westerly component. 
     * @return true if northwest, west, or southwest
     */
    public boolean isWesterly() {
        return (this == Direction.NORTHWEST || this == Direction.WEST || this == Direction.SOUTHWEST);
    }
    /** 
     * A direction that includes an easterly component. 
     * @return true if northeast, east, or southeast
     */
    public boolean isEasterly() {
        return (this == Direction.NORTHEAST || this == Direction.EAST || this == Direction.SOUTHEAST);
    }
    @Override
    public String toString() {
        return name;
    }

    /**
     * Given movement from a start cell to an origin cell, what's the direction
     * that has to be taken?
     * @param origin
     * @param target
     * @return  the direction necessary to travel from "origin" to "target" cell.
     */
    public static Direction inferDirection(Cell origin, Cell target) {
        for (Direction dir : Direction.getAdjacentDirections()) {
            Cell cell = origin.getAdjacentCell(dir);
            if (cell == target) {
                return dir;
            }
        }
        return null;
    }
    
    /** 
     * A list of directions that lead to adjacent boards, the cardinal directions as well as up and down. 
     * @return a list of directions 
     */
    public static List<Direction> getMapDirections() {
        return mapDirections;
    }
    /** 
     * A list of directions that lead to the adjacent cells on a map, not including up or down.
     * @return a list of directions 
     */
    public static List<Direction> getAdjacentDirections() {
        return adjDirections;
    }
    /** No direction indicated. */
    public static final Direction NONE      = new Direction("none", 0, 0, "none");
    /** North. */
    public static final Direction NORTH     = new Direction("north", 0, -1, "south");
    /** South. */
    public static final Direction SOUTH     = new Direction("south", 0, 1, "north");
    /** East. */
    public static final Direction EAST      = new Direction("east", 1, 0, "west");
    /** West. */
    public static final Direction WEST      = new Direction("west", -1, 0, "east");
    /** Northwest. */
    public static final Direction NORTHWEST = new Direction("northwest", -1, -1, "southeast");
    /** Northeast. */
    public static final Direction NORTHEAST = new Direction("northeast", 1, -1, "southwest");
    /** Southwest. */
    public static final Direction SOUTHWEST = new Direction("southwest", -1, 1, "northeast");
    /** Southeast. */
    public static final Direction SOUTHEAST = new Direction("southeast", 1, 1, "northwest");
    /** Up. */
    public static final Direction UP        = new Direction("up", 0, 0, "down");
    /** Down. */
    public static final Direction DOWN      = new Direction("down", 0, 0, "up");
    static {
        mapDirections.add(Direction.NORTH);
        mapDirections.add(Direction.SOUTH);
        mapDirections.add(Direction.EAST);
        mapDirections.add(Direction.WEST);
        mapDirections.add(Direction.UP);
        mapDirections.add(Direction.DOWN);
    }
    static {
        adjDirections.add(Direction.NORTH);
        adjDirections.add(Direction.NORTHEAST);
        adjDirections.add(Direction.EAST);
        adjDirections.add(Direction.SOUTHEAST);
        adjDirections.add(Direction.SOUTH);
        adjDirections.add(Direction.SOUTHWEST);
        adjDirections.add(Direction.WEST);
        adjDirections.add(Direction.NORTHWEST);
    }
}
