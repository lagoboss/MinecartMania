package com.afforess.minecartmaniacore.utils;

public class MathUtils {
    
    public static double range(double value, final double max, final double min) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }
        return value;
    }
    
    public static int range(int value, final int max, final int min) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }
        return value;
    }
}
