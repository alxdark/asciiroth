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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.SILVER;
import static us.asciiroth.client.core.Flags.AMMO_ENLIVENER;
import static us.asciiroth.client.core.Flags.ETHEREAL;

import java.util.ArrayList;
import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.effects.InFlightItem;

/**
 * A reflector is an abstract puzzle piece that has a "reflector" pointing in a 
 * given direction. When a projectile of some sort is thrown or shot at a reflector,
 * it will bounce off of it, reflected in another direction depending on the 
 * current orientation of the reflector (this is easier seen than explained). 
 * Further, the flight is extended by another standard throw as if the object had
 * picked up energy from the reflector.
 * 
 */
public class Reflector extends AbstractTerrain implements ColorListener {

    private static final List<Direction> directions = new ArrayList<Direction>();
    static {
        directions.add(Direction.NORTH);
        directions.add(Direction.NORTHEAST);
        directions.add(Direction.EAST);
        directions.add(Direction.SOUTHEAST);
    }
    
    private final Direction direction;
    
    public Reflector(Direction direction, Color color) {
        super(color.getName() + " Reflector", (ETHEREAL | AMMO_ENLIVENER), color, 
            (direction == Direction.NORTH) ? new Symbol("|", color, SILVER) :
            (direction == Direction.NORTHEAST) ? new Symbol("/", color, SILVER) :
            (direction == Direction.EAST) ? new Symbol("&#151;", color, SILVER) :
            new Symbol("\\", color, SILVER)
        );
        if (!directions.contains(direction)) {
            throw new RuntimeException("Reflector takes N/NE/E/SE directions only");
        }
        this.direction = direction;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        rotate(cell);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        rotate(cell);
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        Direction reflected = reflectedDirection(flier.getDirection());
        flier.setDirection(reflected);
        flier.setOriginator(this);
    }

    private Direction reflectedDirection(Direction incoming) {
        if (direction == Direction.NORTH) {
            if (incoming == Direction.SOUTHEAST) {
                return Direction.SOUTHWEST;
            } else if (incoming == Direction.SOUTHWEST) {
                return Direction.SOUTHEAST;
            } else if (incoming == Direction.NORTHEAST) {
                return Direction.NORTHWEST;
            } else if (incoming == Direction.NORTHWEST) {
                return Direction.NORTHEAST;
            }
        } else if (direction == Direction.NORTHEAST) {
            if (incoming == Direction.SOUTH) {
                return Direction.WEST;
            } else if (incoming == Direction.WEST) {
                return Direction.SOUTH;
            } else if (incoming == Direction.NORTH) {
                return Direction.EAST;
            } else if (incoming == Direction.EAST) {
                return Direction.NORTH;
            }
        } else if (direction == Direction.EAST) {
            if (incoming == Direction.SOUTHEAST) {
                return Direction.NORTHEAST;
            } else if (incoming == Direction.NORTHEAST) {
                return Direction.SOUTHEAST;
            } else if (incoming == Direction.SOUTHWEST) {
                return Direction.NORTHWEST;
            } else if (incoming == Direction.NORTHWEST) {
                return Direction.SOUTHWEST;
            }
        } else if (direction == Direction.SOUTHEAST) {
            if (incoming == Direction.EAST) {
                return Direction.SOUTH;
            } else if (incoming == Direction.SOUTH) {
                return Direction.EAST;
            } else if (incoming == Direction.NORTH) {
                return Direction.WEST;
            } else if (incoming == Direction.WEST) {
                return Direction.NORTH;
            }
        }
        return incoming.getReverseDirection();
    }
    
    private void rotate(Cell cell) {
        int index = directions.indexOf(direction);
        index = (index == (directions.size()-1)) ? 0 : (index+1);
        Direction dir = directions.get(index);
        
        String key = Util.format("Reflector|{0}|{1}", dir.getName(), color.getName());
        Reflector reflector = (Reflector)Registry.get().getPiece(key);
        cell.setTerrain(reflector);
    }
    
    public static final Serializer<Reflector> SERIALIZER = new BaseSerializer<Reflector>() {
        public Reflector create(String[] args) {
            return new Reflector(Direction.byName(args[1]), Color.byName(args[2]));
        }
        public Reflector example() {
            return new Reflector(Direction.NORTHEAST, Color.STEELBLUE);
        }
        public String store(Reflector r) {
            return Util.format("Reflector|{0}|{1}", r.direction.getName(), r.color.getName());
        }
        public String template(String type) {
            return "Reflector|{direction}|{color}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
