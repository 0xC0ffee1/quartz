package net.c0ffee1.db.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * A scheduler designed to run tasks in order asynchronously
 * Useful in Minecraft environments, where database operations must not block the main thread,
 * but still have to be executed in order
 */
open class FixedScheduler(nThreads : Int) : CoroutineScope {
    private val isExecutorThread = ThreadLocal.withInitial { false }

    private val executorService = Executors.newFixedThreadPool(nThreads) { runnable ->
        Thread {
            isExecutorThread.set(true)
            try {
                runnable.run()
            } finally {
                isExecutorThread.remove()
            }
        }
    }

    override val coroutineContext: CoroutineContext = executorService.asCoroutineDispatcher()

    fun shutdown(timeout: Long = 15, unit: TimeUnit = TimeUnit.SECONDS) {
        coroutineContext.cancel()
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(timeout, unit)) {
                executorService.shutdownNow()
                LoggerFactory.getLogger("OrderedDatabaseScheduler").error("Database scheduler did not terminate in specified time!")
            }
        } catch (ie: InterruptedException) {
            executorService.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }

    //For Java
    /**
     * Run a task wrapped returning its result in completable future in this single threaded context
     * Ensures that method is not called from the same context, and if it is, calls it directly
     */
    fun <T> inFuture(callable: Callable<T>): CompletableFuture<T> {
        return if (isExecutorThread.get()) {
            CompletableFuture.completedFuture(callable.call())
        } else {
            future {
                callable.call()
            }
        }
    }
}