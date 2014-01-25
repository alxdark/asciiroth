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
package us.asciiroth.client;

import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.items.ConsumesAmmo;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.ErrorDialog;
import us.asciiroth.client.ui.Log;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Element;

/**
 * Static utility methods for the game.
 *
 */
public class Util {
    
    /**
     * A generic uncaught exception handler that defers to the minimal logging
     * implementation. Registered for both the game and the editor, currently.
     */
    public static final UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = new UncaughtExceptionHandler() {
        public void onUncaughtException(Throwable e) {
            Log.get().debug(e.getClass().getName() + ": " + e.getMessage());
            showError(e.getClass().getName() + ":<br>" + e.getMessage());
        }
    };
    
    private static ErrorDialog errorDialog;
    
    private static final String PIECE_DESCRIPTION = 
        "<span class='s' style='color:{0}; background-color:{1}'>{2}</span>&#160;{3}{4}";

    /**
     * Renders ammunition next to its associated weapon, rather than rendering it separately.
     * What a piece of work. I just never marked any of this out.
     * @param outside
     * @param bag
     * @param piece
     * @param count
     * @return
     */
    /*
    public static String renderSymbolAndLabel2(boolean outside, PlayerBag bag, Piece piece, int count) {
        Symbol symbol = piece.getSymbol();
        String color = (symbol.getColor(outside) != null) ? symbol.getColor(outside).toString() : "inherit";
        String bgcolor = (symbol.getBackground(outside) != null) ? symbol.getBackground(outside).toString() : "inherit";
        String p = (count > 1) ? (" (x"+count+")") : "";
        if (piece.is(RANGED_WEAPON)) {
            int c = 0;
            if (piece instanceof AmmoGun) {
                c = bag.getCount( bag.getByName("Bullet") );
            } else if (piece instanceof AmmoBow) {
                c = bag.getCount( bag.getByName("Arrow") );
            } else if (piece instanceof AmmoParalyzer) {
                c = bag.getCount( bag.getByName("Parabullet") );
            } else if (piece instanceof AmmoSling) {
                c = bag.getCount( bag.getByName("Rock") );
            } else {
                c = -1;
            }
            if (c > -1) {
                p += (" " + c + " shots");
            }
        } else if (piece.is(AMMUNITION)) {
            if ((piece instanceof Bullet        && bag.getByName("AmmoGun") != null) ||
                (piece instanceof Arrow         && bag.getByName("AmmoBow") != null) ||
                (piece instanceof Parabullet    && bag.getByName("AmmoParalyzer") != null) ||
                (piece instanceof Rock          && bag.getByName("AmmoSling") != null)) {
                return null;
            }
        }
        return format(PIECE_DESCRIPTION, color, bgcolor, symbol.getAdjustedEntity(), piece.getName(), p);
    }
    */
    
    /**
     * Render the symbol with a label and a count of items, in both the cell info panel 
     * and the inventory palette. 
     * 
     * @param outside
     * @param piece
     * @param count
     * @return  a string to render
     */
    public static String renderSymbolAndLabel(boolean outside, Piece piece, int count) {
        Symbol symbol = piece.getSymbol();
        String color = (symbol.getColor(outside) != null) ? symbol.getColor(outside).toString() : "inherit";
        String bgcolor = (symbol.getBackground(outside) != null) ? symbol.getBackground(outside).toString() : "inherit";

        String p = (count > 1) ? (" (x"+count+")") : "";
        return format(PIECE_DESCRIPTION, color, bgcolor, symbol.getAdjustedEntity(), piece.getName(), p);
    }

    public static String renderSymbolAndLabel2(boolean outside, Bag.Entry<Item> entry) {
        Piece piece = entry.getPiece();
        Symbol symbol = entry.getPiece().getSymbol();
        String color = (symbol.getColor(outside) != null) ? 
            symbol.getColor(outside).toString() : "inherit";
        String bgcolor = (symbol.getBackground(outside) != null) ? 
            symbol.getBackground(outside).toString() : "inherit";
        
        int count = entry.getCount();
        boolean ca = (piece instanceof ConsumesAmmo);
        String p = "";
        if (count > 1 || ca)    p += " (";
        if (count > 1)          p += "x" + count;
        if (count > 1 && ca)    p += "; ";
        if (ca)                 p += entry.getAmmo() + " shots";
        if (count > 1 || ca)    p += ")";
        return format(PIECE_DESCRIPTION, color, bgcolor, 
            symbol.getAdjustedEntity(), piece.getName(), p);
    }
    
    /**
     * Given a base color and an alternate, oscillate between them. I'm sure there's
     * better ways to do this, but this works (== looks cool).
     * @param from
     * @param to
     * @param rate
     * @param frame
     * @return  new color
     */
    public static Color oscillate(Color from, Color to, int rate, int frame) {
        if (from == null) {
            from = Color.BLACK;
        }
        if (to == null) {
            to = Color.BLACK;
        }
        int red = oscillateComponent(from.getRed(), to.getRed(), rate, frame);
        int green = oscillateComponent(from.getGreen(), to.getGreen(), rate, frame);
        int blue = oscillateComponent(from.getBlue(), to.getBlue(), rate, frame);
        return new Color(red, green, blue);
    }
    
