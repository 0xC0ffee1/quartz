package net.c0ffee1.db.coroutine

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * A scheduler designed to run tasks in order asynchronously
 * Useful in Minecraft environments, where database operations must not block the main thread,
 * but still have to be executed in order
 */
class OrderedDatabaseScheduler(private val taskTimeout: Long = 5000L) : CoroutineScope { // Default timeout of 5000ms
    private val executorService = Executors.newSingleThreadExecutor()
    override val coroutineContext: CoroutineContext = executorService.asCoroutineDispatcher()

    private var lastTask: Job = Job().apply { complete() }

    fun run(block: suspend CoroutineScope.() -> Unit) {
        lastTask = launch {
            lastTask.join() // Ensure tasks execute in order
            withTimeoutOrNull(taskTimeout) { // Apply timeout to each task
                block()
            } ?: println("Task timed out after $taskTimeout ms.")
        }
    }

    fun waitForCompletion(timeout: Long, unit: TimeUnit) {
        runBlocking {
            withTimeoutOrNull(unit.toMillis(timeout)) {
                lastTask.join()
            } ?: println("Timeout occurred waiting for tasks to complete.")
        }
    }

    fun shutdown() {
        coroutineContext.cancel()
        executorService.shutdown()
    }

    // Other methods remain unchanged
}