package com.osvical.wiley.task01;

import java.util.HashMap;

public class WorkerCached implements IWorker {
    private final IWorker worker;
    private final HashMap<String,Integer> cachedResults = new HashMap<>();
    private IEvictionStrategy strategy;
    private Integer maxDeep = 5;

    public WorkerCached(IWorker worker, IEvictionStrategy strategy) {
        this.worker = worker;
        this.strategy = strategy;
    }

    public HashMap<String, Integer> getCachedResults() {
        return cachedResults;
    }

    public void setMaxDeep(Integer maxDeep) {
        this.maxDeep = maxDeep;
    }

    public void setStrategy(IEvictionStrategy strategy) {
        // ToDO если в задании имелось ввиду менять стратегию в рантайме,
        //  то предусмотреть инициализацию внутреннего состояния новой стратегии
        assert false;
        this.strategy = strategy;
    }

    @Override
    public Integer doSomething(String task) {
        Integer result = cachedResults.get(task);

        if ( result != null ) {
            strategy.touch(task);
            return result;
        }

        result = worker.doSomething(task);
        assert result != null;

        if ( cachedResults.size() >= maxDeep ) {
            strategy.evict(cachedResults, maxDeep);
        }

        cachedResults.put(task, result);
        strategy.touch(task);

        return result;
    }
}
