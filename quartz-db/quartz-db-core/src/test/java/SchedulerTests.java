import net.c0ffee1.db.coroutine.FixedScheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulerTests {

    @Test
    public void testSchedulers() throws ExecutionException, InterruptedException {
        FixedScheduler<TestResources> scheduler = new FixedScheduler<>(30, TestResources.class);

        scheduler.inFuture(()->{
            Thread.sleep(1000);
            System.out.println("hello1!");
            return 1;
        }, TestResources.FILE);

        scheduler.inFuture(()->{
            Thread.sleep(200);
            System.out.println("hello2!");
            return 1;
        }, TestResources.FILE);

        Thread.sleep(2000);

        final int numberOfTasks = 600000;

        List<Future<Integer>> futures = new ArrayList<>();

        AtomicInteger integer = new AtomicInteger(0);
        AtomicInteger integer2 = new AtomicInteger(0);


        for(int n = 0; n < numberOfTasks; n++){
            futures.add(scheduler.inFuture(()-> {
                integer.incrementAndGet();
                return 42;
            }, TestResources.IO));

            futures.add(scheduler.inFuture(()-> {
                integer2.incrementAndGet();
                return 43;
            }, TestResources.SOME_OTHER));
        }

        long startTime = System.currentTimeMillis();

        // Wait for all tasks to complete

        Thread.sleep(1000);
        long sum = integer.get() + integer2.get();
        System.out.println("throughtput " + sum + " / s");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;


        System.out.println("Executed " + sum+ " tasks in " + duration + " milliseconds.");
    }
    public enum TestResources{
        IO,
        FILE,
        OTHER,
        SOME_OTHER,
        YET_ANOTHER
    }
}