    // It's all magic... I have no idea what this does.
    // Trained monkeys wrote it. Don't touch it. I hate this method. 
    private static int oscillateComponent(int start, int end, int frames, int frame) {
        if (start == end) {
            return start;
        }
        int diff = Math.abs(end-start);
        int steps = (int)Math.ceil(diff/frames);
        int inc = (Math.abs((frame%(frames*2)) - frames) * steps);
        
        if (end < start) {
            int finalC = (start - inc);
            return (finalC < end) ? end : (finalC > start) ? start : finalC;
        }
        int finalC = (start + inc);
        return (finalC < start) ? start : (finalC > end) ? end : finalC;
    }
    
    public static boolean isNotBlank(String str) {
        return !Util.isBlank(str);
    }
    
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i=0, len = str.length(); i < len; ++i) {
            if (str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get the simple name for this class (the class name without the package), 
     * which is used for serializing and serializing pieces. GWT doesn't support
     * the Java library methods for this such as Class.class.getSimpleName().
     * @param c     the class of the piece
     * @return      the string simple name of the class
     */
    public static <T> String getType(Class<T> c) {
        if (c == null) {
            return null;
        }
        return c.getName().substring(c.getName().lastIndexOf(".")+1);
    }
    
    /**
     * Get the simple name of this object's class, which is used for serializing 
     * and deserializing pieces in the game. GWT doesn't support the Java library
     * methods for this such as obj.getClass().getSimpleName().
     * @param obj   a piece
     * @return      the string simple name of the class
     */
    public static String getType(Object obj) {
        if (obj == null) {
            return null;
        }
        String s = obj.getClass().getName();
        return s.substring(s.lastIndexOf(".")+1);
    }
    
    /**
     * Show an error to the user. This could just as well be a Window.alert() 
     * dialog... 
     * @param error
     */
    public static void showError(String error) {
        if (errorDialog == null) {
            errorDialog = new ErrorDialog(error);
        } else {
            errorDialog.setMessage(error);
        }
        DialogManager.get().push(errorDialog);
    }
    
    public static void showError(String error, String... args) {
        String e = Util.format(error, args);
        showError(e);
    }
    
    /**
     * Set the opacity on this element. Does not account for opacity in Internet
     * Explorer. I feel this is likely to be fixed by IE8, least I hope.
     * 
     * @param el
     * @param opacity   a double value for opacity (from 0.0 to 1.0)
     */
    public static void setOpacity(Element el, double opacity) {
        if (opacity < 0.00001) {
            opacity = 0.0;
        }
        if (opacity > 1.0) {
            opacity = 1.0;
        }
        el.getStyle().setProperty("opacity", Double.toString(opacity));
    }    
    
    /**
     * Get an item of a similar type, but change it to the specified color.
     * @param item      the item serving as a prototype
     * @param color     the new color for the new item created
     * @return          the item with a new color.
     */
    public static Item getItemOtherColor(Item item, Color color) {
        String key = Registry.get().getKey((Item)item);
        key = key.replaceAll("\\b"+item.getColor().getName()+"\\b", color.getName());
        return (Item)Registry.get().getPiece(key);
    }
    
    /**
     * Return a value or the lower/upper bound of a range within which the 
     * value should fall.
     * @param value     the value being tested
     * @param lower     the lowest number that can be returned
     * @param upper     the highest number that can be returned
     * @return          the value, or the lower/upper value if the provided
     *                  value falls outside of the range
     */
    public static int inRange(int value, int lower, int upper) {
        return (value < lower) ? lower : (value > upper) ? upper : value;
    }
   
    /**
     * Format a string of the format "This is a {0} format string {1}" by 
     * substituting the string values provided (there must be one for each
     * braced substitution marker in the string). 
     * @param format    a string to format
     * @param values    one or more values to substitute into the string.
     * @return          the formatted string
     */
    public static String format(String format, String... values) {
        return Util.format(format, convert(values));
    }

    /**
     * Format a string of the format "This is a {0} format string {1}" by 
     * substituting the int values provided (there must be one for each
     * braced substitution marker in the string). 
     * @param format    a string to format
     * @param values    one or more values to substitute into the string.
     * @return          the formatted string
     */
    public static String format(String format, int... values) {
        return Util.format(format, convert(values));
    }
    
    private static native String format(String format, JavaScriptObject values) /*-{
        // Ext's String.format() method.
        return format.replace(/\{(\d+)\}/g, function(m, i){
            return (values[i] == null) ? "" : values[i];
        });        
    }-*/;
    
    // Interesting fix to an annoying problem:
    // http://www.ibm.com/developerworks/web/library/j-gwtcontrols/
    
    private static JavaScriptObject convert(String[] array) { 
        JavaScriptObject result = newJSArray(array.length); 
        for (int i = 0; i<array.length; i++) { 
          arraySet(result, i, array[i]); 
        } 
        return result; 
    }

    private static JavaScriptObject convert(int[] array) { 
        JavaScriptObject result = newJSArray(array.length); 
        for (int i = 0; i<array.length; i++) { 
          arraySet(result, i, array[i]); 
        } 
        return result; 
    }
    
    private static native void arraySet(JavaScriptObject array, int index, String value) /*-{ 
        array[index] = value; 
    }-*/;
    private static native void arraySet(JavaScriptObject array, int index, int value) /*-{ 
        array[index] = value; 
    }-*/;
    
    private static native JavaScriptObject newJSArray(int length) /*-{ 
        return new Array(length);  
    }-*/;
}
