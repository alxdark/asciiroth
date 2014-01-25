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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import us.asciiroth.client.Util;

import com.google.gwt.user.client.Random;

/**
 * There's some interesting color transformation support we 
 * can provider here, and color is an important parameter object
 * for many objects like doors and keys and transformers.
 */
public class Color {

    private static final Map<String, Color> byName = new HashMap<String, Color>();
    public static Color byName(String name) {
        return byName.get(name);
    }
    public static Color random() {
        // Pretty expensive... only used for the win game animation.
        Collection<Color> values = byName.values();
        return (Color)values.toArray()[Random.nextInt(values.size())];
        // return Collections.asList(byName.values());
        // return byName.values();
    }
    private String name;
    private String color;
    private int red;
    private int green;
    private int blue;
    
    public Color(String name, String color) {
        if ("".equals(color) && (color == null || color.length() != 7)) {
            throw new RuntimeException("Color must be in hex format");
        }
        this.name = name;
        this.color = color;
        if (color != "") {
            this.red =      Integer.parseInt(color.substring(1,3), 16);
            this.green =    Integer.parseInt(color.substring(3,5), 16);
            this.blue =     Integer.parseInt(color.substring(5,7), 16);
        }
        String lower = name.toLowerCase();
        String caps = name.substring(0,1).toUpperCase() + name.substring(1);
        byName.put(lower, this);
        byName.put(caps, this);
    }
    public Color(int red, int green, int blue) {
        this.name =     "<Unknown>";
        this.red =      checkBounds(red);
        this.green =    checkBounds(green);
        this.blue =     checkBounds(blue);
        this.color =    Util.format("rgb({0},{1},{2})", this.red, this.green, this.blue);
    }
    private int checkBounds(int value) {
        if (value < 0) return 0;
        if (value > 255) return 255;
        return value;
    }
    public String getName() {
        return name;
    }
    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }
    public Color getFrozenColor() {
        int avg = (int)((red+green+blue)/3)+150;
        return new Color(avg, avg, avg);
    }
    public Color getBurntColor() {
        // .42 .21 .06
        return new Color((int)(red*.42), (int)(green*.21), (int)(blue*.06));
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Color)) return false;
        Color that = (Color)obj;
        return (that.red == red && that.green == green && that.blue == blue);
    }
    @Override
    public String toString() {
        // Need to do this so we get consistent output when debugging, otherwise
        // color String can be different.
        // return Util.format("rgb({0},{1},{2})", this.red, this.green, this.blue);
        return color;
    }
    
    /**
     * No color. A null object pattern. Declaring this color for items that take a 
     * color, but don't use it, helps to circumvent examining the entire board for 
     * a color that is known to be a no-op. 
     */
    public static final Color NONE                  = new Color("None", "#000000");
    
    public static final Color VERYBLACK             = new Color("Very Black", "#111111");
    public static final Color NEARBLACK             = new Color("Near Black", "#333333");
    public static final Color LESSNEARBLACK         = new Color("Near Black", "#555555");
    public static final Color BURNTWOOD             = new Color("Burnt Wood", "#291405");
    
    public static final Color ALICEBLUE             = new Color("Alice Blue", "#F0F8FF");
    public static final Color ANTIQUEWHITE          = new Color("Antique White", "#FAEBD7");
    public static final Color AQUA                  = new Color("Aqua", "#00FFFF");
    public static final Color AQUAMARINE            = new Color("Aquamarine", "#7FFFD4");
    public static final Color AZURE                 = new Color("Azure", "#F0FFFF");
    public static final Color BEIGE                 = new Color("Beige", "#F5F5DC");
    public static final Color BISQUE                = new Color("Bisque", "#FFE4C4");
    public static final Color BLACK                 = new Color("Black", "#000000");
    public static final Color BLANCHEDALMOND        = new Color("Blanched Almond", "#FFEBCD");
    public static final Color BLUE                  = new Color("Blue", "#0000FF");
    public static final Color BLUEVIOLET            = new Color("Blue Violet", "#8A2BE2");
    public static final Color BROWN                 = new Color("Brown", "#A52A2A"); 
    public static final Color BURLYWOOD             = new Color("Burlywood", "#DEB887");
    public static final Color CADETBLUE             = new Color("Cadet Blue", "#5F9EA0");
    public static final Color CHARTREUSE            = new Color("Chartreuse", "#7FFF00");
    public static final Color CHOCOLATE             = new Color("Chocolate", "#D2691E");
    public static final Color CORAL                 = new Color("Coral", "#FF7F50");
    public static final Color CORNFLOWERBLUE        = new Color("Cornflower Blue", "#6495ED");
    public static final Color CORNSILK              = new Color("Cornsilk", "#FFF8DC");
    public static final Color CRIMSON               = new Color("Crimson", "#DC143C");
    public static final Color CYAN                  = new Color("Cyan", "#00FFFF");
    public static final Color DARKBLUE              = new Color("Dark Blue", "#00008B");
    public static final Color DARKCYAN              = new Color("Dark Cyan", "#008B8B");
    public static final Color DARKGOLDENROD         = new Color("Dark Goldenrod", "#B8860B");
    public static final Color DARKGRAY              = new Color("Dark Gray", "#A9A9A9");
    public static final Color DARKGREEN             = new Color("Dark Green", "#006400");
    public static final Color DARKKHAKI             = new Color("Dark Khaki", "#BDB76B");
    public static final Color DARKMAGENTA           = new Color("Dark Magenta", "#8B008B");
    public static final Color DARKOLIVEGREEN        = new Color("Dark Olive Green", "#556B2F");
    public static final Color DARKORANGE            = new Color("Dark Orange", "#FF8C00");
    public static final Color DARKORCHID            = new Color("Dark Orchid", "#9932CC");
    public static final Color DARKRED               = new Color("Dark Red", "#8B0000");
    public static final Color DARKSALMON            = new Color("Dark Salmon", "#E9967A");
    public static final Color DARKSEAGREEN          = new Color("Dark Sea Green", "#8FBC8F");
    public static final Color DARKSLATEBLUE         = new Color("Dark Slate Blue", "#483D8B");
    public static final Color DARKSLATEGRAY         = new Color("Dark Slate Gray", "#2F4F4F");
    public static final Color DARKTURQUOISE         = new Color("Dark Turquoise", "#00CED1");
    public static final Color DARKVIOLET            = new Color("Dark Violet", "#9400D3");
    public static final Color DEEPPINK              = new Color("Deep Pink", "#FF1493");
    public static final Color DEEPSKYBLUE           = new Color("Deep Sky Blue", "#00BFFF");
    public static final Color DIMGRAY               = new Color("Dim Gray", "#696969");
    public static final Color DODGERBLUE            = new Color("Dodger Blue", "#1E90FF");
    public static final Color FIREBRICK             = new Color("Firebrick", "#B22222");
    public static final Color FLORALWHITE           = new Color("Floral White", "#FFFAF0");
    public static final Color FORESTGREEN           = new Color("Forest Green", "#228B22");
    public static final Color FUCHSIA               = new Color("Fuchsia", "#FF00FF");
    public static final Color GAINSBORO             = new Color("Gainsboro", "#DCDCDC");
    public static final Color GHOSTWHITE            = new Color("Ghost White", "#F8F8FF");
    public static final Color GOLD                  = new Color("Gold", "#FFD700");
    public static final Color GOLDENROD             = new Color("Goldenrod", "#DAA520");
    public static final Color GRAY                  = new Color("Gray", "#808080");
    public static final Color GREEN                 = new Color("Green", "#008000");
    public static final Color GREENYELLOW           = new Color("Green Yellow", "#ADFF2F");
    public static final Color HONEYDEW              = new Color("Honeydew", "#F0FFF0");
    public static final Color HOTPINK               = new Color("Hot Pink", "#FF69B4");
    public static final Color INDIANRED             = new Color("Indian Red", "#CD5C5C");
    public static final Color INDIGO                = new Color("Indigo", "#4B0082");
    public static final Color IVORY                 = new Color("Ivory", "#FFFFF0");
    public static final Color KHAKI                 = new Color("Khaki", "#F0E68C");
    public static final Color LAVENDER              = new Color("Lavender", "#E6E6FA");
    public static final Color LAVENDERBLUSH         = new Color("Lavender Blush", "#FFF0F5");
    public static final Color LAWNGREEN             = new Color("Lawn Green", "#7CFC00");
    public static final Color LEMONCHIFFON          = new Color("Lemon Chiffon", "#FFFACD");
    public static final Color LIGHTBLUE             = new Color("Light Blue", "#ADD8E6");
    public static final Color LIGHTCORAL            = new Color("Light Coral", "#F08080");
    public static final Color LIGHTCYAN             = new Color("Light Cyan", "#E0FFFF");
    public static final Color LIGHTGOLDENRODYELLOW  = new Color("Light Goldenrod Yellow", "#FAFAD2");
    public static final Color LIGHTGREEN            = new Color("Light Green", "#90EE90");
    public static final Color LIGHTGREY             = new Color("Light Grey", "#D3D3D3");
    public static final Color LIGHTPINK             = new Color("Light Pink", "#FFB6C1");
    public static final Color LIGHTSALMON           = new Color("Light Salmon", "#FFA07A");
    public static final Color LIGHTSEAGREEN         = new Color("Light Sea Green", "#20B2AA");
    public static final Color LIGHTSKYBLUE          = new Color("Light Sky Blue", "#87CEFA");
    public static final Color LIGHTSLATEGRAY        = new Color("Light Slate Gray", "#778899");
    public static final Color LIGHTSTEELBLUE        = new Color("Light Steel Blue", "#B0C4DE");
    public static final Color LIGHTYELLOW           = new Color("Light Yellow", "#FFFFE0");
    public static final Color LIME                  = new Color("Lime", "#00FF00");
    public static final Color LIMEGREEN             = new Color("Lime Green", "#32CD32");
    public static final Color LINEN                 = new Color("Linen", "#FAF0E6");
    public static final Color MAGENTA               = new Color("Magenta", "#FF00FF");
    public static final Color MAROON                = new Color("Maroon", "#800000");
    public static final Color MEDIUMAQUAMARINE      = new Color("Medium Aquamarine", "#66CDAA");
    public static final Color MEDIUMBLUE            = new Color("Medium Blue", "#0000CD");
    public static final Color MEDIUMORCHID          = new Color("Medium Orchid", "#BA55D3");
    public static final Color MEDIUMPURPLE          = new Color("Medium Purple", "#9370D8");
    public static final Color MEDIUMSEAGREEN        = new Color("Medium Sea Green", "#3CB371");
    public static final Color MEDIUMSLATEBLUE       = new Color("Medium Slate Blue", "#7B68EE");
    public static final Color MEDIUMSPRINGGREEN     = new Color("Medium Spring Green", "#00FA9A");
    public static final Color MEDIUMTURQUOISE       = new Color("Medium Turquoise", "#48D1CC");
    public static final Color MEDIUMVIOLETRED       = new Color("Medium Violet Red", "#C71585");
    public static final Color MIDNIGHTBLUE          = new Color("Midnight Blue", "#191970");
    public static final Color MINTCREAM             = new Color("Mint Cream", "#F5FFFA");
    public static final Color MISTYROSE             = new Color("Misty Rose", "#FFE4E1");
    public static final Color MOCCASIN              = new Color("Moccasin", "#FFE4B5");
    public static final Color NAVAJOWHITE           = new Color("Navajo White", "#FFDEAD");
    public static final Color NAVY                  = new Color("Navy", "#000080");
    public static final Color OLDLACE               = new Color("Old Lace", "#FDF5E6");
    public static final Color OLIVE                 = new Color("Olive", "#808000");
    public static final Color OLIVEDRAB             = new Color("Olive Drab", "#6B8E23");
    public static final Color ORANGE                = new Color("Orange", "#FFA500");
    public static final Color ORANGERED             = new Color("Orange Red", "#FF4500");
    public static final Color ORCHID                = new Color("Orchid", "#DA70D6");
    public static final Color PALEGOLDENROD         = new Color("Pale Goldenrod", "#EEE8AA");
    public static final Color PALEGREEN             = new Color("Pale Green", "#98FB98");
    public static final Color PALETURQUOISE         = new Color("Pale Turquoise", "#AFEEEE");
    public static final Color PALEVIOLETRED         = new Color("Pale Violet Red", "#D87093");
    public static final Color PAPAYAWHIP            = new Color("Papaya Whip", "#FFEFD5");
    public static final Color PEACHPUFF             = new Color("Peach Puff", "#FFDAB9");
    public static final Color PERU                  = new Color("Peru", "#CD853F");
    public static final Color PINK                  = new Color("Pink", "#FFC0CB");
    public static final Color PLUM                  = new Color("Plum", "#DDA0DD");
    public static final Color POWDERBLUE            = new Color("Powder Blue", "#B0E0E6");
    public static final Color PURPLE                = new Color("Purple", "#800080");
    public static final Color RED                   = new Color("Red", "#FF0000");
    public static final Color ROSYBROWN             = new Color("Rosy Brown", "#BC8F8F");
    public static final Color ROYALBLUE             = new Color("Royal Blue", "#4169E1");
    public static final Color SADDLEBROWN           = new Color("Saddle Brown", "#8B4513");
    public static final Color SALMON                = new Color("Salmon", "#FA8072");
    public static final Color SANDYBROWN            = new Color("Sandy Brown", "#F4A460");
    public static final Color SEAGREEN              = new Color("Sea Green", "#2E8B57");
    public static final Color SEASHELL              = new Color("Seashell", "#FFF5EE");
    public static final Color SIENNA                = new Color("Sienna", "#A0522D");
    public static final Color SILVER                = new Color("Silver", "#C0C0C0");
    public static final Color SKYBLUE               = new Color("Sky Blue", "#87CEEB");
    public static final Color SLATEBLUE             = new Color("Slate Blue", "#6A5ACD");
    public static final Color SLATEGRAY             = new Color("Slate Gray", "#708090");
    public static final Color SNOW                  = new Color("Snow", "#FFFAFA");
    public static final Color SPRINGGREEN           = new Color("Spring Green", "#00FF7F");
    public static final Color STEELBLUE             = new Color("Steel Blue", "#4682B4");
    public static final Color TAN                   = new Color("Tan", "#D2B48C");
    public static final Color TEAL                  = new Color("Teal", "#008080");
    public static final Color THISTLE               = new Color("Thistle", "#D8BFD8");
    public static final Color TOMATO                = new Color("Tomato", "#FF6347");
    public static final Color TURQUOISE             = new Color("Turquoise", "#40E0D0");
    public static final Color VIOLET                = new Color("Violet", "#EE82EE");
    public static final Color WHEAT                 = new Color("Wheat", "#F5DEB3");
    public static final Color WHITE                 = new Color("White", "#FFFFFF");
    public static final Color WHITESMOKE            = new Color("White Smoke", "#F5F5F5");
    public static final Color YELLOW                = new Color("Yellow", "#FFFF00");
    public static final Color YELLOWGREEN           = new Color("Yellow Green", "#9ACD32");
    
    // The following colors are tailored to the appearance of certain elements; you wouldn't
    // necessarily use these as trigger colors, the kind of colors that end up in labels, 
    // for example. 
    
    // GREENS
    public static final Color BUSHES                = new Color("Bushes", "#4C9933");
    public static final Color FOREST                = new Color("Forest", "#33570F");
    public static final Color GRASS                 = new Color("Grass", "#1DC943"); // "#00E533"
    
    // GRAYS
    public static final Color CLIFFS                = new Color("Cliffs", "#534A37");
    public static final Color LOW_ROCKS             = new Color("Low Rocks", "#6D665B");
    public static final Color HIGH_ROCKS            = new Color("High Rocks", "#60594C");
    
    // OLD COLORS
    public static final Color WAVES                 = new Color("Waves", "#455DDD");
    public static final Color OCEAN                 = new Color("Ocean", "#455D8B");
    public static final Color SAND                  = new Color("Sand", "#FFE3BF");
    public static final Color SURF                  = new Color("Surf", "#8B9BBA");
    public static final Color MUD                   = new Color("Mud", "#8C7E5E");
    public static final Color BUILDING_FLOOR        = new Color("Building Floor", "#EAEAEA");
    public static final Color BUILDING_WALL         = new Color("Building Color", "#7B7B7B");
    public static final Color BARELY_BUILDING_WALL  = new Color("Light Building Color", "#A4A4A4");
    public static final Color LAVA                  = new Color("Lava", "#A50F15");
    public static final Color PIER                  = new Color("Pier", "#BF955F"); 
    public static final Color DARK_PIER             = new Color("Dark Pier", "#966F3C");
    public static final Color WOOD_PILING           = new Color("Wood Piling", "#543C1C");
}
