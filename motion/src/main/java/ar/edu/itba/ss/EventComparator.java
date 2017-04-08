package ar.edu.itba.ss;

import java.util.Comparator;

/**
 * Created by jpascale on 4/7/17.
 */
public class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        if (o1.getTime() < o2.getTime()) return -1;
        if (o1.getTime() > o2.getTime()) return 1;
        return 0;
    }
}
