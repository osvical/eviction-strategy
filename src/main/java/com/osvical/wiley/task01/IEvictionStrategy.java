package com.osvical.wiley.task01;

import java.util.HashMap;

public interface IEvictionStrategy {
    void touch(String task);
    void evict(HashMap<String,Integer> cachedResults, Integer maxDeep);
}
