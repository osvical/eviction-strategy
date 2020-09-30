import com.osvical.wiley.task01.*;
import org.junit.Test;

public class AppTest {

    // внедрение счетчика реальных вычислений мимо кеша
    Integer workerDoCount;
    WorkerImpl testWorker = new WorkerImpl() {
        @Override
        public Integer doSomething(String task) {
            workerDoCount++;
            return super.doSomething(task);
        }
    };

    @Test public void testLRU() {
        workerDoCount = 0;
        WorkerCached workerCached = new WorkerCached( testWorker, new EvictionStrategyLRU() );
        workerCached.setMaxDeep(3);
        workerCached.doSomething("task1");
        workerCached.doSomething("task2");
        workerCached.doSomething("task3");

        workerCached.doSomething("task2"); // поднятие актуальности task2 из середины наверх, опустив task3
        workerCached.doSomething("task4"); // task4 переполнил емкость кеша, вытолкнув task1
        assert ! workerCached.getCachedResults().containsKey("task1");
        workerCached.doSomething("task5"); // task5 вытолкнул task3 вместо task2, благодаря второму вызову task2
        assert ! workerCached.getCachedResults().containsKey("task3");

        // содержание кеша
        assert workerCached.getCachedResults().containsKey("task2");
        assert workerCached.getCachedResults().containsKey("task4");
        assert workerCached.getCachedResults().containsKey("task5");

        // соответствие заданному максимальному размеру
        assert workerCached.getCachedResults().size() == 3;

        // вычисления произвелись 5 раза, по количеству уникальных задач
        System.out.println(workerDoCount);
        assert workerDoCount == 5;
    }

    @Test public void testLFU() {
        workerDoCount = 0;
        WorkerCached workerCached = new WorkerCached( testWorker, new EvictionStrategyLFU() );
        workerCached.setMaxDeep(3);
        workerCached.doSomething("task1");
        workerCached.doSomething("task2");
        workerCached.doSomething("task3");

        workerCached.doSomething("task1");
        workerCached.doSomething("task1");

        workerCached.doSomething("task3");

        workerCached.doSomething("task4");
        assert ! workerCached.getCachedResults().containsKey("task2");

        workerCached.doSomething("task5");
        assert ! workerCached.getCachedResults().containsKey("task4");

        workerCached.doSomething("task5");
        workerCached.doSomething("task5");

        workerCached.doSomething("task6");
        assert ! workerCached.getCachedResults().containsKey("task3");

        // содержание кеша
        assert workerCached.getCachedResults().containsKey("task1");
        assert workerCached.getCachedResults().containsKey("task5");
        assert workerCached.getCachedResults().containsKey("task6");

        // соответствие заданному максимальному размеру
        assert workerCached.getCachedResults().size() == 3;

        // вычисления произвелись 6 раза, по количеству уникальных задач
        System.out.println(workerDoCount);
        assert workerDoCount == 6;
    }
}
