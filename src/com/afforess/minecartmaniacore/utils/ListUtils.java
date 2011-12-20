package com.afforess.minecartmaniacore.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    
    public static ArrayList<?> toArrayList(final List<?> list) {
        if (!(list instanceof ArrayList)) {
            final ArrayList<Object> newList = new ArrayList<Object>(list.size());
            for (final Object c : list) {
                newList.add(c);
            }
            return newList;
        }
        return (ArrayList<?>) list;
    }
}
