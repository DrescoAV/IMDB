package org.example;

import java.util.Comparator;

public class SortedComparator implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        String name1 = getNameOrTitle(o1);
        String name2 = getNameOrTitle(o2);
        return name1.compareTo(name2);
    }

    private String getNameOrTitle(Object object) {
        if (object instanceof Actor) {
            return ((Actor) object).getName();
        } else if (object instanceof Production) {
            return ((Production) object).getTitle();
        } else {
            return object.toString();
        }
    }
}