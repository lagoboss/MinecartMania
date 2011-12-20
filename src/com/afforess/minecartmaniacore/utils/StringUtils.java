package com.afforess.minecartmaniacore.utils;

public class StringUtils {
    
    public static String getNumber(final String s) {
        String n = "";
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (Character.isDigit(c) || (c == '.') || (c == '-')) {
                n += c;
            }
        }
        return n;
    }
    
    public static boolean containsLetters(final String s) {
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (Character.isLetter(c))
                return true;
        }
        return false;
    }
    
    public static String removeWhitespace(final String s) {
        String s1 = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                s1 += s.charAt(i);
            }
        }
        return s1;
    }
    
    public static String removeBrackets(final String s) {
        String str = "";
        boolean isStation = false;
        if (s.toLowerCase().contains("st-")) { //see if we need to make sure [ ] in the middle do not get removed. 
                                               //Also lower case because the same sign and line will come in as "st-" AND "St-"
            isStation = true;
        }
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if ((c == ']') || (c == '[')) {
                if (!isStation) {
                    continue; // we have a non-station string so remove all brackets 
                }
                // we have a station string if we got this far
                if ((i == 0) && (c == '[')) {
                    continue; //only strip beginning [ bracket.
                }
                if ((i == (s.length() - 1)) && (c == ']') && (s.charAt(0) == '[')) {
                    continue; //only strip ending bracket if a beginning bracket exists
                }
            }
            str += c;
        }
        
        return str;
    }
    
    public static String addBrackets(final String s) {
        return "[" + removeBrackets(s) + "]";
    }
    
    public static String indent(final String s, final int times) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append("\t");
        }
        sb.append(s);
        return sb.toString();
    }
    
    /**
     * Join an array command into a String
     * 
     * @author Hidendra
     * @param arr
     * @param offset
     * @return
     */
    public static String join(final String[] arr, final int offset) {
        return join(arr, offset, " ");
    }
    
    /**
     * Join an array command into a String
     * 
     * @author Hidendra
     * @param arr
     * @param offset
     * @param delim
     * @return
     */
    public static String join(final String[] arr, final int offset, final String delim) {
        String str = "";
        
        if ((arr == null) || (arr.length == 0))
            return str;
        
        for (int i = offset; i < arr.length; i++) {
            str += arr[i] + delim;
        }
        
        return str.trim();
    }
}
