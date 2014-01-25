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

import java.util.HashMap;
import java.util.Map;

/**
 * A typesafe enum that represents anything that has a boolean state, such as doors (open or 
 * closed), switches (on or off), etc. 
 *
 */
public class State {

    private static final Map<String, State> byName = new HashMap<String, State>();
    /** Retrieve the state by name (either "on" or "off"). 
     * @param name  the name of the state object to retrieve
     * @return      a state constant, or null if the name matches none
     */
    public static State byName(String name) {
        return byName.get(name);
    }
    
    /** A piece in the on state. */
    public static final State ON = new State("on");
    /** A piece in the off state. */
    public static final State OFF = new State("off");
    
    private String name;
    
    private State(String name) {
        this.name = name;
        String lower = name.toLowerCase();
        String caps = name.substring(0,1).toUpperCase() + name.substring(1);
        byName.put(lower, this);
        byName.put(caps, this);
    }
    /**
     * Get the name of the current state
     * @return  either "on" or "off"
     */
    public String getName() {
        return name;
    }
    /**
     * Is this state the ON state?
     * @return  true if equal to State.ON, false otherwise
     */
    public boolean isOn() {
        return (this == ON);
    }
    /**
     * Is this state the OFF state?
     * @return  true if equal to State.OFF, false otherwise
     */
    public boolean isOff() {
        return (this == OFF);
    }
}
