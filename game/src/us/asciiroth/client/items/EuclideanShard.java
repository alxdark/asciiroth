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
 * A component that is necessary to activate a Euclidean engine, and those are 
 * necessary to power a Euclidean Transporter. A very difficult type of piece 
 * to activate, and the focus of the TOA scenario.
 *
 */
public class EuclideanShard extends AbstractItem {

    private EuclideanShard(Color color) {
        super(color.getName() + " Euclidean Shard", 0, color, new Symbol("&diams;", color));
    }

    /** Type serializer. */
    public static final Serializer<EuclideanShard> SERIALIZER = new Serializer<EuclideanShard>() {
        public EuclideanShard create(String[] args) {
            return new EuclideanShard(Color.byName(args[1]));
        }
        public EuclideanShard example() {
            return new EuclideanShard(STEELBLUE);
        }
        public String store(EuclideanShard es) {
            return "EuclideanShard|"+es.color.getName();
        }
        public String template(String type) {
            return "EuclideanShard|{color}";
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
