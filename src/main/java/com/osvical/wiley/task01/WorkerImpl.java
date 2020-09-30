package com.osvical.wiley.task01;

public class WorkerImpl implements IWorker {
    @Override
    public Integer doSomething(String task) {
        return task.hashCode();
    }
}
