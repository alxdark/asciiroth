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
package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Crate;

/**
 * A container (chest or crate) that is trapped and explodes when opened. 
 * This trap creates an energy field that weakens the player. 
 */
public class EnergyTrapContainer extends TrapContainerBase {
    
    /**
     * Constructor.
     * @param terrain
     */
    public EnergyTrapContainer(Terrain terrain) {
        super(terrain);
        assert terrain != null : "EnergyTrapContainer: terrain is null";
        
        trapType = ENERGY;
    }
    public Terrain proxy(Terrain terrain) {
        return new EnergyTrapContainer(terrain);
    }
    /** Type serializer. */
    public static final Serializer<EnergyTrapContainer> SERIALIZER = new BaseSerializer<EnergyTrapContainer>() {
        public EnergyTrapContainer create(String[] args) {
            return new EnergyTrapContainer(unescTerrain(args[1]));
        }
        public EnergyTrapContainer example() {
            return new EnergyTrapContainer(new Crate(null));
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(EnergyTrapContainer tc) {
            return "EnergyTrapContainer|"+esc(tc.terrain);
        }
        public String template(String type) {
            return "EnergyTrapContainer|{terrain}";
        }
    };
}
