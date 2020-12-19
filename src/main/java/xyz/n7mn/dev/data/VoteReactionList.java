package xyz.n7mn.dev.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VoteReactionList {

    List<VoteReaction> reactionList = Collections.synchronizedList(new ArrayList<>());

    public void addList(VoteReaction voteReaction){

        synchronized (reactionList){
            reactionList.add(voteReaction);
        }

    }

    public void deleteList(VoteReaction voteReaction){

        synchronized (reactionList){
            reactionList.remove(voteReaction);
        }

    }

    public List<VoteReaction> getList(){

        List<VoteReaction> voteReaction = new ArrayList<>();
        synchronized (reactionList){
            voteReaction.addAll(reactionList);
        }

        return voteReaction;
    }

    public VoteReaction getData(UUID uuid){
        List<VoteReaction> list = getList();
        for (VoteReaction voteReaction : list){

            if (voteReaction.getUuid().equals(uuid)){
                return voteReaction;
            }

        }

        return null;
    }
}
