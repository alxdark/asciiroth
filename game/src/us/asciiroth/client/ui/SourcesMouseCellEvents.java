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
package us.asciiroth.client.ui;

/**
 * A source of mouse-related events that are expressed in terms of the cells
 * on the game board, not in terms of X/Y pixels (as the normal GWT SourcesMouseEvents
 * interface).
 *
 */
public interface SourcesMouseCellEvents {
    /**
     * Add a listener to this source of events.
     * @param listener  the listener to add
     */
    public void addMouseCellListener(MouseCellListener listener);
    /**
     * Remove the listener from this source of events.
     * @param listener  the listener to remove
     */
    public void removeMouseCellListener(MouseCellListener listener);
}
