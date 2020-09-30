package com.osvical.wiley.task01;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EvictionStrategyLFU implements IEvictionStrategy {

    private LinkedHashMap<String,Integer> counter = new LinkedHashMap<>();

    @Override
    public void touch(String task) {
        if ( counter.containsKey(task) ) {
            counter.put( task, counter.get(task) +1 );
        } else {
            counter.put( task, 0 );
        }
    }

    @Override
    public void evict(HashMap<String,Integer> cachedResults, Integer maxDeep) {
        // sort
        counter = counter.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                )
        );

        Map.Entry<String,Integer> e;
        while ( cachedResults.size() >= maxDeep ) {
            e = counter.entrySet().iterator().next();
            cachedResults.remove(e.getKey());

            // ToDo можно доработать алгоритм, не сбрасывая сразу счетчики вытесненных таско
            //  Но в простейшем случае - очистка счетчика
            counter.remove(e.getKey());
        }
    }

}
