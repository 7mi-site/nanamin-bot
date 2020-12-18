package xyz.n7mn.dev.data;

import java.util.Comparator;

public class VoteComparator implements Comparator<Vote> {

    @Override
    public int compare(Vote o1, Vote o2) {
        return Integer.compare(o2.getCount(), o1.getCount());
    }

}
