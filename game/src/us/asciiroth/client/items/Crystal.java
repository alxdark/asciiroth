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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.STEELBLUE;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A crystal. It can be used to activate a Pylon, 
 * which serves as a kind of transporter.
 *
 */
public class Crystal extends AbstractItem {

    protected Crystal(Color color) {
        super(color.getName() + " Crystal", 0, color, new Symbol("&diams;", color));
    }

    /** Type serializer. */
    public static final Serializer<Crystal> SERIALIZER = new Serializer<Crystal>() {
        public Crystal create(String[] args) {
            return new Crystal(Color.byName(args[1]));
        }
        public Crystal example() {
            return new Crystal(STEELBLUE);
        }
        public String store(Crystal es) {
            return "Crystal|"+es.color.getName();
        }
        public String template(String type) {
            return "Crystal|{color}";
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
