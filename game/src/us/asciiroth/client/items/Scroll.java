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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Util;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.ui.DialogManager;

/**
 * A scroll that will display an HTML page in a modal dialog. The url you provide
 * should be relative to the scenario URL (the text must be distributed as part
 * of the scenario). Scrolls have a name in order to distinguish them since their
 * contents differ.
 * <p> 
 * A scroll can be created without an URL to a document to read. In that case, it
 * works more like a marker piece. For example, an NPC could give the player a 
 * "Writ of Introduction" that just serves to unlock another part of the game.
 *
 */
public class Scroll extends AbstractItem {

    private final String url;
    
    /**
     * Constructor.
     * @param name  the name to show as part of the scroll's label
     * @param url   the url to load when the scroll is read, which should be
     *              relative to the base folder of the scenario. 
     */
    public Scroll(String name, String url) {
        super(name, 0, 
            new Symbol("&sect;", WHITE, null, BLACK, null));
        this.url = url;
        if (name == null || url == null || !url.endsWith(".html")) {
            throw new RuntimeException("Scroll name or url is null");
        }
    }
    /**
     * Constructor.
     * @param name
     */
    public Scroll(String name) {
        super(name, 0, new Symbol("&sect;", WHITE, null, BLACK, null));
        this.url = null;
    }
    @Override
    public String getName() {
        return "Scroll: &ldquo;"+name+"&rdquo;";
    }
    @Override
    public String getIndefiniteNoun(String phrase) {
        return Util.format(phrase, "a scroll: &ldquo;"+name+"&rdquo;");
    }
    @Override
    public void onUse(Event event) {
        if (url != null) {
            DialogManager.get().pushScenarioDialog(event.getPlayer().getScenarioURL(), url);    
        } else {
            Events.get().fireMessage(event.getBoard().getCurrentCell(),
                "It says nothing unexpected (or interesting).");
        }
    }
    /** Type serializer. */
    public static final Serializer<Scroll> SERIALIZER = new BaseSerializer<Scroll>() {
        public Scroll create(String[] args) {
            return (args.length == 3) ? new Scroll(args[1], args[2]) : new Scroll(args[1]);
        }
        public Scroll example() {
            return new Scroll("An Example");
        }
        public String store(Scroll s) {
            return (s.url != null) ?
                Util.format("Scroll|{0}|{1}", s.name, s.url) : 
                Util.format("Scroll|{0}", s.name);
        }
        public String template(String type) {
            return "Scroll|{name}|{url?}";
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
