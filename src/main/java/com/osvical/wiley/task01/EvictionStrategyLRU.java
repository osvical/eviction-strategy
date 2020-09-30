package com.osvical.wiley.task01;

import java.util.HashMap;
import java.util.LinkedList;

public class EvictionStrategyLRU implements IEvictionStrategy {
    private final LinkedList<String> linkedList = new LinkedList<>();

    @Override
    public void touch(String task) {
        linkedList.remove(task);
        linkedList.addLast(task);
    }

    @Override
    public void evict(HashMap<String, Integer> cachedResults, Integer maxDeep) {
        assert cachedResults.size() == linkedList.size();
        while ( cachedResults.size() >= maxDeep ) {
            cachedResults.remove( linkedList.removeFirst() );
        }
    }

}
