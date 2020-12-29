package xyz.n7mn.dev.Command.votecommand;

import java.util.Comparator;

public class VoteComparator implements Comparator<VoteResultData> {

    @Override
    public int compare(VoteResultData o1, VoteResultData o2) {
        return Long.compare(o2.getCount(), o1.getCount());
    }

}
