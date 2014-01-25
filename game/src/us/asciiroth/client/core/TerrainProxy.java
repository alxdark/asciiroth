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

/**
 * A piece (always terrain) that proxies another terrain. When the 
 * encompassing terrain is rendered, it's the proxied terrain returned
 * by the <code>getProxiedTerrain</code> method that is used for rendering.
 *
 */
public interface TerrainProxy {
    /** 
     *  @return     the proxied terrain which will be used for rendering 
     */
    public Terrain getProxiedTerrain();
    
    /**
     * 
     * @return      a new proxy, with the same behavior but wrapping a 
     *              different terrain
     */
    public Terrain proxy(Terrain terrain);
}
