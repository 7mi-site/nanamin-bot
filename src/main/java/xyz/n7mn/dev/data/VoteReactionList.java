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
            for (VoteReaction voteReaction1 : reactionList){
                if (voteReaction1.getReactionEmote().getEmoji().equals(voteReaction.getReactionEmote().getEmoji()) && voteReaction1.getMessageId().equals(voteReaction.getMessageId())){
                    reactionList.remove(voteReaction1);
                    return;
                }
            }
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
