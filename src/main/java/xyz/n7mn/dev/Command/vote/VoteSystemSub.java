package xyz.n7mn.dev.Command.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoteSystemSub {

    private static List<String> messageCacheList = null;

    public static List<String> getMessageCacheList(){

        if (messageCacheList == null){
            messageCacheList = Collections.synchronizedList(new ArrayList<>());
            return messageCacheList;
        }

        List<String> me = new ArrayList<>();
        synchronized (messageCacheList){
            me.addAll(messageCacheList);
        }

        return me;
    }

    public static void addMessageCache(String id){

        if (messageCacheList == null){
            messageCacheList = Collections.synchronizedList(new ArrayList<>());
        }

        synchronized (messageCacheList){
            messageCacheList.add(id);
        }

    }

    public static void delMessageCache(String id){

        if (messageCacheList == null){
            messageCacheList = Collections.synchronizedList(new ArrayList<>());
        }

        List<String> temp = new ArrayList<>();
        synchronized (messageCacheList){
            temp.addAll(messageCacheList);
        }

        int i = 0;
        for (String t : temp){
            if (t.equals(id)){
                break;
            }

            i++;
        }

        synchronized (messageCacheList){
            messageCacheList.remove(i);
        }
    }
}
